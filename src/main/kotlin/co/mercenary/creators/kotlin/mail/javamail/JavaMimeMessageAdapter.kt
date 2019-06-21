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

import co.mercenary.creators.kotlin.io.EMPTY_STRING
import co.mercenary.creators.kotlin.io.data.ContentResource
import co.mercenary.creators.kotlin.mail.MimeMode
import java.util.*
import javax.activation.*
import javax.mail.*
import javax.mail.internet.*

class JavaMimeMessageAdapter(private val mime: JavaMimeMessage) {

    private var root: MimeMultipart? = null

    private var main: MimeMultipart? = null

    constructor(session: Session, mode: MimeMode) : this(JavaMimeMessage(session), mode)

    constructor(mime: JavaMimeMessage, mode: MimeMode) : this(mime) {
        when (mode) {
            MimeMode.NONE -> {
                setMimeMessageParts(root = null, main = null)
            }
            MimeMode.MIXED -> {
                val root = MimeMultipart("mixed")
                getMimeMessage().setContent(root)
                setMimeMessageParts(root = root, main = root)
            }
            MimeMode.RELATED -> {
                val root = MimeMultipart("related")
                getMimeMessage().setContent(root)
                setMimeMessageParts(root = root, main = root)
            }
            MimeMode.MIXED_RELATED -> {
                val root = MimeMultipart("mixed")
                getMimeMessage().setContent(root)
                val main = MimeMultipart("related")
                val body = MimeBodyPart()
                body.setContent(main)
                root.addBodyPart(body)
                setMimeMessageParts(root = root, main = main)
            }
        }
    }

    fun getMimeMessage() = mime

    fun setMimeMessageParts(root: MimeMultipart?, main: MimeMultipart?) {
        this.root = root
        this.main = main
    }

    fun isMultipart(): Boolean = (root != null)

    fun getRootMimeMultipart(): MimeMultipart = root ?: throw IllegalStateException("root")

    fun getMainMimeMultipart(): MimeMultipart = main ?: throw IllegalStateException("main")

    fun setSubject(text: String?) {
        getMimeMessage().subject = text ?: EMPTY_STRING
    }

    fun setFrom(from: InternetAddress) {
        getMimeMessage().setFrom(from)
    }

    fun setReplyTo(back: InternetAddress) {
        getMimeMessage().replyTo = arrayOf(back)
    }

    fun setRecipients(type: Message.RecipientType, list: Array<InternetAddress>) {
        if (list.isNotEmpty()) {
            getMimeMessage().setRecipients(type, list)
        }
    }

    fun getDate(): Date? = getMimeMessage().sentDate

    fun setDate(date: Date?) {
        if (date != null) {
            getMimeMessage().sentDate = date
        }
    }

    fun getId(): String? = getMimeMessage().messageID

    fun send(call: Transport) {
        getMimeMessage().send(call)
    }

    fun setText(text: String, html: Boolean = false) {
        val part = if (isMultipart()) getMainPart() else getMimeMessage()
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

    internal fun addDataSource(name: String, data: ContentResource, disposition: String, part: MimeMultipart) {
        MimeBodyPart().also { body ->
            body.fileName = MimeUtility.encodeText(name)
            body.disposition = disposition
            body.dataHandler = DataHandler(getDataSource(name, data))
            part.addBodyPart(body)
        }
    }

    internal fun getDataSource(name: String, data: ContentResource): DataSource {
        return object : DataSource {
            override fun getName() = name
            override fun getInputStream() = data.getInputStream()
            override fun getContentType() = data.getContentType()
            override fun getOutputStream() = data.getOutputStream()
        }
    }
}