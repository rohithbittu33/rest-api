package com.rohith.etl

import cats._
import cats.effect._
import cats.implicits._
import io.circe._, io.circe.parser._, io.circe.generic.semiauto._
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf
import scala.io.Source
import scala.reflect.runtime.universe._
// import org.http4s.client.blaze.BlazeClientBuilder
// import org.http4s.client.Client
// import org.http4s.Uri
//import org.http4s._
//import org.http4s.circe._
// import scala.util.{Failure, Success, Try}
// import scala.concurrent.ExecutionContext.global
// import org.http4s.circe._


object Schema{

    case class Data(
        id: Long,
        user_id: Long,
        title: String,
        body: String,
        created_at: String,
        updated_at: String
    )

    case class Meta(pagination: Pagination)

    case class Pagination(pagination: Map[String, Long])

    case class OverallData(code: Long, meta: Meta, data: List[Data])

    implicit val metaDecoder: Decoder[Meta] = deriveDecoder[Meta]
    implicit val paginationDecoder: Decoder[Pagination] = Decoder[Map[String, Long]].map(x => Pagination(x.toMap))
    implicit val dataDecoder: Decoder[Data] = deriveDecoder[Data]
    implicit val overalldataDecoder: Decoder[OverallData] = deriveDecoder[OverallData]

    implicit val overalldataEntityDecoder: EntityDecoder[IO, OverallData] = jsonOf[IO, OverallData]

    def classAccessors[T: TypeTag]: List[MethodSymbol] = typeOf[T].members.collect {
        case m: MethodSymbol if m.isCaseAccessor => m
      }.toList

    private val dataHeader = classAccessors[Data].map(x => x.name.toString).reverse.mkString(",") + "\n"

    implicit class CSVWrapper(val prod: Product) extends AnyVal {
        def toCSV() = prod.productIterator.map{
                        case Some(value) => s""""$value""""
                        case None => ""
                        case rest => s""""$rest""""
                      }.mkString(",")
    }

    def toCsvFormat(a: OverallData): IO[String] = IO(
        (dataHeader :: a.data.map(_.toCSV() + "\n")).mkString
    )
    
}