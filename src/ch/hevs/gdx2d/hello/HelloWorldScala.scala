package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.{Gdx, Input, InputProcessor}
import ch.hevs.gdx2d.desktop.PortableApplication

object HelloWorldScala {
  def main(args: Array[String]): Unit = {
    new HelloWorldScala
  }
}

class HelloWorldScala extends PortableApplication {
  var leftPaddle: Paddle = _
  var rightPaddle: Paddle = _
  var ball: Ball = _
  var p1: Player = _
  var p2: Player = _
  var p1b: Boolean = false
  var p2b: Boolean = false
  var server: SimpleSocketServer = _
  var menu: Menu = _
  var inMenu: Boolean = true

  override def onInit(): Unit = {
    setTitle("Pong")
    menu = new Menu(startGame, this)
    menu.init()
    initializeGame()
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    if (inMenu) {
      menu.draw()
      try {
        if (p1.client.getCheck) {
          println("piouuuu")
          if (p1.client.toGet == "start") inMenu = false
        }
      } catch {
        case e: NullPointerException =>
      }
      try {
        if (p2.client.getCheck) {
          println("popo")
          if (p2.client.toGet == "start") inMenu = false
        }
      } catch {
        case e: NullPointerException =>
      }
    } else {
      leftPaddle.draw(g)
      rightPaddle.draw(g)
      ball.draw(g)
      updateGame()
    }
    g.drawFPS()
  }

  def updateGame(): Unit = {
    if (p1b && p1.client.getCheck) {
      val commands = p1.client.toGet.split(",")
      leftPaddle.update("1", commands)
      rightPaddle.update("2", commands)
    }
    if (p2b && p2.client.getCheck) {
      val commands = p2.client.toGet.split(",")
      rightPaddle.update("2", commands)
      leftPaddle.update("1", commands)
    }
    ball.update(leftPaddle, rightPaddle)
  }

  def initializeGame(): Unit = {
    leftPaddle = new Paddle(20, Gdx.graphics.getHeight / 2 - 40, 10, 80)
    rightPaddle = new Paddle(Gdx.graphics.getWidth - 30, Gdx.graphics.getHeight / 2 - 40, 10, 80)
    ball = new Ball(Gdx.graphics.getWidth / 2, Gdx.graphics.getHeight / 2, 10, 10)
  }

  def startGame(): Unit = {
    inMenu = false
    Gdx.input.setInputProcessor(new InputProcessor {
      override def keyDown(keycode: Int): Boolean = {
        keycode match {
          case Input.Keys.UP =>
            if (p1b) p1.client.send("1,UP")
            else if (p2b) p2.client.send("2,UP")
            true
          case Input.Keys.DOWN =>
            if (p1b) p1.client.send("1,DOWN")
            else if (p2b) p2.client.send("2,DOWN")
            true
          case _ => false
        }
      }

      override def keyUp(i: Int): Boolean = true

      override def keyTyped(c: Char): Boolean = false

      override def touchDown(i: Int, i1: Int, i2: Int, i3: Int): Boolean = false

      override def touchUp(i: Int, i1: Int, i2: Int, i3: Int): Boolean = false

      override def touchDragged(i: Int, i1: Int, i2: Int): Boolean = false

      override def mouseMoved(i: Int, i1: Int): Boolean = false

      override def scrolled(i: Int): Boolean = false
    })
  }

  def exitGame(): Unit = {
    Gdx.app.exit()
  }

  override def onDispose(): Unit = {
    if (server != null) server.stop()
    menu.dispose()
    super.onDispose()
  }
}
