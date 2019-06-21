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

@file:kotlin.jvm.JvmName("MailKt")

package co.mercenary.creators.kotlin

import co.mercenary.creators.kotlin.mail.Validated
import reactor.core.publisher.*
import java.util.*
import java.util.stream.Collectors

typealias ParallelScheduler = co.mercenary.creators.kotlin.reactor.ParallelScheduler

typealias DefaultMailMessageSender = co.mercenary.creators.kotlin.mail.javamail.JavaMailMessageSender

fun isValid(value: Any?): Boolean = when (value) {
    null -> false
    is Validated -> value.isValid()
    else -> true
}

fun Date?.copy(): Date = when (this) {
    null -> Date()
    else -> Date(time)
}

fun uuid() = UUID.randomUUID().toString()

fun getProcessors(): Int = Runtime.getRuntime().availableProcessors()

fun <T : Any> Mono<T>.blocks(): T = block()!!

fun <T : Any> Iterable<T>.toFlux(): Flux<T> = Flux.fromIterable(this)

fun <T : Any> Flux<T>.toList(): List<T> = collect(Collectors.toList<T>()).blocks()





