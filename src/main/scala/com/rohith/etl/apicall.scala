package com.rohith.etl

import cats._
import cats.effect._
import cats.implicits._
import io.circe._, io.circe.parser._, io.circe.generic.semiauto._
import org.http4s.EntityDecoder
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.Uri
import scala.concurrent.ExecutionContext.global
//import org.http4s.circe._
//import org.http4s._
//import org.http4s.circe.jsonOf
// import scala.io.Source
//import ObjectStoreWriter._
// import scala.util.{Failure, Success, Try}

object Apicall extends IOApp {

    import Schema._
    import BuildUri._
    import ObjectStoreWriter._
    import LocalWriter._

    // sample method to parse the data and check

    def getContentShortOD(j: String): IO[Option[OverallData]] = parser.decode(j)(overalldataDecoder) match {
        case Right(v) => IO(Some(v))
        case Left(e) => 
          IO(None)
    }
    
    def run(args: List[String]): IO[ExitCode] = {

        val program: IO[Unit] = BlazeClientBuilder[IO](global).resource.use {client =>
            for {
                    uri       <- buildUri
                    jsondata  <- client.expect[OverallData](uri)                    
                    csvdata   <- toCsvFormat(jsondata)
                    // _         <- IO(println(csvdata))
                    // _         <- writeString(csvdata, "report-for-", "2020-11-06")
                    _       <- writeToLocal(csvdata)
            } yield ()
        }

        IO(program.unsafeRunSync).as(ExitCode.Success)
    }
}