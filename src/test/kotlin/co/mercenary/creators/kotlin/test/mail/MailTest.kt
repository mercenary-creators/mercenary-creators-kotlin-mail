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

package co.mercenary.creators.kotlin.test.mail

import co.mercenary.creators.kotlin.mail.*
import co.mercenary.creators.kotlin.util.*
import org.junit.jupiter.api.Test

class MailTest : AbstractKotlinMailTest() {
    @Test
    fun test() {
        val many = getMailMessageRepeat()
        val send = getMailMessageSender()
        val mail = Mail {
            repeat(many) {
                mime {
                    from("deansjones@gmail.com")
                    reply("deansjones@gmail.com")
                    subject("Subject: Mime Message Attach HTML $it")
                    cc("deansjones@outlook.com")
                    to(listOf("deansjones@me.com", "deansjones@gmail.com"))
                    body {
                        html(contentResourceLoader["dean.htm"])
                        attach("test.pdf", contentResourceLoader["test.pdf"])
                        inline("dune.jpg", contentResourceLoader["dune.jpg"])
                    }
                }
            }
        }
        val list = timed {
            mail.send(send)
        }
        info { list.size }
        list.size shouldBe many
        val size = list.filter { it.isValid().not() }.size
        info { size }
    }
}