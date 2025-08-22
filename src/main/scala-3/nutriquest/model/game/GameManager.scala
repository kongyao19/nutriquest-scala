package nutriquest.model.game

import nutriquest.model.Input
import nutriquest.model.entities.{HealthyFood, Player, UnhealthyFood}
import scalafx.application.Platform
import scalafx.scene.control.{Alert, ButtonType}
import scalafx.scene.control.Alert.AlertType

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
  var gameTimeLimit: Double = 30.0
  var currentGameTime: Double = 0.0

  // Map generation settings - these will be updated dynamically
  private var gameWidth = 800.0
  private var gameHeight = 600.0
  private val healthyFoodPerMap = 15
  private val unhealthyFoodPerMap = 8

  def updateGameBounds(width: Double, height: Double): Unit =
    gameWidth = width
    gameHeight = height
    println(s"GameManager bounds updated to: $width x $height")

    // Update player movement bounds
    Player.setGameBounds(width, height)

    // Ensure player stays within new bounds
    val playerSize = 50.0
    player.posX = math.min(player.posX, width - playerSize)
    player.posY = math.min(player.posY, height - playerSize)
    player.posX = math.max(player.posX, 0)
    player.posY = math.max(player.posY, 0)

  def startGame(): Unit =
    gameState = GameState.Playing
    player = Player()
    currentGameTime = 0.0

  def startGameWithBounds(width: Double, height: Double): Unit =
    gameState = GameState.Playing
    player = Player()
    currentGameTime = 0.0

    // Set bounds first, then generate map
    updateGameBounds(width, height)
    generateNewMap()

  def pauseGame(): Unit =
    if gameState == GameState.Playing then
      gameState = GameState.Paused
    else if gameState == GameState.Paused then
      gameState = GameState.Playing

  def showQuitConfirmation(): Unit =
    // Pause the game first
    if gameState == GameState.Playing then pauseGame()

    Platform.runLater(() => {
      val alert = new Alert(AlertType.Confirmation):
        title = "Quit Game"
        headerText = "Are you sure you want to quit?"
        contentText = "Your current progress will be lost."

      val result = alert.showAndWait()
      result match
        case Some(ButtonType.OK) =>
          gameState = GameState.MainMenu
        case _ =>
          if gameState == GameState.Paused then pauseGame()
    })

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
          showQuitConfirmation()
          Input.qPressed = false

      case GameState.Paused =>
        if Input.spacePressed then
          pauseGame()
          Input.spacePressed = false

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

    println(s"Generating map with bounds: $gameWidth x $gameHeight")

    // Minimum distance between food items and edge buffer
    val minDistance = 80.0
    val maxAttempts = 100
    val edgeBuffer = 50.0 // Keep food away from edges
    val playerSize = 50.0

    // Keep track of all occupied positions
    val occupiedPositions = scala.collection.mutable.ListBuffer[(Double, Double)]()

    // Add player position to avoid spawning food on player
    occupiedPositions += ((player.posX, player.posY))

    // Calculate usable area (accounting for food size and edge buffer)
    val usableWidth = gameWidth - (edgeBuffer * 2) - playerSize
    val usableHeight = gameHeight - (edgeBuffer * 2) - playerSize

    if (usableWidth <= 0 || usableHeight <= 0) {
      println(s"Invalid game bounds for food generation! Width: $usableWidth, Height: $usableHeight")
      return
    }

    // Generate healthy foods
    for _ <- 1 to healthyFoodPerMap do
      var placed = false
      var attempts = 0

      while !placed && attempts < maxAttempts do
        val x = Random.nextDouble() * usableWidth + edgeBuffer
        val y = Random.nextDouble() * usableHeight + edgeBuffer

        // Check if this position is too close to any existing position
        val tooClose = occupiedPositions.exists { case (existingX, existingY) =>
          val distance = math.sqrt(math.pow(x - existingX, 2) + math.pow(y - existingY, 2))
          distance < minDistance
        }

        if !tooClose then
          healthyFoodGroup += HealthyFood.generateRandom(x, y)
          occupiedPositions += ((x, y))
          placed = true

        attempts += 1

      if attempts >= maxAttempts then
        println(s"Failed to place healthy food after $maxAttempts attempts")

    // Generate unhealthy foods
    for _ <- 1 to unhealthyFoodPerMap do
      var placed = false
      var attempts = 0

      while !placed && attempts < maxAttempts do
        val x = Random.nextDouble() * usableWidth + edgeBuffer
        val y = Random.nextDouble() * usableHeight + edgeBuffer

        // Check if this position is too close to any existing position
        val tooClose = occupiedPositions.exists { case (existingX, existingY) =>
          val distance = math.sqrt(math.pow(x - existingX, 2) + math.pow(y - existingY, 2))
          distance < minDistance
        }

        if !tooClose then
          unhealthyFoodGroup += UnhealthyFood.generateRandom(x, y)
          occupiedPositions += ((x, y))
          placed = true

        attempts += 1

      if attempts >= maxAttempts then
        println(s"Failed to place unhealthy food after $maxAttempts attempts")

    println(s"Generated ${healthyFoodGroup.size} healthy foods and ${unhealthyFoodGroup.size} unhealthy foods")

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