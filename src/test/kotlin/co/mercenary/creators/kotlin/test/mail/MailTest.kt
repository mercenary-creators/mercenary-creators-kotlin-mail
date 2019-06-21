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

import co.mercenary.creators.kotlin.io.data.ClassPathContentResource
import co.mercenary.creators.kotlin.mail.Mail
import co.mercenary.creators.kotlin.test.util.AbstractKotlinMailTest
import org.junit.jupiter.api.Test

class MailTest : AbstractKotlinMailTest() {
    @Test
    fun text() {
        val mail = Mail {
            repeat(7) { index ->
                text {
                    from("deansjones@gmail.com")
                    subject("Subject: Text Message $index")
                    to(listOf("deansjones@me.com", "deansjones@gmail.com"))
                    body("Testing $index \n")
                }
            }
            mime {
                from("deansjones@gmail.com")
                reply("deansjones@gmail.com")
                subject("Subject: Mime Message Attach HTML")
                to(listOf("deansjones@me.com", "deansjones@gmail.com"))
                body {
                    html(ClassPathContentResource("dean.html"))
                    attach("test.pdf", ClassPathContentResource("test.pdf"))
                    attach("dune.jpg", ClassPathContentResource("dune.jpg"))
                }
            }
        }
        mail.send(getMailMessageSender())
    }
}