package ch.hevs.gdx2d.hello

import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.scenes.scene2d.{InputEvent, Stage}
import com.badlogic.gdx.scenes.scene2d.ui.{Skin, TextButton, TextField}
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

import scala.concurrent.ExecutionContext.Implicits.global

class Menu(val startGame: () => Unit, val hw: HelloWorldScala) {
  val buttonWidth = 180
  val buttonHeight = 30

  val skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"))
  val stage = new Stage()

  val p1Button = new TextButton("Host Game", skin)
  val ipFieldP2 = new TextField("", skin)
  ipFieldP2.setMessageText("Enter IP here...")

  def init(): Unit = {
    Gdx.input.setInputProcessor(stage)

    p1Button.setWidth(buttonWidth)
    p1Button.setHeight(buttonHeight)
    p1Button.setPosition(Gdx.graphics.getWidth / 2 - buttonWidth / 2, (Gdx.graphics.getHeight * 0.6).toInt)
    p1Button.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = {
        super.clicked(event, x, y)
        hw.server = new SimpleSocketServer(9999)
        hw.server.start()
        hw.p = new Player(new SimpleSocketClient("localhost", 9999), "1")
        hw.pb = true
        //startGame()
      }
    })

    ipFieldP2.setWidth(buttonWidth)
    ipFieldP2.setHeight(buttonHeight)
    ipFieldP2.setPosition(Gdx.graphics.getWidth / 2 - buttonWidth / 2, (Gdx.graphics.getHeight * 0.4).toInt)
    ipFieldP2.setTextFieldListener(new TextFieldListener {
      override def keyTyped(textField: TextField, key: Char): Unit = {
        if (key == '\r' || key == '\n') {
          hw.p = new Player(new SimpleSocketClient(ipFieldP2.getText, 9999), "2")
          hw.pb = true
          hw.p.client.send("start")
          //startGame()
        }
      }
    })

    stage.addActor(p1Button)
    stage.addActor(ipFieldP2)
  }

  def draw(): Unit = {
    stage.act()
    stage.draw()
  }

  def dispose(): Unit = {
    stage.dispose()
    skin.dispose()
  }
}
