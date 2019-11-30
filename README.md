Mercenary Creators Kotlin Mail
====
Provides a fluent Kotlin DSL for defining SMTP servers and constructing email messages in plain text or MIME/HTML, with Attachments and Inline Attachments. Uses reactive schedulers for high performance.

Downloading artifacts

SNAPSHOTS:

Maven:
```xml
<dependency>
  <groupId>co.mercenary-creators</groupId>
  <artifactId>mercenary-creators-kotlin-mail</artifactId>
  <version>9.1.1-SNAPSHOT</version>
</dependency>
```
Gradle:
```
dependencies {
    compile(group: 'co.mercenary-creators', name: 'mercenary-creators-kotlin-mail', version: '9.1.1-SNAPSHOT')
}
```

```kotlin
	val smtp = MailMessageSenderBuilder {
            port(587)
            host("smtp.gmail.com")
            username(getConfigProperty("co.mercenary.creators.core.test.mail.user"))
            password(getConfigProperty("co.mercenary.creators.core.test.mail.pass"))
            configuration("mail.smtp.auth" to true, "mail.smtp.starttls.enable" to true)
        }
        val mail = Mail {
            repeat(8) { index ->
                mime {
                    from("deansjones@gmail.com")
                    reply("deansjones@gmail.com")
                    subject("Subject: Mime Message Attach HTML $index")
                    to(listOf("deansjones@me.com", "deansjones@gmail.com"))
                    body {
                        html(getClassPathResource("dean.html"))
                        attach("test.pdf", getClassPathResource("test.pdf"))
                        inline("dune.jpg", getClassPathResource("dune.jpg"))
                    }
                }
            }
        }
        val list = mail.send(smtp)
```
License:

Copyright (c) 2019, Mercenary Creators Company. All rights reserved.

Mercenary Creators Kotlin Mail is released under version 2.0 of the Apache License.

http://www.apache.org/licenses/LICENSE-2.0.html

Author(s):

Dean S. Jones
deansjones@gmail.com
