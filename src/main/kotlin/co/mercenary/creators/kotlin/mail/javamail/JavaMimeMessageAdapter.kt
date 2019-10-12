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
import co.mercenary.creators.kotlin.util.MercenaryExceptiion
import co.mercenary.creators.kotlin.util.io.*
import java.util.*
import javax.activation.*
import javax.mail.*
import javax.mail.internet.*

class JavaMimeMessageAdapter @JvmOverloads constructor(private val mime: JavaMimeMessage, mode: MimeMode = MimeMode.NONE) {

    @JvmOverloads
    constructor(session: Session, mode: MimeMode = MimeMode.NONE) : this(JavaMimeMessage(session), mode)

    private var root: MimeMultipart? = null

    private var main: MimeMultipart? = null

    init {
        when (mode) {
            MimeMode.NONE -> {
                setMimeMessageParts(null)
            }
            MimeMode.MIXED -> {
                MimeMultipart("mixed").also { root ->
                    mime.setContent(root)
                    setMimeMessageParts(root)
                }
            }
            MimeMode.RELATED -> {
                MimeMultipart("related").also { root ->
                    mime.setContent(root)
                    setMimeMessageParts(root)
                }
            }
            MimeMode.MIXED_RELATED -> {
                MimeMultipart("mixed").also { root ->
                    mime.setContent(root)
                    MimeMultipart("related").also { main ->
                        MimeBodyPart().also { body ->
                            body.setContent(main)
                            root.addBodyPart(body)
                        }
                        setMimeMessageParts(root, main)
                    }
                }
            }
        }
    }

    @JvmOverloads
    fun setMimeMessageParts(root: MimeMultipart?, main: MimeMultipart? = root) {
        this.root = root
        this.main = main
    }

    fun isMultipart(): Boolean = (root != null)

    fun getRootMimeMultipart(): MimeMultipart = root ?: throw MercenaryExceptiion("root")

    fun getMainMimeMultipart(): MimeMultipart = main ?: throw MercenaryExceptiion("main")

    fun setSubject(text: String?) {
        mime.subject = text ?: EMPTY_MAIL_SUBJECT
    }

    fun setFrom(from: InternetAddress) {
        mime.setFrom(from)
    }

    fun addFrom(list: Array<InternetAddress>) {
        if (list.isNotEmpty()) {
            mime.addFrom(list)
        }
    }

    fun setReplyTo(back: InternetAddress) {
        mime.replyTo = arrayOf(back)
    }

    fun setRecipients(type: Message.RecipientType, list: Array<InternetAddress>) {
        if (list.isNotEmpty()) {
            mime.setRecipients(type, list)
        }
    }

    fun getDate(): Date? = mime.sentDate

    fun setDate(date: Date?) {
        if (date != null) {
            mime.sentDate = date
        }
    }

    fun getId(): String? = mime.messageID

    fun send(call: Transport): Boolean {
        return mime.send(call)
    }

    fun isSaved(): Boolean {
        return mime.saved
    }

    @JvmOverloads
    fun setText(text: String, html: Boolean = false) {
        val part = if (isMultipart()) getMainPart() else mime
        if (html) setHtmlText(part, text) else setPlainText(part, text)
    }

    fun setText(text: String, html: String) {
        val main = MimeMultipart("alternative")
        getMainPart().setContent(main, "text/alternative")
        MimeBodyPart().also {
            setPlainText(it, text)
            main.addBodyPart(it)
        }
        MimeBodyPart().also {
            setHtmlText(it, html)
            main.addBodyPart(it)
        }
    }

    fun setBoth(text: String, html: String?) {
        if (html != null) {
            setText(text, html)
        }
        else {
            setText(text)
        }
    }

    fun setPlainText(part: MimePart, text: String) {
        part.setText(text)
    }

    fun setHtmlText(part: MimePart, text: String) {
        part.setContent(text, "text/html")
    }

    fun getMainPart(): MimeBodyPart {
        var body: MimeBodyPart? = null
        val part = getMainMimeMultipart()
        for (i in 0 until part.count) {
            val temp = part.getBodyPart(i)
            if (temp.fileName == null) {
                body = temp as MimeBodyPart
            }
        }
        if (body == null) {
            body = MimeBodyPart()
            part.addBodyPart(body)
        }
        return body
    }

    fun addInlineDataSource(name: String, data: ContentResource) {
        addDataSource(name, data, MimeBodyPart.INLINE, getMainMimeMultipart())
    }

    fun addAttachDataSource(name: String, data: ContentResource) {
        addDataSource(name, data, MimeBodyPart.ATTACHMENT, getRootMimeMultipart())
    }

    private fun addDataSource(name: String, data: ContentResource, disposition: String, part: MimeMultipart) {
        MimeBodyPart().also { body ->
            body.fileName = MimeUtility.encodeText(name)
            body.disposition = disposition
            body.dataHandler = DataHandler(getDataSource(name, data))
            part.addBodyPart(body)
        }
    }

    private fun getDataSource(name: String, data: ContentResource): DataSource {
        return object : DataSource {
            override fun getName() = name
            override fun getInputStream() = data.getInputStream()
            override fun getContentType() = data.getContentType()
            override fun getOutputStream() = if (data is OutputContentResource) data.getOutputStream() else EmptyOutputStream.INSTANCE
        }
    }
}