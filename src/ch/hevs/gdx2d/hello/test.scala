package ch.hevs.gdx2d.hello

import scala.concurrent.ExecutionContext.Implicits.global

object test extends App {
  val server = new SimpleSocketServer(9999)
  server.start()

  val client1 = new SimpleSocketClient("localhost", 9999)
  client1.start()

  val client2 = new SimpleSocketClient("localhost", 9999)
  client2.start()

  Thread.sleep(1000) // Attendre que les connexions soient établies

  client1.send("Hello from client 1")
  client2.send("Hello from client 2")

  Thread.sleep(1000) // Attendre la réception des messages

  client1.stop()
  client2.stop()
  server.stop()
}
