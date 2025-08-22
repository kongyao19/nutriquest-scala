package nutriquest.view

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import nutriquest.MainApp
import nutriquest.model.game.GameState
import scalafx.Includes.*
import scalafx.animation.AnimationTimer
import scalafx.application.Platform
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

class GameController {
  @FXML
  private var gameArea: Pane = _
  @FXML
  private var scoreLabel: Label = _
  @FXML
  private var timeLabel: Label = _
  @FXML
  private var pauseLabel: Label = _

  private var timer: AnimationTimer = _
  private var lastTimer = 0L
  private var firstFrame = true
  private var gameInitialized = false

  // Keep track of food counts to detect collection
  private var lastHealthyFoodCount = 0
  private var lastUnhealthyFoodCount = 0

  def initialize(): Unit = {
    println("GameController initialized")
    setupGame()
  }

  private def setupGame(): Unit = {
    // Add resize listeners
    gameArea.widthProperty.addListener((_, _, newWidth) => {
      val width = newWidth.doubleValue()
      val height = gameArea.getHeight
      if (width > 0 && height > 0 && gameInitialized) {
        updateGameBounds(width, height)
      }
    })

    gameArea.heightProperty.addListener((_, _, newHeight) => {
      val width = gameArea.getWidth
      val height = newHeight.doubleValue()
      if (width > 0 && height > 0 && gameInitialized) {
        updateGameBounds(width, height)
      }
    })

    // Wait for proper layout
    Platform.runLater(() => {
      Platform.runLater(() => {
        Platform.runLater(() => {
          initializeGameWhenReady()
        })
      })
    })
  }

  private def initializeGameWhenReady(): Unit = {
    val width = gameArea.getWidth
    val height = gameArea.getHeight

    println(s"Attempting to initialize game with bounds: $width x $height")

    if (width > 50 && height > 50 && !gameInitialized) {
      gameInitialized = true

      // Update bounds in GameManager first
      MainApp.gameManager.updateGameBounds(width, height)

      // Start the game
      MainApp.gameManager.startGameWithBounds(width, height)

      // Add player
      gameArea.children.add(MainApp.gameManager.player.imageView)

      // Add initial food items
      addFoodToScene()

      // Initialize food counts
      lastHealthyFoodCount = MainApp.gameManager.getHealthyFoods.size
      lastUnhealthyFoodCount = MainApp.gameManager.getUnhealthyFoods.size

      // Start game loop
      startGameLoop()

      println("Game successfully initialized!")
    } else if (!gameInitialized) {
      println(s"Waiting for proper dimensions... Current: $width x $height")
      Platform.runLater(() => initializeGameWhenReady())
    }
  }

  private def updateGameBounds(width: Double, height: Double): Unit = {
    if (width <= 0 || height <= 0) return

    println(s"Updating game bounds to: $width x $height")
    MainApp.gameManager.updateGameBounds(width, height)

    // Refresh food positions for new bounds
    refreshFoodOnScreen()
  }

  private def addFoodToScene(): Unit = {
    // Add healthy foods
    MainApp.gameManager.getHealthyFoods.foreach { food =>
      if (!gameArea.children.contains(food.imageView)) {
        gameArea.children.add(food.imageView)
      }
    }
    // Add unhealthy foods
    MainApp.gameManager.getUnhealthyFoods.foreach { food =>
      if (!gameArea.children.contains(food.imageView)) {
        gameArea.children.add(food.imageView)
      }
    }
  }

  private def startGameLoop(): Unit = {
    timer = AnimationTimer(t => {
      val delta = if firstFrame then
        firstFrame = false
        lastTimer = t
        0.0  // Don't update on first frame
      else
        (t - lastTimer) / 1e9

      // Update game logic only if delta is valid
      if delta > 0 && delta < 0.1 then // Cap delta to prevent huge jumps
        MainApp.gameManager.update(delta)

      // Update HUD
      updateHUD()

      // Check if food was collected by comparing counts
      val currentHealthyCount = MainApp.gameManager.getHealthyFoods.size
      val currentUnhealthyCount = MainApp.gameManager.getUnhealthyFoods.size

      if (currentHealthyCount != lastHealthyFoodCount || currentUnhealthyCount != lastUnhealthyFoodCount) {
        // Food was collected or map regenerated, refresh the screen
        refreshFoodOnScreen()
        lastHealthyFoodCount = currentHealthyCount
        lastUnhealthyFoodCount = currentUnhealthyCount
      }

      // Handle game states
      MainApp.gameManager.gameState match
        case GameState.Playing =>
          pauseLabel.setVisible(false)

        case GameState.Paused =>
          pauseLabel.setVisible(true)

        case GameState.GameOver =>
          timer.stop()
          MainApp.showGameOver()

        case GameState.MainMenu =>
          timer.stop()
          MainApp.showMainMenu()

        case _ =>

      lastTimer = t
    })
    timer.start()
  }

  private def refreshFoodOnScreen(): Unit = {
    // Remove all existing food from scene (keep player)
    val toRemove = gameArea.children.filter(node => {
      node != MainApp.gameManager.player.imageView
    })
    gameArea.children.removeAll(toRemove.toSeq: _*)

    // Add player back if somehow removed
    if (!gameArea.children.contains(MainApp.gameManager.player.imageView)) {
      gameArea.children.add(MainApp.gameManager.player.imageView)
    }

    // Add current food items
    addFoodToScene()
  }

  private def updateHUD(): Unit = {
    scoreLabel.setText(MainApp.gameManager.getCurrentScore.toString)
    timeLabel.setText(MainApp.gameManager.getFormattedTime)

    // Change time color when running low
    val remainingTime = MainApp.gameManager.getRemainingTime
    if remainingTime <= 10 then
      timeLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;")
    else if remainingTime <= 20 then
      timeLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;")
    else
      timeLabel.setStyle("-fx-text-fill: lightgreen; -fx-font-weight: bold;")
  }

  def cleanup(): Unit = {
    if timer != null then
      timer.stop()
  }
}