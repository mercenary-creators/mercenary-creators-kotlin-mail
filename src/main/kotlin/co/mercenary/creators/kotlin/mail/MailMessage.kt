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

import co.mercenary.creators.kotlin.util.type.Validated
import java.util.*

interface MailMessage<BODY> : Validated {
    fun addTo(list: Iterable<String>)
    fun addTo(vararg list: String) {
        if (list.isNotEmpty()) {
            addTo(setOf(*list))
        }
    }
    fun setTo(list: Iterable<String>)
    fun setTo(vararg list: String) {
        setTo(setOf(*list))
    }
    fun getTo(): List<String>
    fun addCc(list: Iterable<String>)
    fun addCc(vararg list: String) {
        if (list.isNotEmpty()) {
            addCc(setOf(*list))
        }
    }
    fun setCc(list: Iterable<String>)
    fun setCc(vararg list: String) {
        setCc(setOf(*list))
    }
    fun getCc(): List<String>
    fun addBcc(list: Iterable<String>)
    fun addBcc(vararg list: String) {
        if (list.isNotEmpty()) {
            addBcc(setOf(*list))
        }
    }
    fun setBcc(list: Iterable<String>)
    fun setBcc(vararg list: String) {
        setBcc(setOf(*list))
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
}