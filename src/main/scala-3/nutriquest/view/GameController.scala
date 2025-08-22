package nutriquest.view

import javafx.fxml.FXML
import javafx.scene.Group
import javafx.scene.control.Label
import nutriquest.MainApp
import nutriquest.model.game.GameState
import scalafx.Includes.*
import scalafx.animation.AnimationTimer
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

class GameController {
  @FXML 
  private var gameArea: Group = _
  @FXML 
  private var scoreLabel: Label = _
  @FXML 
  private var timeLabel: Label = _
  @FXML 
  private var pauseLabel: Label = _

  private var timer: AnimationTimer = _
  private var lastTimer = 0L
  private var firstFrame = true

  def initialize(): Unit = {
    // This method is called automatically after FXML loading
    setupGame()
  }

  private def setupGame(): Unit = {
    // Start the game
    MainApp.gameManager.startGame()

    // Add background
    gameArea.children.add(Rectangle(1100, 700, Color.DarkGreen))

    // Add player
    gameArea.children.add(MainApp.gameManager.player.imageView)

    // Add initial food items
    addFoodToScene()

    // Start game loop
    startGameLoop()
  }

  private def addFoodToScene(): Unit = {
    // Add healthy foods
    MainApp.gameManager.getHealthyFoods.foreach(food => gameArea.children.add(food.imageView))
    // Add unhealthy foods
    MainApp.gameManager.getUnhealthyFoods.foreach(food => gameArea.children.add(food.imageView))
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
      if delta > 0 then
        MainApp.gameManager.update(delta)

      // Update HUD
      updateHUD()

      // Check if map was regenerated (all healthy food collected)
      if MainApp.gameManager.getHealthyFoods.size == MainApp.gameManager.healthyFoodGroup.size &&
        MainApp.gameManager.getUnhealthyFoods.size == MainApp.gameManager.unhealthyFoodGroup.size then
        // Map regenerated, need to refresh food on screen
        refreshFoodOnScreen()

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
    // Remove all existing food from scene
    val toRemove = gameArea.children.filter(node => {
      node != MainApp.gameManager.player.imageView && !node.isInstanceOf[Rectangle]
    })
    gameArea.children.removeAll(toRemove.toSeq: _*)

    // Add player back (in case it was removed)
    if (!gameArea.children.contains(MainApp.gameManager.player.imageView)) {
      gameArea.children.add(MainApp.gameManager.player.imageView)
    }

    // Add new food items
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