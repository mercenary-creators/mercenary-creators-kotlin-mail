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

import co.mercenary.creators.kotlin.util.io.InputStreamSupplier
import co.mercenary.creators.kotlin.util.toInputStream
import java.io.*
import java.net.URL
import java.nio.file.Path
import java.util.*

interface ConfigurableMailMessageSender : MailMessageSender {
    fun getHostPort(): Int
    fun setHostPort(hostport: Int)
    fun getHostName(): String?
    fun setHostName(hostname: String?)
    fun getUserName(): String?
    fun setUserName(username: String?)
    fun getPassword(): String?
    fun setPassword(password: String?)
    fun getParallelism(size: Int): Int
    fun getMaxParallel(): Int
    fun setMaxParallel(size: Int)
    fun setMaxParallel(calc: () -> Int)
    fun getMinParallel(): Int
    fun setMinParallel(size: Int)
    fun setMinParallel(calc: () -> Int)
    fun getConfiguration(): Properties
    fun setConfiguration(data: URL) {
        setConfiguration(data.toInputStream())
    }

    fun setConfiguration(data: File) {
        setConfiguration(data.toInputStream())
    }

    fun setConfiguration(data: Path) {
        setConfiguration(data.toInputStream())
    }

    fun setConfiguration(data: Reader) {
        setConfiguration(Properties(getDefaultConfiguration()).also { conf ->
            data.use { conf.load(it) }
        })
    }

    fun setConfiguration(data: InputStream) {
        setConfiguration(Properties(getDefaultConfiguration()).also { conf ->
            data.use { conf.load(it) }
        })
    }

    fun setConfiguration(data: InputStreamSupplier) {
        setConfiguration(data.getInputStream())
    }

    fun setConfiguration(data: Map<String, Any>) {
        setConfiguration(Properties(getDefaultConfiguration()).also { conf ->
            for ((k, v) in data) {
                conf[k] = v.toString()
            }
        })
    }

    fun setConfiguration(vararg data: Pair<String, Any>) {
        setConfiguration(mapOf(*data))
    }

    fun setConfiguration(conf: Properties)

    fun getDefaultConfiguration() = Properties()
}