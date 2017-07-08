package org.http4s
package multipart

import cats.Eq
import cats.implicits._
import org.http4s.headers._
import org.http4s._

final case class Multipart(parts: Vector[Part], boundary: Boundary = Boundary.create) {
  def headers: Headers = Headers(`Content-Type`(MediaType.multipart("form-data", Some(boundary.value))))
}

object Multipart {

  implicit val multipartEq : Eq[Multipart] = Eq.instance{ (a, b) =>
    a.headers === b.headers &&
    a.boundary === b.boundary &&
    a.parts === b.parts
  }

}




