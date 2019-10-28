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

import co.mercenary.creators.kotlin.util.DefaultContentResourceLoader
import co.mercenary.creators.kotlin.util.io.CachedContentResource
import java.util.concurrent.ConcurrentHashMap

open class CachedContentResourceLoader : DefaultContentResourceLoader() {

    protected val cached = ConcurrentHashMap<String, CachedContentResource>()

    override operator fun get(path: String): CachedContentResource {
        return cached.computeIfAbsent(path) {
            super.get(it).toContentCache()
        }
    }
}