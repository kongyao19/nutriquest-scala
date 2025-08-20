package nutriquest.view

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, ProgressBar}
import javafx.scene.layout.{GridPane, HBox, VBox}
import nutriquest.model.entities.{HealthyFood, UnhealthyFood}
import nutriquest.model.game.GameManager
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.scene.shape.Rectangle
import scalafx.Includes.*
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.paint.Color
import scalafx.util.Duration

class GameViewController:
  @FXML
  private var gameGrid: GridPane = null
  @FXML
  private var scoreLabel: Label = null
  @FXML
  private var timeLabel: Label = null
  @FXML
  private var healthLabel: Label = null
  @FXML
  private var proteinBar: ProgressBar = null
  @FXML
  private var vitaminBar: ProgressBar = null
  @FXML
  private var carbsBar: ProgressBar = null
  @FXML
  private var fatsBar: ProgressBar = null
  @FXML
  private var startButton: Button = null
  @FXML
  private var pauseButton: Button = null
  @FXML
  private var gameInfoBox: VBox = null
  @FXML
  private var nutritionBox: HBox = null

  private val gameManager = new GameManager()
  private var gameLoop: Timeline = null
  private val gridSize = 20
  private val cellSize = 25

  private val gridCells: Array[Array[Rectangle]] = Array.ofDim(gridSize, gridSize)

  @FXML
  def initialize(): Unit =
    setupGameGrid()
    setupGameLoop()
    updateDisplay()

    Platform.runLater(() => {
      gameGrid.scene.value.setOnKeyPressed(handleKeyPress)
      gameGrid.requestFocus()
    })

  private def setupGameGrid(): Unit =
    gameGrid.children.clear()

    for
      row <- 0 until gridSize
      col <- 0 until gridSize

    do
      val cell = new Rectangle:
        width = cellSize
        height = cellSize
        fill = Color.LightGray
        stroke = Color.Black
        strokeWidth = 1

      gridCells(row)(col) = cell
      gameGrid.add(cell, col, row)

  private def setupGameLoop(): Unit = {
    gameLoop = new Timeline:
      keyFrames = Seq(
        KeyFrame(
          Duration(100),
          onFinished = _ => updateDisplay()
        )
      )
      cycleCount = Timeline.Indefinite
  }

  @FXML
  def handleStartGame(): Unit = {
    gameManager.startNewGame()
    gameLoop.play()
    startButton.disable = true
    pauseButton.disable = false
    gameGrid.requestFocus()
  }

  @FXML
  def handlePauseGame(): Unit =
    if gameLoop.status.value.toString == "RUNNING" then {
      gameLoop.pause()
      pauseButton.text = "Resume"
    }
    else
      gameLoop.play()
      pauseButton.text = "Pause"

  def handleKeyPress(keyEvent: KeyEvent): Unit =
    keyEvent.code match
      case KeyCode.W | KeyCode.Up => gameManager.handleKeyPress(KeyCode.W)
      case KeyCode.S | KeyCode.Down => gameManager.handleKeyPress(KeyCode.S)
      case KeyCode.A | KeyCode.Left => gameManager.handleKeyPress(KeyCode.A)
      case KeyCode.D | KeyCode.Right => gameManager.handleKeyPress(KeyCode.D)
      case _ =>

    keyEvent.consume()

  private def updateDisplay(): Unit =
    if !gameManager.isGameRunning then
      gameLoop.stop()
      showGameOver()
      return

    val gameState = gameManager.getGameState
    val player = gameState.getPlayer
    val nutrition = player.getNutrition

    for
      row <- 0 until gridSize
      col <- 0 until gridSize
    do
      gridCells(row)(col).fill = Color.LightGray

    val playerPos = player.getPosition
    if playerPos.isValidOn(gridSize) then
      gridCells(playerPos.y)(playerPos.x).fill = Color.Blue

    gameState.getAllFoods.filter(!_.isCollected).foreach{food =>
      val pos = food.getPosition
      if pos.isValidOn(gridSize) then
        val color = food match
          case _: HealthyFood => Color.Green
          case _: UnhealthyFood => Color.Red
          case _ => Color.Yellow
        gridCells(pos.y)(pos.x).fill = color
    }

    gameState.getAllPowerUps.filter(!_.isCollected).foreach{powerUp =>
      val pos = powerUp.getPosition
      if pos.isValidOn(gridSize) then
        gridCells(pos.y)(pos.x).fill = Color.Gold
    }

    scoreLabel.text = s"Score: ${player.getScore}"
    timeLabel.text = s"Time: ${gameState.getRemainingTime / 1000}s"
    healthLabel.text = s"Health: ${player.getHealth}"

    val maxNutrition = 100.0
    proteinBar.progress = math.min(nutrition.protein / maxNutrition, 1.0)
    vitaminBar.progress = math.min(nutrition.vitamin / maxNutrition, 1.0)
    carbsBar.progress = math.min(nutrition.carbs / maxNutrition, 1.0)
    fatsBar.progress = math.min(nutrition.healthyFats / maxNutrition, 1.0)

  private def showGameOver(): Unit =
    startButton.disable = false
    pauseButton.disable = true
    pauseButton.text = "Pause"

    val player = gameManager.getGameState.getPlayer
    val finalScore = player.getScore
    val healthRating = player.getHealth

    scoreLabel.text = s"Game Over! Final Score: $finalScore"
    healthLabel.text = s"Final Rating: $healthRating"