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

import reactor.core.publisher.Flux
import java.util.stream.Collectors

typealias ParallelScheduler = co.mercenary.creators.kotlin.reactor.ParallelScheduler

typealias DefaultMailMessageSender = co.mercenary.creators.kotlin.mail.javamail.JavaMailMessageSender

internal fun <T : Any> Iterable<T>.toFlux(): Flux<T> = Flux.fromIterable(this)

internal fun <T : Any> Flux<T>.toList(): List<T> = collect(Collectors.toList<T>()).block()!!





