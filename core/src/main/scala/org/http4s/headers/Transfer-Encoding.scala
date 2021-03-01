/*
 * Copyright 2013 http4s.org
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

package org.http4s
package headers

import cats.data.NonEmptyList
import cats.parse.Parser
import cats.syntax.all._
import org.http4s.internal.parsing.Rfc7230
import org.http4s.v2.Header
import org.typelevel.ci.CIString

object `Transfer-Encoding` {

  def apply(head: TransferCoding, tail: TransferCoding*): `Transfer-Encoding` =
    apply(NonEmptyList(head, tail.toList))

  private[http4s] val parser: Parser[`Transfer-Encoding`] =
    Rfc7230.headerRep1(TransferCoding.parser).map(apply)

  implicit val headerInstance: Header[`Transfer-Encoding`, Header.Recurring] =
    Header.createRendered(
      CIString("Transfer-Encoding"),
      _.values,
      ParseResult.fromParser(parser, "Invalid Transfer-Encoding header")
    )

  implicit val headerSemigroupInstance: cats.Semigroup[`Transfer-Encoding`] =
    (a, b) => `Transfer-Encoding`(a.values.concatNel(b.values))

  def name = headerInstance.name
}

final case class `Transfer-Encoding`(values: NonEmptyList[TransferCoding]) {
  def hasChunked: Boolean = values.exists(_ === TransferCoding.chunked)

  def value: String = Header[`Transfer-Encoding`].value(this)
}
