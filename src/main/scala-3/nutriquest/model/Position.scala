package nutriquest.model

case class Position(x: Int, y: Int):
  def +(other: Position): Position = Position(x + other.x, y + other.y)

  def isValidOn(gridSize: Int): Boolean = x >= 0 && x < gridSize && y >= 0 && y < gridSize

object Direction:
  val up = Position(0, -1)
  val down = Position(0, 1)
  val left = Position(-1, 0)
  val right = Position(1, 0)