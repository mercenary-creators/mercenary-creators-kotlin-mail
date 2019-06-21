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

import co.mercenary.creators.kotlin.io.data.ContentResource

class MimeMailMessageBody : Validated {

    private var body: String? = null

    private var html: String? = null

    private val attach = LinkedHashMap<String, ContentResource>()

    private val inline = LinkedHashMap<String, ContentResource>()

    fun getMessageBodyText(): String? = body

    fun setMessageBodyText(body: String) {
        this.body = body
    }

    fun getMessageBodyHtml(): String? = html

    fun setMessageBodyHtml(html: String) {
        this.html = html
    }

    fun getInlineParts(): Map<String, ContentResource> = inline

    fun addInlinePart(name: String, part: ContentResource) {
        inline[name] = part
    }

    fun getAttachParts(): Map<String, ContentResource> = attach

    fun addAttachPart(name: String, part: ContentResource) {
        attach[name] = part
    }

    fun isEmpty(): Boolean = inline.isEmpty() && attach.isEmpty()

    override fun isValid(): Boolean {
        return (getMessageBodyText() != null) || (getMessageBodyHtml() != null)
    }
}