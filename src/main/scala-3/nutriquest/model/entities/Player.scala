package nutriquest.model.entities

import nutriquest.model.{Input, Movable}

class Player extends GameEntity(750, 300, "/nutriquest/images/sid.png") with Movable:
  // Player stats
  var score: Int = 0
  imageView.fitHeight = 100
  imageView.fitWidth = 100

  // Movement (constant speed)
  var movingSpeed: Double = 8.0

  // Player size
  val width: Double = 100.0
  val height: Double = 100.0

  // Add points to score
  def addScore(points: Int): Unit =
    score += points

object Player extends Movable:
  var movingSpeed: Double = 8.0

  // Dynamic game bounds
  private var gameWidth: Double = 800.0
  private var gameHeight: Double = 600.0

  def apply(): Player = new Player()

  def setGameBounds(width: Double, height: Double): Unit =
    gameWidth = width
    gameHeight = height
    println(s"Player bounds set to: $width x $height") // Debug

  def move(player: Player): Unit =
    val playerSize = 100.0 // Player's visual size

    if Input.wPressed || Input.upPressed then
      if player.posY > 0 then
        player.posY = math.max(0, player.posY - player.movingSpeed)

    if Input.aPressed || Input.leftPressed then
      if player.posX > 0 then
        player.posX = math.max(0, player.posX - player.movingSpeed)
        player.imageView.scaleX = 1 // Flip character to face left

    if Input.sPressed || Input.downPressed then
      if player.posY < gameHeight - playerSize then
        player.posY = math.min(gameHeight - playerSize, player.posY + player.movingSpeed)

    if Input.dPressed || Input.rightPressed then
      if player.posX < gameWidth - playerSize then
        player.posX = math.min(gameWidth - playerSize, player.posX + player.movingSpeed)
        player.imageView.scaleX = -1 // Flip character back to face right