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

import co.mercenary.creators.kotlin.util.type.*
import javax.mail.*
import javax.mail.internet.MimeMessage

class JavaMimeMessage(session: Session) : MimeMessage(session){

    val saved: Boolean
        get() = super.saved

    val everyone: Array<Address>
        get() = recipients()

    fun send(call: Transport): Boolean {
        val list = everyone
        if (list.isNotEmpty()) {
            if (call.isConnected) {
                call.sendMessage(this, list)
                return true
            }
        }
        return false
    }

    fun recipients(): Array<Address> {
        val to = this.getRecipients(Message.RecipientType.TO) ?: emptyArray()
        val cc = this.getRecipients(Message.RecipientType.CC) ?: emptyArray()
        val bc = this.getRecipients(Message.RecipientType.BCC) ?: emptyArray()
        return (to + cc + bc).distinct().filterNotNull().distinct().toTypedArray()
    }

    override fun createMimeMessage(session: Session): JavaMimeMessage {
        return JavaMimeMessage(session)
    }
}