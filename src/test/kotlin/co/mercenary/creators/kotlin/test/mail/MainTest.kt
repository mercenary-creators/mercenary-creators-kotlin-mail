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

import co.mercenary.creators.kotlin.io.data.*
import co.mercenary.creators.kotlin.test.util.AbstractKotlinTest
import org.junit.jupiter.api.Test

class MainTest : AbstractKotlinTest() {
    @Test
    fun text() {
        val name = "dune.jpg"
        val dune = ClassPathContentResource(name)
        info { dune }
        info { dune.getContentType() }
        info { dune.getContentTime() }
        info { dune.getContentSize() }
        dune.isContentCache().shouldBe(false) {
            name
        }
        info { IO.getPathExtension(name) }
        val data = HEX.encodeer(name)
        info { data }
        val text = HEX.decodeer(data)
        info { text }
        name.shouldBe(text) {
            name
        }
    }
}