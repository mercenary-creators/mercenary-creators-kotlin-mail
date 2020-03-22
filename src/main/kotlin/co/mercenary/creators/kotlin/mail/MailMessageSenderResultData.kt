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

import co.mercenary.creators.kotlin.util.*
import java.util.*

 data class MailMessageSenderResultData @JvmOverloads constructor(private val id: String? = "<UNKNOWN>", private val date: Date? = Date(), private val good: Boolean = false, private val message: String = EMPTY_STRING) : MailMessageSenderResult {
    private val text = id ?: "<UNKNOWN>"
    override fun getId() = text
    @MailDsl
    override fun isValid() = good
    override fun getDate() = date.copyOf()
    override fun getMessage() = message
}