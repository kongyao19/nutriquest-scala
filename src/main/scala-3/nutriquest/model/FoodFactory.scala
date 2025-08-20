package nutriquest.model

import nutriquest.model.entities.{Food, HealthyFood, UnhealthyFood}
import scala.util.Random

class FoodFactory:
  private val random = Random()

  def createRandomFood(position: Position): Food =
    val foodTypes = List(
      () => createHealthyFood(position, NutritionType.Protein),
      () => createHealthyFood(position, NutritionType.Vitamin),
      () => createHealthyFood(position, NutritionType.Carbs),
      () => createHealthyFood(position, NutritionType.Healthy_Fats),
      () => createUnhealthyFood(position)
    )
    foodTypes(random.nextInt(foodTypes.length))()
    
  private def createHealthyFood(position: Position, category: NutritionType): HealthyFood =
    category match {
      case NutritionType.Protein => HealthyFood(position, NutritionValue(protein = 10), 10, "Chicken", category)
      case NutritionType.Vitamin => HealthyFood(position, NutritionValue(vitamin = 15), 15, "Spinach", category)
      case NutritionType.Carbs => HealthyFood(position, NutritionValue(carbs = 8), 8, "Brown Rice", category)
      case NutritionType.Healthy_Fats => HealthyFood(position, NutritionValue(healthyFats = 12), 12, "Avocado", category)
      case _ => HealthyFood(position, NutritionValue(protein = 5), 5, "Generic Food", category)
    }
    
  private def createUnhealthyFood(position: Position): UnhealthyFood =
    val junkFoods = List("French Fries", "Soda", "Candy", "Chips")
    val penalties = List(15, 10, 8, 12)
    val index = random.nextInt(junkFoods.length)
    UnhealthyFood(position, junkFoods(index), penalties(index))