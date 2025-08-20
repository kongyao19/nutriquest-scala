package nutriquest.model.game

class GameStats:
  private var gamesPlayed: Int = 0
  private var totalScore: Int = 0
  private var bestScore: Int = 0
  private var totalNutritionScore: Int = 0

  def recordGame(finalScore: Int, nutritionScore: Int): Unit =
    gamesPlayed += 1
    totalScore += finalScore
    if finalScore > bestScore then bestScore = finalScore
    totalNutritionScore += nutritionScore

  def getAverageScore: Double = if gamesPlayed > 0 then totalScore.toDouble / gamesPlayed else 0.0

  def getBestScore: Int  = bestScore

  def getGamesPlayed: Int = gamesPlayed

  def getAverageNutritionScore: Double = if gamesPlayed > 0 then totalNutritionScore.toDouble / gamesPlayed else 0.0

  def getPerformanceLevel: String =
    val avgScore = getAverageScore
    if avgScore >= 200 then "Nutrition Expert!"
    else if avgScore >= 150 then "Health Conscious"
    else if avgScore >= 100 then "Learning"
    else "Needs Improvement"