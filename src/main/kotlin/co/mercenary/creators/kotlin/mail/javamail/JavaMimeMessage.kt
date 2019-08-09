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

import javax.mail.*
import javax.mail.internet.MimeMessage

open class JavaMimeMessage : MimeMessage {
    constructor(session: Session) : super(session)
    constructor(other: MimeMessage) : super(other)

    fun isSaved(): Boolean {
        return super.saved
    }

    fun send(call: Transport) {
        call.sendMessage(this, getEveryone())
    }

    fun getEveryone(): Array<Address> {
        return allRecipients ?: emptyArray()
    }

    override fun createMimeMessage(session: Session): JavaMimeMessage {
        return JavaMimeMessage(session)
    }
}