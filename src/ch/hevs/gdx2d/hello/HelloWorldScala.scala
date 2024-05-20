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
  var p: Player = _
  var pb: Boolean = false
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
        if (p.client.getCheck) {
          println("piouuuu")
          if (p.client.toGet == "start") inMenu = false
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
    if (pb && p.client.getCheck) {
      val commands = p.client.toGet.split(",")
      leftPaddle.update("1", commands)
      rightPaddle.update("2", commands)
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
            if (pb) p.client.send(s"${p.playerID},UP")
            true
          case Input.Keys.DOWN =>
            if (pb) p.client.send(s"${p.playerID},DOWN")
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
