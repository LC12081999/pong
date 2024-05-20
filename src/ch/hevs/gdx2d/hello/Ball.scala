package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics

class Ball (xI: Int, yI: Int, h: Int, w: Int) extends Obj(xI, yI, h, w) {
  var speedX: Int = 4
  var speedY: Int = 4

  override def draw(g: GdxGraphics): Unit = {
    g.drawRectangle(x.toFloat, y.toFloat, width.toFloat, height.toFloat, 0f)
  }

  def update(leftPaddle: Paddle, rightPaddle: Paddle): Unit = {
    x += speedX
    y += speedY

    if (x <= leftPaddle.x + leftPaddle.width && y >= leftPaddle.y && y <= leftPaddle.y + leftPaddle.height) {
      speedX = -speedX
    } else if (x >= rightPaddle.x - rightPaddle.width && y >= rightPaddle.y && y <= rightPaddle.y + rightPaddle.height) {
      speedX = -speedX
    } else if (x <= 0) {
      reset()
    } else if (x >= 640 - width) {
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
