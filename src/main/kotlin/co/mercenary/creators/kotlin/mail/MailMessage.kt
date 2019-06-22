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

import co.mercenary.creators.kotlin.util.core.CoreValidated
import java.util.*

interface MailMessage<BODY> : CoreValidated {
    fun addTo(list: List<String>)
    fun setTo(list: List<String>)
    fun getTo(): List<String>
    fun addCc(list: List<String>)
    fun setCc(list: List<String>)
    fun getCc(): List<String>
    fun addBcc(list: List<String>)
    fun setBcc(list: List<String>)
    fun getBcc(): List<String>
    fun getDate(): Date?
    fun setDate(date: Date)
    fun getFrom(): String?
    fun setFrom(from: String)
    fun getReplyTo(): String?
    fun setReplyTo(repl: String)
    fun getSubject(): String?
    fun setSubject(subj: String)
    fun setBody(body: BODY)
    fun getBody(): BODY?
}