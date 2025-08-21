package nutriquest.model

import nutriquest.model.entities.Player

trait Movable:
  var movingSpeed: Double

trait Collectible:
  def onCollect(player: Player): Unit

trait Positionable:
  def posX: Double

  def posY: Double