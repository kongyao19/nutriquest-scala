package nutriquest.model.entities

import nutriquest.model.NutritionTypes

import scala.util.Random

// General Food class
abstract class Food(_x: Double, _y: Double, _imagePath: String, val nutritionType: NutritionTypes, val pointValue: Int)
  extends GameEntity(_x, _y, _imagePath):

  def getPointValue: Int = pointValue

  def getNutritionType: NutritionTypes = nutritionType

// Healthy Food class
class HealthyFood(x: Double, y: Double, nutritionType: NutritionTypes)
  extends Food(x, y, getImagePath(nutritionType), nutritionType, getPointValue(nutritionType)):

  def onCollect(player: Player): Unit =
    player.addScore(pointValue)

object HealthyFood:
  def apply(x: Double, y: Double, nutritionType: NutritionTypes): HealthyFood =
    new HealthyFood(x, y, nutritionType)

  // Generate random healthy food
  def generateRandom(x: Double, y: Double): HealthyFood =
    val healthyTypes = Array(NutritionTypes.Protein, NutritionTypes.Vitamin,
      NutritionTypes.Carbs, NutritionTypes.Healthy_Fats)
    val randomType = healthyTypes(Random.nextInt(healthyTypes.length))
    new HealthyFood(x, y, randomType)

// Unhealthy Food class  
class UnhealthyFood(x: Double, y: Double)
  extends Food(x, y, "/nutriquest/images/junk.png", NutritionTypes.Junk, -10):

  def onCollect(player: Player): Unit =
    player.addScore(pointValue) // Negative points

object UnhealthyFood:
  def apply(x: Double, y: Double): UnhealthyFood = new UnhealthyFood(x, y)

  def generateRandom(x: Double, y: Double): UnhealthyFood =
    new UnhealthyFood(x, y)

// Helper functions for food generation
private def getImagePath(nutritionType: NutritionTypes): String = nutritionType match
  case NutritionTypes.Protein => "/nutriquest/images/protein.png"
  case NutritionTypes.Vitamin => "/nutriquest/images/vitamin.png"
  case NutritionTypes.Carbs => "/nutriquest/images/carbs.png"
  case NutritionTypes.Healthy_Fats => "/nutriquest/images/fats.png"
  case NutritionTypes.Junk => "/nutriquest/images/junk.png"

private def getPointValue(nutritionType: NutritionTypes): Int = nutritionType match
  case NutritionTypes.Protein => 15
  case NutritionTypes.Vitamin => 12
  case NutritionTypes.Carbs => 10
  case NutritionTypes.Healthy_Fats => 18
  case NutritionTypes.Junk => -10