package ch.hevs.gdx2d.hello

import java.io._
import java.net._
import scala.io.BufferedSource
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

class SimpleSocketClient(host: String, port: Int) {
  @volatile private var running = true

  private val socket = new Socket(host, port)
  private val in = new BufferedSource(socket.getInputStream).getLines()
  private val out = new PrintStream(socket.getOutputStream)
  var toGet: String = ""
  var getCheck: Boolean = false

  def start(): Unit = {
    Future {
      try {
        while (running && in.hasNext) {
          val response = in.next()
          synchronized {
            toGet = response
            getCheck = true
          }
          println(s"Server: $response")
        }
      } catch {
        case e: Exception => println(s"Error: ${e.getMessage}")
      } finally {
        stop()
      }
    }
  }

  def send(message: String): Unit = {
    out.println(message)
    out.flush()
  }

  def stop(): Unit = {
    running = false
    out.close()
    socket.close()
  }
}
