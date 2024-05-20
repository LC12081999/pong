package ch.hevs.gdx2d.hello

import ch.hevs.gdx2d.lib.GdxGraphics

abstract class Obj(xI: Int, yI: Int, w: Int, h: Int) {
  var x = xI
  var y = yI
  val width: Int = w
  val height: Int = h
  def draw(g: GdxGraphics): Unit
}
