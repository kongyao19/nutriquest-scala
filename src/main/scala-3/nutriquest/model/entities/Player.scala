package nutriquest.model.entities

import nutriquest.model.{Input, Movable}

class Player extends GameEntity(550, 350, "/nutriquest/images/sid.jpg") with Movable:
  // Player stats
  var score: Int = 0

  // Movement (constant speed)
  var movingSpeed: Double = 8.0

  // Add points to score
  def addScore(points: Int): Unit =
    score += points

object Player extends Movable:
  var movingSpeed: Double = 8.0

  def apply(): Player = new Player()

  def move(player: Player): Unit =
    if Input.wPressed || Input.upPressed then
      if player.posY > 0 then
        player.posY = player.posY - player.movingSpeed

    if Input.aPressed || Input.leftPressed then
      if player.posX > 0 then
        player.posX = player.posX - player.movingSpeed
        player.imageView.scaleX = -1 // Flip character to face left

    if Input.sPressed || Input.downPressed then
      if player.posY < 700 then
        player.posY = player.posY + player.movingSpeed

    if Input.dPressed || Input.rightPressed then
      if player.posX < 1100 then
        player.posX = player.posX + player.movingSpeed
        player.imageView.scaleX = 1 // Flip character back to face right