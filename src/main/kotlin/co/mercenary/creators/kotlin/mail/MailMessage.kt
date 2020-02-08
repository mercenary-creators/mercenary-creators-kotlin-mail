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

import co.mercenary.creators.kotlin.util.type.Validated
import java.util.*

interface MailMessage<BODY> : Validated {
    fun addTo(list: Iterable<String>)
    fun addTo(list: Sequence<String>) {
        addTo(list.toSet())
    }

    fun addTo(vararg list: String) {
        if (list.isNotEmpty()) {
            addTo(list.toSet())
        }
    }

    fun setTo(list: Iterable<String>)
    fun setTo(list: Sequence<String>) {
        setTo(list.toSet())
    }

    fun setTo(vararg list: String) {
        setTo(list.toSet())
    }

    fun getTo(): List<String>
    fun addCc(list: Iterable<String>)
    fun addCc(list: Sequence<String>) {
        addCc(list.toSet())
    }

    fun addCc(vararg list: String) {
        if (list.isNotEmpty()) {
            addCc(list.toSet())
        }
    }

    fun setCc(list: Iterable<String>)
    fun setCc(list: Sequence<String>) {
        setCc(list.toSet())
    }

    fun setCc(vararg list: String) {
        setCc(list.toSet())
    }

    fun getCc(): List<String>
    fun addBcc(list: Iterable<String>)
    fun addBcc(list: Sequence<String>) {
        addBcc(list.toSet())
    }

    fun addBcc(vararg list: String) {
        if (list.isNotEmpty()) {
            addBcc(list.toSet())
        }
    }

    fun setBcc(list: Iterable<String>)
    fun setBcc(list: Sequence<String>) {
        setBcc(list.toSet())
    }

    fun setBcc(vararg list: String) {
        setBcc(list.toSet())
    }

    fun getBcc(): List<String>
    fun getDate(): Date?
    fun setDate(date: Date)
    fun getFrom(): String?
    fun setFrom(from: String)
    fun getReplyTo(): String?
    fun setReplyTo(repl: String)
    fun getSubject(): String
    fun setSubject(subj: String)
    fun setBody(body: BODY)
    fun getBody(): BODY?
    fun getDescription(): String
}