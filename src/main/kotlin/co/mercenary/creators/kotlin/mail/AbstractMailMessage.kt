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

import co.mercenary.creators.kotlin.util.*
import java.util.*

abstract class AbstractMailMessage<BODY> : MailMessage<BODY> {

    private var body: BODY? = null

    private var date: Date? = null

    private var from: String? = null

    private var repl: String? = null

    private var subj: String? = null

    private val to = mutableSetOf<String>()

    private val cc = mutableSetOf<String>()

    private val bc = mutableSetOf<String>()

    override fun getBody(): BODY? = body

    override fun setBody(body: BODY) {
        this.body = body
    }

    override fun getFrom() = from

    override fun setFrom(from: String) {
        this.from = Mail.parse(from)
    }

    override fun getSubject() = subj.orElse { "Subject:" }

    override fun setSubject(subj: String) {
        this.subj = subj
    }

    override fun getReplyTo() = repl

    override fun setReplyTo(repl: String) {
        this.repl = Mail.parse(repl)
    }

    override fun setTo(list: Iterable<String>) {
        to.clear()
        addTo(list)
    }

    override fun addTo(list: Iterable<String>) {
        append(to, list)
    }

    override fun getTo() = to.distinct()

    override fun setCc(list: Iterable<String>) {
        cc.clear()
        addCc(list)
    }

    override fun addCc(list: Iterable<String>) {
        append(cc, list)
    }

    override fun getCc() = cc.distinct()

    override fun setBcc(list: Iterable<String>) {
        bc.clear()
        addBcc(list)
    }

    override fun addBcc(list: Iterable<String>) {
        append(bc, list)
    }

    override fun getBcc() = bc.distinct()

    override fun setDate(date: Date) {
        this.date = date.copyOf()
    }

    override fun getDate() = date

    override fun isValid(): Boolean {
        return (Mail.parse(getFrom()) != null) && (getTo().isNotEmpty()) && isValid(getBody())
    }

    private fun append(send: MutableSet<String>, list: Iterable<String>) {
        send += list.mapNotNull { Mail.parse(it) }.distinct()
    }
}