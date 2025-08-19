package nutriquest.model

import scalafx.scene.paint.Color

enum NutritionType:
  case Protein, Vitamin, Carbs, Healthy_Fats, Junk

  def getColor: Color = this match {
    case Protein => Color.Red
    case Vitamin => Color.Green
    case Carbs => Color.Orange
    case Healthy_Fats => Color.Purple
    case Junk => Color.DarkRed
  }

  def getSymbol: String = this match {
    case Protein => "🥩"
    case Vitamin => "🥬"
    case Carbs => "🍞"
    case Healthy_Fats => "🥑"
    case Junk => "🍟"
  }

case class NutritionValue(
                         protein: Int = 0,
                         vitamin: Int = 0,
                         carbs: Int = 0,
                         healthyFats: Int = 0,
                         junk: Int = 0
                         ):

  def +(other: NutritionValue): NutritionValue = NutritionValue(
    protein + other.protein,
    vitamin + other.vitamin,
    carbs + other.carbs,
    healthyFats + other.healthyFats,
    junk + other.junk
  )
  
  def totalHealthy: Int = protein + vitamin + carbs + healthyFats
  
  def overallScore: Int = totalHealthy - (junk * 2)