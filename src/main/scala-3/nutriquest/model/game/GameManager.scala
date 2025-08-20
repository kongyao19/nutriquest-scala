package nutriquest.model.game

import scalafx.scene.input.KeyCode

class GameManager:
  private val gameState = GameState(20)
  private val gameStats = GameStats()

  def startNewGame(): Unit =
    gameState.startGame()
    println("New game started!")

  def handleKeyPress(keyCode: KeyCode): Unit =
    gameState.movePlayer(keyCode)
    
  def isGameRunning: Boolean = gameState.isGameRunning
  
  def getGameState: GameState = gameState
  
  def getGameStats: GameStats = gameStats
  
  def endCurrentGame(): Unit =
    if gameState.isGameRunning then
      val finalScore = gameState.getPlayer.getScore
      val nutritionScore = gameState.getPlayer.getNutrition.overallScore
      gameStats.recordGame(finalScore, nutritionScore)
      gameState.endGame()