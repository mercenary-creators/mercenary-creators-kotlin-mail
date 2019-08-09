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

abstract class AbstractConfigurableMailMessageSender : ConfigurableMailMessageSender {

    private var hostport = IS_NOT_FOUND

    private var confdata = Properties()

    private var hostname: String? = null

    private var username: String? = null

    private var password: String? = null

    override fun getHostPort() = hostport

    override fun setHostPort(hostport: Int) {
        this.hostport = hostport
    }

    override fun getHostName() = hostname

    override fun setHostName(hostname: String?) {
        this.hostname = hostname
    }

    override fun getUserName() = username

    override fun setUserName(username: String?) {
        this.username = username
    }

    override fun getPassword() = password

    override fun setPassword(password: String?) {
        this.password = password
    }

    override fun getConfiguration() = confdata

    override fun setConfiguration(conf: Properties) {
        this.confdata = conf
    }
}