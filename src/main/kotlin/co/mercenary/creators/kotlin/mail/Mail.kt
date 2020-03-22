/*
 * Copyright (c) 2020, Mercenary Creators Company. All rights reserved.
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

import co.mercenary.creators.kotlin.util.*
import co.mercenary.creators.kotlin.util.io.*
import co.mercenary.creators.kotlin.util.type.*
import java.io.*
import java.net.*
import java.nio.channels.ReadableByteChannel
import java.nio.charset.Charset
import java.nio.file.Path
import java.util.*
import javax.mail.internet.InternetAddress

@MailDsl
class Mail @JvmOverloads constructor(private val probe: ContentTypeProbe = DefaultContentTypeProbe(), block: Mail.() -> Unit) : Validated {

    private val messages = ArrayList<MailMessage<*>>()

    init {
        block(this)
    }

    @MailDsl
    fun text(block: TextMailMessageBuilder.() -> Unit) {
        TextMailMessage().also {
            TextMailMessageBuilder(it).apply(block)
            messages += it
        }
    }

    @MailDsl
    fun mime(block: MimeMailMessageBuilder.() -> Unit) {
        MimeMailMessage().also {
            MimeMailMessageBuilder(it).apply(block)
            messages += it
        }
    }

    fun getContentTypeProbe() = probe

    fun size() = messages.size

    fun clear() = messages.clear()

    @MailDsl
    override fun isValid() = messages.all { it.isValid() }

    fun send(sender: MailMessageSender) = sender.send(messages).also { clear() }

    fun <S : MailMessageSender> send(builder: Builder<S>) = send(builder.build())

    abstract inner class MailMessageBuilder<T : MailMessageBuilder<T>>(private val mail: MailMessage<*>) {

        abstract val self: T

        @MailDsl
        infix fun then(then: T): T = then

        @MailDsl
        infix fun from(from: String): T {
            mail.setFrom(from)
            if (mail.getReplyTo().isNullOrEmpty()) {
                mail.setReplyTo(from)
            }
            return self
        }

        @MailDsl
        infix fun reply(repl: String): T {
            mail.setReplyTo(repl)
            return self
        }

        @MailDsl
        fun to(list: Iterable<String>) {
            mail.addTo(list)
        }

        @MailDsl
        fun to(list: Sequence<String>) {
            mail.addTo(list)
        }

        @MailDsl
        fun to(vararg list: String) {
            mail.addTo(*list)
        }

        @MailDsl
        fun cc(list: Iterable<String>) {
            mail.addCc(list)
        }

        @MailDsl
        fun cc(list: Sequence<String>) {
            mail.addCc(list)
        }

        @MailDsl
        fun cc(vararg list: String) {
            mail.addCc(*list)
        }

        @MailDsl
        fun bcc(list: Iterable<String>) {
            mail.addBcc(list)
        }

        @MailDsl
        fun bcc(list: Sequence<String>) {
            mail.addBcc(list)
        }

        @MailDsl
        fun bcc(vararg list: String) {
            mail.addBcc(*list)
        }

        @MailDsl
        @JvmOverloads
        fun date(date: Date = Date()) {
            mail.setDate(date)
        }

        @MailDsl
        fun subject(subj: String) {
            mail.setSubject(subj)
        }
    }

    inner class TextMailMessageBuilder(private val text: TextMailMessage) : MailMessageBuilder<TextMailMessageBuilder>(text) {

        override val self: TextMailMessageBuilder
            get() = this

        @MailDsl
        fun body(data: CharSequence) {
            text.setBody(data.toString())
        }

        @MailDsl
        fun body(data: () -> String) {
            text.setBody(data.invoke())
        }

        @MailDsl
        @JvmOverloads
        fun body(data: ByteArray, charset: Charset = Charsets.UTF_8) {
            text.setBody(data.toString(charset))
        }

        @MailDsl
        @JvmOverloads
        fun body(data: URL, charset: Charset = Charsets.UTF_8) {
            body(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun body(data: URI, charset: Charset = Charsets.UTF_8) {
            body(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun body(data: File, charset: Charset = Charsets.UTF_8) {
            body(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun body(data: Path, charset: Charset = Charsets.UTF_8) {
            body(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun body(data: InputStream, charset: Charset = Charsets.UTF_8) {
            body(data.toByteArray(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun body(data: InputStreamSupplier, charset: Charset = Charsets.UTF_8) {
            body(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun body(data: ReadableByteChannel, charset: Charset = Charsets.UTF_8) {
            body(data.toInputStream(), charset)
        }
    }

    inner class MimeMailMessageBuilder(private val mime: MimeMailMessage) : MailMessageBuilder<MimeMailMessageBuilder>(mime) {

        override val self: MimeMailMessageBuilder
            get() = this

        @MailDsl
        fun body(block: MimeMailMessageBodyBuilder.() -> Unit) {
            MimeMailMessageBody().also {
                MimeMailMessageBodyBuilder(it).apply(block)
                mime.setBody(it)
            }
        }
    }

    inner class MimeMailMessageBodyBuilder(private val body: MimeMailMessageBody) {
        @MailDsl
        fun message(data: CharSequence) {
            body.setMessageBodyText(data.toString())
        }

        @MailDsl
        fun message(data: () -> String) {
            body.setMessageBodyText(data.invoke())
        }

        @MailDsl
        @JvmOverloads
        fun message(data: ByteArray, charset: Charset = Charsets.UTF_8) {
            body.setMessageBodyText(data.toString(charset))
        }

        @MailDsl
        @JvmOverloads
        fun message(data: URL, charset: Charset = Charsets.UTF_8) {
            message(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun message(data: URI, charset: Charset = Charsets.UTF_8) {
            message(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun message(data: File, charset: Charset = Charsets.UTF_8) {
            message(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun message(data: Path, charset: Charset = Charsets.UTF_8) {
            message(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun message(data: InputStream, charset: Charset = Charsets.UTF_8) {
            message(data.toByteArray(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun message(data: InputStreamSupplier, charset: Charset = Charsets.UTF_8) {
            message(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun message(data: ReadableByteChannel, charset: Charset = Charsets.UTF_8) {
            message(data.toInputStream(), charset)
        }

        @MailDsl
        fun html(data: CharSequence) {
            body.setMessageBodyHtml(data.toString())
        }

        @MailDsl
        fun html(data: () -> String) {
            body.setMessageBodyHtml(data.invoke())
        }

        @MailDsl
        @JvmOverloads
        fun html(data: ByteArray, charset: Charset = Charsets.UTF_8) {
            body.setMessageBodyHtml(data.toString(charset))
        }

        @MailDsl
        @JvmOverloads
        fun html(data: URL, charset: Charset = Charsets.UTF_8) {
            html(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun html(data: URI, charset: Charset = Charsets.UTF_8) {
            html(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun html(data: File, charset: Charset = Charsets.UTF_8) {
            html(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun html(data: Path, charset: Charset = Charsets.UTF_8) {
            html(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun html(data: InputStream, charset: Charset = Charsets.UTF_8) {
            html(data.toByteArray(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun html(data: InputStreamSupplier, charset: Charset = Charsets.UTF_8) {
            html(data.toInputStream(), charset)
        }

        @MailDsl
        @JvmOverloads
        fun inline(name: String, data: URL, type: String = DEFAULT_CONTENT_TYPE) {
            when (type.isDefaultContentType()) {
                true -> inline(name, URLContentResource(data, getContentTypeProbe().getContentType(name, type)))
                else -> inline(name, URLContentResource(data, type))
            }
        }

        @MailDsl
        fun inline(name: String, data: URI) {
            inline(name, data.toContentResource())
        }

        @MailDsl
        @JvmOverloads
        fun inline(name: String, data: Path, type: String = DEFAULT_CONTENT_TYPE) {
            when (type.isDefaultContentType()) {
                true -> inline(name, FileContentResource(data, getContentTypeProbe().getContentType(name, type)))
                else -> inline(name, FileContentResource(data, type))
            }
        }

        @MailDsl
        @JvmOverloads
        fun inline(name: String, data: File, type: String = DEFAULT_CONTENT_TYPE) {
            when (type.isDefaultContentType()) {
                true -> inline(name, FileContentResource(data, getContentTypeProbe().getContentType(name, type)))
                else -> inline(name, FileContentResource(data, type))
            }
        }

        @MailDsl
        @JvmOverloads
        fun inline(name: String, data: ByteArray, type: String = DEFAULT_CONTENT_TYPE) {
            when (type.isDefaultContentType()) {
                true -> inline(name, ByteArrayContentResource(data, getContentTypeProbe().getContentType(name, type)))
                else -> inline(name, ByteArrayContentResource(data, type))
            }
        }

        @MailDsl
        fun inline(name: String, make: ContentResourceLookup) {
            inline(name, make.invoke(name))
        }

        @MailDsl
        fun inline(name: String, data: ContentResource) {
            body.addInlinePart(name, data)
        }

        @MailDsl
        @JvmOverloads
        fun attach(name: String, data: URL, type: String = DEFAULT_CONTENT_TYPE) {
            when (type.isDefaultContentType()) {
                true -> attach(name, URLContentResource(data, getContentTypeProbe().getContentType(name, type)))
                else -> attach(name, URLContentResource(data, type))
            }
        }

        @MailDsl
        @JvmOverloads
        fun attach(name: String, data: URI, type: String = DEFAULT_CONTENT_TYPE) {
            when (type.isDefaultContentType()) {
                true -> attach(name, URLContentResource(data, getContentTypeProbe().getContentType(name, type)))
                else -> attach(name, URLContentResource(data, type))
            }
        }

        @MailDsl
        @JvmOverloads
        fun attach(name: String, data: Path, type: String = DEFAULT_CONTENT_TYPE) {
            when (type.isDefaultContentType()) {
                true -> attach(name, FileContentResource(data, getContentTypeProbe().getContentType(name, type)))
                else -> attach(name, FileContentResource(data, type))
            }
        }

        @MailDsl
        @JvmOverloads
        fun attach(name: String, data: File, type: String = DEFAULT_CONTENT_TYPE) {
            when (type.isDefaultContentType()) {
                true -> attach(name, FileContentResource(data, getContentTypeProbe().getContentType(name, type)))
                else -> attach(name, FileContentResource(data, type))
            }
        }

        @MailDsl
        @JvmOverloads
        fun attach(name: String, data: ByteArray, type: String = DEFAULT_CONTENT_TYPE) {
            when (type.isDefaultContentType()) {
                true -> attach(name, ByteArrayContentResource(data, getContentTypeProbe().getContentType(name, type)))
                else -> attach(name, ByteArrayContentResource(data, type))
            }
        }

        @MailDsl
        fun attach(name: String, make: ContentResourceLookup) {
            attach(name, make.invoke(name))
        }

        @MailDsl
        fun attach(name: String, data: ContentResource) {
            body.addAttachPart(name, data)
        }
    }

    companion object {

        private val NONE = emptyArray<InternetAddress>()

        @JvmStatic
        fun parse(address: String?): String? = address(address)?.address

        @JvmStatic
        fun address(address: String?): InternetAddress? {
            val text = toTrimOrNull(address) ?: return null
            return try {
                InternetAddress(text, true)
            }
            catch (cause: Throwable) {
                null
            }
        }

        @JvmStatic
        fun addresses(addresses: Sequence<String>): Array<InternetAddress> {
            val data = addresses.toSet()
            return when (data.isEmpty()) {
                true -> NONE.copyOf()
                else -> addresses(data)
            }
        }

        @JvmStatic
        fun addresses(vararg addresses: String): Array<InternetAddress> {
            return when (addresses.isEmpty()) {
                true -> NONE.copyOf()
                else -> addresses(addresses.toSet())
            }
        }

        @JvmStatic
        fun addresses(addresses: Iterable<String>): Array<InternetAddress> {
            val data = addresses.toSet()
            return when (data.isEmpty()) {
                true -> NONE.copyOf()
                else -> addresses(data)
            }
        }

        @JvmStatic
        fun addresses(addresses: Set<String>): Array<InternetAddress> {
            return when (addresses.isEmpty()) {
                true -> NONE.copyOf()
                else -> addresses.mapNotNull { address(it) }.distinct().toTypedArray()
            }
        }
    }
}