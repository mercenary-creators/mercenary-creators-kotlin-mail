/*
 * Copyright (c) 2019, Mercenary Creators Company. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.mercenary.creators.kotlin.mail.javamail

import co.mercenary.creators.kotlin.mail.*
import co.mercenary.creators.kotlin.util.*
import co.mercenary.creators.kotlin.util.logging.ILogging
import java.util.concurrent.ConcurrentHashMap
import javax.mail.*
import kotlin.math.*

open class JavaMailMessageSender : AbstractConfigurableMailMessageSender() {

    private var maxcalc: () -> Int = { MAX_PARALLELISM }

    private var mincalc: () -> Int = { MIN_PARALLELISM }

    private val session: Session by lazy {
        Session.getInstance(getConfiguration())
    }

    protected val logger: ILogging by lazy {
        LoggingFactory.logger(this)
    }

    protected open fun text(message: MailMessage<*>): String? = if (message.isValid()) {
        when (message) {
            is TextMailMessage -> message.getBody()
            is MimeMailMessage -> when (val body = message.getBody()) {
                null -> null
                else -> body.getMessageBodyText().orEmpty()
            }
            else -> null
        }
    }
    else null

    protected open fun semd(message: MailMessage<*>, sess: Session, maps: ConcurrentHashMap<String, Transport>): MailMessageSenderResult {
        val text = text(message)
        val from = Mail.address(message.getFrom())
        return if ((text != null) && (from != null)) {
            val uuid = uuid()
            val tkey = getThreadName()
            val call = maps.computeIfAbsent(tkey) {
                getTransport(sess)
            }
            val html = when (message) {
                is MimeMailMessage -> when (val body = message.getBody()) {
                    null -> null
                    else -> body.getMessageBodyHtml()
                }
                else -> null
            }
            val part = when (message) {
                is MimeMailMessage -> when (val body = message.getBody()) {
                    null -> false
                    else -> body.isEmpty().not()
                }
                else -> false
            }
            val mime = when (part.or(html != null)) {
                true -> JavaMimeMessageAdapter(sess, MimeMode.MIXED)
                else -> JavaMimeMessageAdapter(sess)
            }
            mime.setFrom(from)
            mime.setDate(message.getDate())
            mime.setSubject(message.getSubject())
            mime.setReplyTo(Mail.address(message.getReplyTo()).orElse { from })
            mime.setRecipients(Message.RecipientType.TO, Mail.addresses(message.getTo()))
            mime.setRecipients(Message.RecipientType.CC, Mail.addresses(message.getCc()))
            mime.setRecipients(Message.RecipientType.BCC, Mail.addresses(message.getBcc()))
            mime.setBoth(text, html)
            if (part) {
                when (message) {
                    is MimeMailMessage -> {
                        val body = message.getBody()
                        if (body != null) {
                            for ((name, data) in body.getAttachParts()) {
                                mime.addAttachDataSource(name, data)
                            }
                            for ((name, data) in body.getInlineParts()) {
                                mime.addInlineDataSource(name, data)
                            }
                        }
                    }
                }
            }
            if (call.isConnected) {
                try {
                    mime.send(call)
                    MailMessageSenderResultData(mime.getId(), mime.getDate(), true)
                }
                catch (cause: Throwable) {
                    Throwables.thrown(cause)
                    logger.error(cause) {
                        uuid.plus(SPACE_STRING).plus(tkey)
                    }
                    MailMessageSenderResultData(message = cause.toString())
                }
            }
            else {
                MailMessageSenderResultData()
            }
        }
        else {
            MailMessageSenderResultData()
        }
    }

    override fun getMaxParallel() = maxcalc.invoke()

    override fun setMaxParallel(calc: () -> Int) {
        maxcalc = calc
    }

    override fun getMinParallel() = mincalc.invoke()

    override fun setMinParallel(calc: () -> Int) {
        mincalc = calc
    }

    override fun setMaxParallel(size: Int) {
        setMaxParallel {
            min(max(abs(size), MIN_PARALLELISM), MAX_PARALLELISM)
        }
    }

    override fun setMinParallel(size: Int) {
        setMinParallel {
            max(min(abs(size), MAX_PARALLELISM), MIN_PARALLELISM)
        }
    }

    override fun getParallelism(parallelism: Int): Int {
        return max(min(min(abs(parallelism).plus(1).div(2).times(2), getProcessors().plus(1).div(2)), getMaxParallel()), getMinParallel())
    }

    override fun send(messages: List<MailMessage<*>>): List<MailMessageSenderResult> {
        val many = messages.size
        if (many == 0) {
            return emptyList()
        }
        val sess = session
        val size = getParallelism(many)
        val maps = ConcurrentHashMap<String, Transport>(size)
        try {
            ParallelScheduler("$size-mail-sender", size).also { on ->
                return messages.toFlux().parallel(size).runOn(on).map { semd(it, sess, maps) }.sequential(size).toList().also {
                    on.dispose()
                }
            }
        }
        finally {
            maps.values.forEach {
                if (it.isConnected) {
                    try {
                        it.close()
                    }
                    catch (cause: Throwable) {
                        Throwables.thrown(cause)
                    }
                }
            }
        }
    }

    protected open fun getTransport(session: Session): Transport = session.transport.also {
        when (val username = toTrimOrNull(getUserName())) {
            null -> it.connect(getHostName(), getHostPort(), null, null)
            else -> it.connect(getHostName(), getHostPort(), username, getPassword())
        }
    }

    companion object {
        const val MIN_PARALLELISM = 1
        const val MAX_PARALLELISM = 16
    }
}