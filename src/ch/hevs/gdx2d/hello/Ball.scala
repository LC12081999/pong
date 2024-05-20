package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics

class Ball(xI: Int, yI: Int, h: Int, w: Int) extends Obj(xI, yI, h, w) {
  var speedX: Int = 4
  var speedY: Int = 4

  override def draw(g: GdxGraphics): Unit = {
    g.drawRectangle(x.toFloat, y.toFloat, width.toFloat, height.toFloat, 0f)
  }

  def update(leftPaddle: Paddle, rightPaddle: Paddle): Unit = {
    x += speedX
    y += speedY

    // Vérification des collisions avec les raquettes
    if (x <= leftPaddle.x + leftPaddle.width && y + height >= leftPaddle.y && y <= leftPaddle.y + leftPaddle.height) {
      speedX = -speedX
    } else if (x + width >= rightPaddle.x && y + height >= rightPaddle.y && y <= rightPaddle.y + rightPaddle.height) {
      speedX = -speedX
    }

    // Vérification des collisions avec les bords du terrain
    if (y <= 0 || y + height >= 480) { // Supposons que la hauteur du terrain soit 480
      speedY = -speedY
    }

    // Réinitialisation si la balle sort du terrain par la gauche ou la droite
    if (x <= 0 || x >= 640 - width) { // Supposons que la largeur du terrain soit 640
      reset()
    }
  }

  def reset(): Unit = {
    x = 320
    y = 240
    speedX = 4
    speedY = 4
  }
}
