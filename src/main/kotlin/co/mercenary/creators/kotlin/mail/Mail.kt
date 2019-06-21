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

package co.mercenary.creators.kotlin.mail

import co.mercenary.creators.kotlin.io.*
import co.mercenary.creators.kotlin.io.data.*
import java.io.*
import java.net.*
import java.nio.charset.Charset
import java.nio.file.Path
import java.util.*

import javax.mail.internet.InternetAddress

class Mail(private val probe: ContentTypeProbe = IO.getContentTypeProbe(), block: Mail.() -> Unit) : Validated {

    private val messages = arrayListOf<MailMessage<*>>()

    init {
        block(this)
    }

    fun text(block: TextMailMessageBuilder.() -> Unit) {
        TextMailMessage().also {
            TextMailMessageBuilder(it).apply(block)
            messages += it
        }
    }

    fun mime(block: MimeMailMessageBuilder.() -> Unit) {
        MimeMailMessage().also {
            MimeMailMessageBuilder(it).apply(block)
            messages += it
        }
    }

    fun getContentTypeProbe() = probe

    fun size() = messages.size

    fun clear() = messages.clear()

    override fun isValid() = messages.all { it.isValid() }

    fun send(sender: MailMessageSender) = sender.send(messages).also { clear() }

    fun <S : MailMessageSender> send(builder: Builder<S>) = send(builder.build())

    abstract inner class MailMessageBuilder(private val mail: MailMessage<*>) {

        fun from(from: String) {
            mail.setFrom(from)
        }

        fun reply(repl: String) {
            mail.setReplyTo(repl)
        }

        fun to(list: List<String>) {
            mail.addTo(list)
        }

        fun cc(list: List<String>) {
            mail.addCc(list)
        }

        fun bcc(list: List<String>) {
            mail.addBcc(list)
        }

        fun date(date: Date = Date()) {
            mail.setDate(date)
        }

        fun subject(subj: String) {
            mail.setSubject(subj)
        }
    }

    inner class TextMailMessageBuilder(private val text: TextMailMessage) : MailMessageBuilder(text) {

        fun body(data: CharSequence) {
            text.setBody(data.toString())
        }

        fun body(data: () -> String) {
            text.setBody(data.invoke())
        }

        fun body(data: ByteArray, charset: Charset = Charsets.UTF_8) {
            text.setBody(String(data, charset))
        }

        fun body(data: URL, charset: Charset = Charsets.UTF_8) {
            body(data.toByteArray(), charset)
        }

        fun body(data: File, charset: Charset = Charsets.UTF_8) {
            body(data.toByteArray(), charset)
        }

        fun body(data: Path, charset: Charset = Charsets.UTF_8) {
            body(data.toFile(), charset)
        }

        fun body(data: InputStream, charset: Charset = Charsets.UTF_8) {
            body(data.toByteArray(), charset)
        }

        fun body(data: InputStreamSupplier, charset: Charset = Charsets.UTF_8) {
            body(data.getInputStream(), charset)
        }
    }

    inner class MimeMailMessageBuilder(private val mime: MimeMailMessage) : MailMessageBuilder(mime) {

        fun body(block: MimeMailMessageBodyBuilder.() -> Unit) {
            MimeMailMessageBody().also {
                MimeMailMessageBodyBuilder(it).apply(block)
                mime.setBody(it)
            }
        }
    }

    inner class MimeMailMessageBodyBuilder(private val body: MimeMailMessageBody) {

        fun message(data: CharSequence) {
            body.setMessageBodyText(data.toString())
        }

        fun message(data: () -> String) {
            body.setMessageBodyText(data.invoke())
        }

        fun message(data: ByteArray, charset: Charset = Charsets.UTF_8) {
            body.setMessageBodyText(String(data, charset))
        }

        fun message(data: URL, charset: Charset = Charsets.UTF_8) {
            message(data.toByteArray(), charset)
        }

        fun message(data: File, charset: Charset = Charsets.UTF_8) {
            message(data.toByteArray(), charset)
        }

        fun message(data: Path, charset: Charset = Charsets.UTF_8) {
            message(data.toFile(), charset)
        }

        fun message(data: InputStream, charset: Charset = Charsets.UTF_8) {
            message(data.toByteArray(), charset)
        }

        fun message(data: InputStreamSupplier, charset: Charset = Charsets.UTF_8) {
            message(data.getInputStream(), charset)
        }

        fun html(data: CharSequence) {
            body.setMessageBodyHtml(data.toString())
        }

        fun html(data: () -> String) {
            body.setMessageBodyHtml(data.invoke())
        }

        fun html(data: ByteArray, charset: Charset = Charsets.UTF_8) {
            body.setMessageBodyHtml(String(data, charset))
        }

        fun html(data: URL, charset: Charset = Charsets.UTF_8) {
            html(data.toByteArray(), charset)
        }

        fun html(data: File, charset: Charset = Charsets.UTF_8) {
            html(data.toByteArray(), charset)
        }

        fun html(data: Path, charset: Charset = Charsets.UTF_8) {
            html(data.toFile(), charset)
        }

        fun html(data: InputStream, charset: Charset = Charsets.UTF_8) {
            html(data.toByteArray(), charset)
        }

        fun html(data: InputStreamSupplier, charset: Charset = Charsets.UTF_8) {
            html(data.getInputStream(), charset)
        }

        fun inline(name: String, data: URL, type: String = DEFAULT_CONTENT_TYPE) {
            inline(name, URLContentResource(data, getContentTypeProbe().getContentType(data, type)))
        }

        fun inline(name: String, data: Path, type: String = DEFAULT_CONTENT_TYPE) {
            inline(name, data.toFile(), type)
        }

        fun inline(name: String, data: File, type: String = DEFAULT_CONTENT_TYPE) {
            inline(name, FileContentResource(data, getContentTypeProbe().getContentType(data, type)))
        }

        fun inline(name: String, data: ByteArray, type: String = DEFAULT_CONTENT_TYPE) {
            inline(name, ByteArrayContentResource(data, name, getContentTypeProbe().getContentType(name, type)))
        }

        fun inline(name: String, data: ContentResource) {
            body.addInlinePart(name, data)
        }

        fun attach(name: String, data: URL, type: String = DEFAULT_CONTENT_TYPE) {
            attach(name, URLContentResource(data, getContentTypeProbe().getContentType(data, type)))
        }

        fun attach(name: String, data: Path, type: String = DEFAULT_CONTENT_TYPE) {
            attach(name, data.toFile(), type)
        }

        fun attach(name: String, data: File, type: String = DEFAULT_CONTENT_TYPE) {
            attach(name, FileContentResource(data, getContentTypeProbe().getContentType(data, type)))
        }

        fun attach(name: String, data: ByteArray, type: String = DEFAULT_CONTENT_TYPE) {
            attach(name, ByteArrayContentResource(data, name, getContentTypeProbe().getContentType(name, type)))
        }

        fun attach(name: String, data: ContentResource) {
            body.addAttachPart(name, data)
        }
    }

    companion object {

        @JvmStatic
        fun parse(address: String?): String? = address(address)?.address

        @JvmStatic
        fun address(address: String?): InternetAddress? {
            val text = toTrimOrNull(address) ?: return null
            return try {
                InternetAddress(text, true)
            }
            catch (_: Throwable) {
                null
            }
        }

        @JvmStatic
        fun addresses(addresses: List<String>): Array<InternetAddress> {
            return addresses.distinct().mapNotNull { address(it) }.toTypedArray()
        }
    }
}