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
import kotlin.math.*

abstract class AbstractKotlinMailTest : AbstractKotlinTest() {

    override fun getConfigPropertiesBuilder(): () -> Properties = {
        Properties().also { prop ->
            RESOURCE_LOADER["file:/opt/development/properties/mercenary-creators-core/mail-test.properties"].cache().toInputStream().use { prop.load(it) }
        }
    }

    fun getMailMessageRepeat(many: Int = 8): Int = abs(min(getConfigProperty("co.mercenary.creators.core.test.mail.many").toIntOrNull().orElse { abs(many) }, 64))

    protected open fun getMailMessageSender(): MailMessageSenderBuilder = MailMessageSenderBuilder {
        port(587)
        host("smtp.gmail.com")
        username(getConfigProperty("co.mercenary.creators.core.test.mail.user"))
        password(getConfigProperty("co.mercenary.creators.core.test.mail.pass"))
        configuration("mail.smtp.auth" to true, "mail.smtp.starttls.enable" to true)
    }
}