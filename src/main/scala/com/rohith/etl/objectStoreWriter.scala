package com.rohith.etl

import scala.util.{Failure, Success, Try}
import cats.effect._
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{PutObjectRequest, S3Object}
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import java.nio.ByteBuffer
import java.util.{Date, TimeZone}
import java.text.SimpleDateFormat
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPOutputStream
import org.log4s.Logger

object ObjectStoreWriter{

    val env = System.getenv

    def s3Client: Option[S3Client] = {
        Try(S3Client.builder
        .credentialsProvider(DefaultCredentialsProvider.builder.build)
        .build) match {
            case Success(conn) => Some(conn)
            case Failure(ec2Error) => None
            }
    }

    def byteArrayToByteBuffer(bs: Array[Byte]): ByteBuffer = {
        val data = ByteBuffer.allocate(bs.length)
        data.put(bs).flip()
        data
   }

    private def compress(input: Array[Byte]): Array[Byte] = {
        val bos = new ByteArrayOutputStream(input.length)
        val gzip = new GZIPOutputStream(bos)
        gzip.write(input)
        gzip.close()
        val compressed = bos.toByteArray
        bos.close()
        compressed
    }

    def write(d: String, keyBase: String, date: String): IO[Unit] = IO {     
        val bucket = "as-out-dev"
        val key = s"rohithWS/json-test/dt=${date}/${keyBase}_${date}.csv.gz"

        val s3:S3Client = s3Client.get

        val data = byteArrayToByteBuffer(compress(d.getBytes("UTF-8")))
        
        s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), 
            RequestBody.fromByteBuffer(data))
   }

   def write(d: Array[Byte], keyBase: String, date: String): IO[Unit] = IO {
        val bucket = "as-out-dev"
        val key = s"rohithWS/json-test/dt=${date}/${keyBase}_${date}.csv.gz"

        val s3:S3Client = s3Client.get
        val data = byteArrayToByteBuffer(d)
        s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), 
            RequestBody.fromByteBuffer(data))
   }

    def writeString(d: String, keyBase: String, date: String): IO[Unit] = IO {
        val bucket = "as-out-dev"
        val key = s"rohithWS/json-test/dt=${date}/${keyBase}_${date}.csv.gz"

        val s3:S3Client = s3Client.get

        val data = byteArrayToByteBuffer(compress(d.getBytes("UTF-8")))

        s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(),
            RequestBody.fromByteBuffer(data))
   }
}
