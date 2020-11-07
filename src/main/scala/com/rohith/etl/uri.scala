package com.rohith.etl

import cats.effect._
import org.http4s.Uri
// import scala.util.{Failure, Success, Try}
// import cats.implicits._
// import retry._
// import retry.CatsEffect._
// import org.http4s.client.Client
// import org.http4s.client.dsl.io._
// import org.http4s.Method._
// import org.http4s._

object BuildUri {

   def buildUri: IO[Uri] = IO {
        
        Uri.uri("https://gorest.co.in/public-api/posts")
   }
}