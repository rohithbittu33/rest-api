package com.rohith.etl

import cats._
import cats.effect._
import cats.implicits._
import java.io.File
import java.io.PrintWriter 

object LocalWriter{

    def writeToLocal(csv: String): IO[Unit] = IO{
        val writer = new PrintWriter(new File("/users/rmadamshetty/Documents/restAPIdata.csv"))
        writer.write(csv)
        writer.close()
    } 
}