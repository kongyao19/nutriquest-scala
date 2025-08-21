package nutriquest

import javafx.fxml.FXMLLoader
import javafx.scene as jfxs
import nutriquest.model.Input
import nutriquest.model.game.GameManager
import nutriquest.model.leaderboard.LBManager
import scalafx.application.JFXApp3
import scalafx.Includes.*
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.{Group, Scene}
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.layout.AnchorPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

//import javafx.fxml.FXMLLoader
//import nutriquest.model.game.GameManager
//import scalafx.application.JFXApp3
//import scalafx.application.JFXApp3.PrimaryStage
//import scalafx.Includes.*
//import scalafx.scene.Scene
//
//import java.net.URL

object MainApp extends JFXApp3:
  // Global managers
  val gameManager = GameManager()
  val leaderboardManager = LBManager()
  var roots: scalafx.scene.layout.BorderPane = null
  
  override def start(): Unit =
    // Load root layout
    val rootResource = getClass.getResource("view/RootLayout.fxml")
    val loader = new FXMLLoader(rootResource)
    loader.load()
    roots = loader.getRoot[jfxs.layout.BorderPane]
  
    // Initialize stage
    stage = new PrimaryStage:
      title = "NutriQuest🍌 - Nutrition Adventure Game"
      resizable = false
      scene = new Scene:
        stylesheets += getClass.getResource("view/GameTheme.css").toString
        root = roots
  
        // Set up key event handling
        onKeyPressed = (e: KeyEvent) => {
          e.code match
            case KeyCode.W => Input.wPressed = true
            case KeyCode.A => Input.aPressed = true
            case KeyCode.S => Input.sPressed = true
            case KeyCode.D => Input.dPressed = true
            case KeyCode.Q => Input.qPressed = true
            case KeyCode.Space => Input.spacePressed = !Input.spacePressed
            case _ =>
        }
  
        onKeyReleased = (e: KeyEvent) => {
          e.code match
            case KeyCode.W => Input.wPressed = false
            case KeyCode.A => Input.aPressed = false
            case KeyCode.S => Input.sPressed = false
            case KeyCode.D => Input.dPressed = false
            case KeyCode.Q => Input.qPressed = false
            case _ =>
        }
    showMainMenu()
    
  // Navigation methods
  def showMainMenu(): Unit =
    val resource = getClass.getResource("view/MainMenu.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val mainMenuRoot = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.center = mainMenuRoot

  def showInstructions(): Unit =
    val resource = getClass.getResource("view/Instructions.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val instructionsRoot = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.center = instructionsRoot

  def showLeaderboard(): Unit =
    val resource = getClass.getResource("view/Leaderboard.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val controller = loader.getController[nutriquest.view.LeaderboardController]()
    controller.loadLeaderboard()
    val leaderboardRoot = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.center = leaderboardRoot

  def showGame(): Unit =
    roots.center = new AnchorPane():
      children = new Group() {}

      // Start the game
      gameManager.startGame()

      // Add background
      children.add(Rectangle(1200, 800, Color.DarkGreen))

      // Add player
      children.add(gameManager.player.imageView)

      // Add initial food items
      gameManager.getHealthyFoods.foreach(food => children.add(food.imageView))
      gameManager.getUnhealthyFoods.foreach(food => children.add(food.imageView))

      // Game loop timer
      var lastTimer = 0L
      val timer: AnimationTimer = AnimationTimer(t => {
        val delta = (t - lastTimer) / 1e9

        // Update game logic
        gameManager.update(delta)

        // Handle game states
        gameManager.gameState match
          case nutriquest.model.game.GameState.Playing =>
          // Update UI elements here if needed

          case nutriquest.model.game.GameState.Paused =>
            timer.stop()
            // Add pause overlay
            val pauseText = new scalafx.scene.text.Text("GAME PAUSED - Press SPACE to Resume"):
              x = 400
              y = 400
              style = "-fx-font-size: 24px; -fx-fill: white;"
            children.add(pauseText)

            val pauseTimer: AnimationTimer = AnimationTimer(t => {
              if gameManager.gameState != nutriquest.model.game.GameState.Paused then
                children.remove(pauseText)
                timer.start()
            })
            pauseTimer.start()

          case nutriquest.model.game.GameState.GameOver =>
            timer.stop()
            showGameOver()

          case _ =>

        lastTimer = t
      })
      timer.start()

  def showGameOver(): Unit =
    val resource = getClass.getResource("view/GameOver.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val controller = loader.getController[nutriquest.view.GameOverController]()
    controller.setFinalScore(gameManager.getCurrentScore)
    val gameOverRoot = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.center = gameOverRoot
