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

import co.mercenary.creators.kotlin.DefaultMailMessageSender
import co.mercenary.creators.kotlin.util.type.Builder
import java.io.*
import java.net.URL
import java.util.*

class MailMessageSenderBuilder(private val sender: ConfigurableMailMessageSender = DefaultMailMessageSender(), block: MailMessageSenderBuilder.() -> Unit) : Builder<ConfigurableMailMessageSender> {

    init {
        block(this)
    }

    fun port(hostport: Int) {
        sender.setHostPort(hostport)
    }

    fun host(hostname: String?) {
        sender.setHostName(hostname)
    }

    fun username(username: String?) {
        sender.setUserName(username)
    }

    fun password(password: String?) {
        sender.setPassword(password)
    }

    fun configuration(data: Properties) {
        sender.setConfiguration(data)
    }

    fun configuration(data: URL) {
        sender.setConfiguration(data)
    }

    fun configuration(data: Reader) {
        sender.setConfiguration(data)
    }

    fun configuration(data: InputStream) {
        sender.setConfiguration(data)
    }

    fun configuration(data: Map<String, Any>) {
        sender.setConfiguration(data)
    }

    fun configuration(vararg data: Pair<String, Any>) {
        sender.setConfiguration(*data)
    }

    override fun build() = sender
}