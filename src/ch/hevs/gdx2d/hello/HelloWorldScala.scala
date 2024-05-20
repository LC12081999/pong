package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.{Gdx, Input, InputProcessor}
import ch.hevs.gdx2d.desktop.PortableApplication
import com.badlogic.gdx.utils.Timer
import com.badlogic.gdx.utils.Timer.Task

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
      if (p != null) {
        if (p.client.toGet == "start") startGame()
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
      p.client.getCheck = false
    }
    ball.update(leftPaddle, rightPaddle)
  }

  def initializeGame(): Unit = {
    leftPaddle = new Paddle(20, Gdx.graphics.getHeight / 2 - 40, 10, 80)
    rightPaddle = new Paddle(Gdx.graphics.getWidth - 30, Gdx.graphics.getHeight / 2 - 40, 10, 80)
    ball = new Ball(Gdx.graphics.getWidth / 2, Gdx.graphics.getHeight / 2, 10, 10)
  }

  var upPressed = false
  var downPressed = false
  val sendInterval = 0.1f // Intervalle en secondes pour envoyer les commandes

  def startGame(): Unit = {
    inMenu = false

    Gdx.input.setInputProcessor(new InputProcessor {
      override def keyDown(keycode: Int): Boolean = {
        keycode match {
          case Input.Keys.UP =>
            if (pb) {
              upPressed = true
              startSendingCommands("UP")
            }
            true
          case Input.Keys.DOWN =>
            if (pb) {
              downPressed = true
              startSendingCommands("DOWN")
            }
            true
          case _ => false
        }
      }

      override def keyUp(keycode: Int): Boolean = {
        keycode match {
          case Input.Keys.UP =>
            upPressed = false
            stopSendingCommands()
            true
          case Input.Keys.DOWN =>
            downPressed = false
            stopSendingCommands()
            true
          case _ => false
        }
      }

      override def keyTyped(c: Char): Boolean = false
      override def touchDown(i: Int, i1: Int, i2: Int, i3: Int): Boolean = false
      override def touchUp(i: Int, i1: Int, i2: Int, i3: Int): Boolean = false
      override def touchDragged(i: Int, i1: Int, i2: Int): Boolean = false
      override def mouseMoved(i: Int, i1: Int): Boolean = false
      override def scrolled(i: Int): Boolean = false
    })
  }

  def startSendingCommands(direction: String): Unit = {
    Timer.schedule(new Task {
      override def run(): Unit = {
        if (upPressed && direction == "UP") {
          p.client.send(s"${p.playerID},UP")
        } else if (downPressed && direction == "DOWN") {
          p.client.send(s"${p.playerID},DOWN")
        }
      }
    }, 0, sendInterval)
  }

  def stopSendingCommands(): Unit = {
    Timer.instance().clear()
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
