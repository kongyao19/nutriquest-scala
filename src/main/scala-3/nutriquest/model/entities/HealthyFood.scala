package nutriquest.model.entities

import nutriquest.model.{NutritionType, NutritionValue, Position}
import scalafx.scene.paint.Color

class HealthyFood(
                 position: Position,
                 nutritionValue: NutritionValue,
                 points: Int,
                 name: String,
                 val category: NutritionType
                 ) extends Food(position, nutritionValue, points, name):
  def getColor: Color = category.getColor

  def getSymbol: String = category.getSymbol

  def getDescription: String = s"$name: +$points points, boosts ${category.toString.toLowerCase}"