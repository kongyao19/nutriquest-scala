package nutriquest.model.game

import nutriquest.MainApp
import nutriquest.model.Input
import nutriquest.model.entities.{HealthyFood, Player, UnhealthyFood}

import scala.collection.mutable.ListBuffer
import scala.util.Random

class GameManager:
  // Game state
  var gameState: GameState = GameState.MainMenu
  var player: Player = Player()

  // Entity collections
  var healthyFoodGroup: ListBuffer[HealthyFood] = ListBuffer.empty
  var unhealthyFoodGroup: ListBuffer[UnhealthyFood] = ListBuffer.empty

  // Game timer (in seconds)
  var gameTimeLimit: Double = 120.0 // 2 minutes
  var currentGameTime: Double = 0.0

  // Map generation settings
  private val gameWidth = 1100
  private val gameHeight = 700
  private val healthyFoodPerMap = 15
  private val unhealthyFoodPerMap = 8

  def startGame(): Unit =
    gameState = GameState.Playing
    player = Player()
    currentGameTime = 0.0
    generateNewMap()

  def pauseGame(): Unit =
    if gameState == GameState.Playing then
      gameState = GameState.Paused
    else if gameState == GameState.Paused then
      gameState = GameState.Playing

  def update(deltaTime: Double): Unit =
    gameState match
      case GameState.Playing =>
        // Update game timer
        currentGameTime += deltaTime

        // Move player
        Player.move(player)

        // Check collisions
        checkCollisions()

        // Check if all healthy food collected (map reset condition)
        if healthyFoodGroup.isEmpty then
          generateNewMap()

        // Check time limit
        if currentGameTime >= gameTimeLimit then
          gameState = GameState.GameOver

        // Check for pause
        if Input.spacePressed then
          pauseGame()
          Input.spacePressed = false

        // Check for quit to main menu
        if Input.qPressed then
          MainApp.showQuitConfirmation()
          Input.qPressed = false

      case GameState.Paused =>
        if Input.spacePressed then
          pauseGame()
          Input.spacePressed = false

      case GameState.GameOver =>
        if Input.qPressed then
          MainApp.showQuitConfirmation()
          Input.qPressed = false

      case _ => // Handle other states in the UI layer

  private def checkCollisions(): Unit =
    val collisionDistance = 40.0

    // Check healthy food collisions
    healthyFoodGroup.filterInPlace: food =>
      val distance = math.sqrt(math.pow(player.posX - food.posX, 2) + math.pow(player.posY - food.posY, 2))
      if distance < collisionDistance then
        food.onCollect(player)
        false // Remove from list
      else
        true // Keep in list

    // Check unhealthy food collisions  
    unhealthyFoodGroup.filterInPlace: food =>
      val distance = math.sqrt(math.pow(player.posX - food.posX, 2) + math.pow(player.posY - food.posY, 2))
      if distance < collisionDistance then
        food.onCollect(player)
        false // Remove from list
      else
        true // Keep in list

  private def generateNewMap(): Unit =
    // Clear existing food
    healthyFoodGroup.clear()
    unhealthyFoodGroup.clear()

    // Generate healthy foods
    for _ <- 1 to healthyFoodPerMap do
      val x = Random.nextDouble() * (gameWidth - 100) + 50 // Leave margins
      val y = Random.nextDouble() * (gameHeight - 100) + 50
      healthyFoodGroup += HealthyFood.generateRandom(x, y)

    // Generate unhealthy foods
    for _ <- 1 to unhealthyFoodPerMap do
      val x = Random.nextDouble() * (gameWidth - 100) + 50
      val y = Random.nextDouble() * (gameHeight - 100) + 50
      unhealthyFoodGroup += UnhealthyFood.generateRandom(x, y)

  // Getter methods for UI
  def getHealthyFoods: List[HealthyFood] = healthyFoodGroup.toList
  def getUnhealthyFoods: List[UnhealthyFood] = unhealthyFoodGroup.toList

  // Game status methods
  def getRemainingTime: Double = math.max(0, gameTimeLimit - currentGameTime)
  def getElapsedTime: Double = currentGameTime
  def isGameOver: Boolean = gameState == GameState.GameOver
  def getCurrentScore: Int = player.score
  def getHealthyFoodCount: Int = healthyFoodGroup.size
  def getUnhealthyFoodCount: Int = unhealthyFoodGroup.size

  // Methods for UI display
  def getFormattedTime: String =
    val remaining = getRemainingTime
    val minutes = (remaining / 60).toInt
    val seconds = (remaining % 60).toInt
    f"$minutes%02d:$seconds%02d"

  def getGameProgress: Double =
    currentGameTime / gameTimeLimit

object GameManager:
  def apply(): GameManager = new GameManager()