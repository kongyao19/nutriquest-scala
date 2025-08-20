package nutriquest.model.entities

import nutriquest.model.{NutritionValue, Position}
import scalafx.scene.paint.Color

class UnhealthyFood(
                  position: Position,
                  name: String,
                  val penalty: Int
                  ) extends Food(position, NutritionValue(junk = penalty), -penalty, name):
  def getColor: Color = Color.DarkRed

  def getSymbol: String = "🍟"

  def getDescription: String = s"$name: -$penalty points, junk food penalty!"

  override def onCollect(player: Player): Unit =
    player.addScore(-penalty)
    player.addNutrition(nutritionValue)
    player.applyJunkPenalty(penalty)
    collect()