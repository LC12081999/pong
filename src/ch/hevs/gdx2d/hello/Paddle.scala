package ch.hevs.gdx2d.hello
import ch.hevs.gdx2d.lib.GdxGraphics

class Paddle(xI: Int, yI: Int, w: Int, h: Int) extends Obj(xI, yI, w, h) {
  val speed: Int = 5

  override def draw(g: GdxGraphics): Unit = {
    g.drawFilledRectangle(x.toFloat, y.toFloat, width.toFloat, height.toFloat, 0f)
  }

  def update(playerID: String, a: Array[String]): Unit = {
    if (a(0) == playerID) {
      if (a(1) == "UP") {
        y += speed
      } else if (a(1) == "DOWN") {
        y -= speed
      }
      y = Math.max(0, Math.min(y, 480 - height))
    }
  }
}
