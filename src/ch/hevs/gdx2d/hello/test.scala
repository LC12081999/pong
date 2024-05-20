package ch.hevs.gdx2d.hello

import scala.concurrent.ExecutionContext.Implicits.global

object test extends App {
  val test1 = new SimpleSocketServer(9999)
  test1.start()
  val test2 = new SimpleSocketClient("localhost", 9999)
}
