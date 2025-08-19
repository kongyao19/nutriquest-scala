package nutriquest.model.entities

import nutriquest.model.{Collectible, Drawable, NutritionValue, Position}

abstract class Food(
                   protected var position: Position,
                   val nutritionValue: NutritionValue,
                   val points: Int,
                   val name: String,
                   ) extends Drawable, Collectible:
  protected var collected: Boolean = false

  def getPosition: Position = position

  def isCollected: Boolean = collected

  def collect(): Unit = collected = true

  def getDescription: String

  def onCollect(player: Player): Unit =
    player.addScore(points)
    player.addNutrition(nutritionValue)
    collect()