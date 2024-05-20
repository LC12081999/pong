package ch.hevs.gdx2d.hello

import java.io._
import java.net._
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.io.BufferedSource

class SimpleSocketServer(port: Int)(implicit ec: ExecutionContext) {

  val clients = mutable.ListBuffer[PrintStream]()
  private var serverSocket: ServerSocket = _

  def start(): Unit = {
    serverSocket = new ServerSocket(port)
    println(s"Server started on port $port")

    Future {
      while (true) {
        val clientSocket = serverSocket.accept()
        println("Client connected")

        val clientOutput = new PrintStream(clientSocket.getOutputStream)
        clients.synchronized {
          clients += clientOutput
        }

        Future {
          handleClient(clientSocket)
        }
      }
    }
  }

  def stop(): Unit = {
    if (serverSocket != null && !serverSocket.isClosed) {
      serverSocket.close()
    }
    clients.synchronized {
      clients.foreach(_.close())
      clients.clear()
    }
    println("Server stopped")
  }

  private def handleClient(socket: Socket): Unit = {
    val in = new BufferedSource(socket.getInputStream).getLines()
    val out = new PrintStream(socket.getOutputStream)

    try {
      while (true) {
        if (in.hasNext) {
          val line = in.next()
          if (line == "exit") {
            out.println("Goodbye!")
            out.flush()
            return
          }
          println(s"Received: $line")
          broadcast(line)
        }
      }
    } catch {
      case e: Exception => println(s"Error: ${e.getMessage}")
    } finally {
      socket.close()
      clients.synchronized {
        clients -= out
      }
      println("Client disconnected")
    }
  }

  private def broadcast(message: String): Unit = {
    clients.synchronized {
      clients.foreach { out =>
        out.println(message)
        out.flush()
      }
    }
  }
}
