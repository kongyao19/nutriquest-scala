package nutriquest.model.entities

import nutriquest.model.{Drawable, Movable, NutritionValue, Position}
import scalafx.scene.paint.Color

import scala.collection.mutable.ListBuffer

class Player(private var position: Position) extends Drawable, Movable:
  private var score: Int = 0
  private var nutrition: NutritionValue = NutritionValue()
  private var activePowerUps: ListBuffer[PowerUp] = ListBuffer()
  private var speedBoostActive: Boolean = false
  private var scoreMultiplierActive: Boolean = false
  
  def getPosition: Position = position
  
  def getColor: Color = Color.Blue
  
  def getSymbol: String = "😊"
  
  def move(direction: Position): Unit =
    position += direction
    
  def canMoveTo(newPosition: Position, gridSize: Int): Boolean =
    newPosition.isValidOn(gridSize)
    
  def addScore(points: Int): Unit =
    val finalPoints = if scoreMultiplierActive then points * 2 else points
    score += finalPoints
    
  def addNutrition(nutritionValue: NutritionValue): Unit =
    nutrition += nutritionValue
    
  def applyJunkPenalty(penalty: Int): Unit =
    println(s"Junk food penalty applied: $penalty")
    
  def activateSpeedBoost(): Unit = speedBoostActive = true
  
  def deactivateSpeedBoost(): Unit = speedBoostActive = false
  
  def activateScoreMultiplier(): Unit = scoreMultiplierActive = true
  
  def deactivateScoreMultiplier(): Unit = scoreMultiplierActive = false
  
  def getScore: Int = score
  
  def getNutrition: NutritionValue = nutrition
  
  def hasSpeedBoost: Boolean = speedBoostActive
  
  def hasScoreMultiplier: Boolean = scoreMultiplierActive
  
  def getHealth: String =
    val healthScore = nutrition.overallScore
    if healthScore >= 100 then "Excellent!"
    else if healthScore >= 50 then "Good"
    else if healthScore >= 0 then "Fair"
    else "Poor"