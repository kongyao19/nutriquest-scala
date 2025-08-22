package nutriquest

import javafx.fxml.FXMLLoader
import javafx.scene as jfxs
import nutriquest.model.Input
import nutriquest.model.game.GameManager
import nutriquest.model.leaderboard.LBManager
import scalafx.application.{JFXApp3, Platform}
import scalafx.Includes.*
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.control.{Alert, ButtonType}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.{Group, Scene}
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.layout.AnchorPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

object MainApp extends JFXApp3:
  // Global managers
  var gameManager: GameManager = null
  var leaderboardManager: LBManager = null

  var roots: scalafx.scene.layout.BorderPane = null

  override def start(): Unit =
    gameManager = GameManager()
    leaderboardManager = LBManager()

    // Load root layout
    val rootResource = getClass.getResource("view/RootLayout.fxml")
    val loader = new FXMLLoader(rootResource)
    loader.load()
    roots = loader.getRoot[jfxs.layout.BorderPane]

    // Initialize stage
    stage = new PrimaryStage:
      title = "NutriQuest🍌 - Nutrition Adventure Game"
      scene = new Scene:
//        stylesheets += getClass.getResource("view/GameTheme.css").toString
        root = roots

        // Set up key event handling
        onKeyPressed = (e: KeyEvent) => {
          e.code match
            case KeyCode.W => Input.wPressed = true
            case KeyCode.A => Input.aPressed = true
            case KeyCode.S => Input.sPressed = true
            case KeyCode.D => Input.dPressed = true
            case KeyCode.Q => if !Input.qPressed then Input.qPressed = true
            case KeyCode.Space => if !Input.spacePressed then Input.spacePressed = true
            case KeyCode.Up => Input.upPressed = true
            case KeyCode.Down => Input.downPressed = true
            case KeyCode.Left => Input.leftPressed = true
            case KeyCode.Right => Input.rightPressed = true
            case _ =>
        }

        onKeyReleased = (e: KeyEvent) => {
          e.code match
            case KeyCode.W => Input.wPressed = false
            case KeyCode.A => Input.aPressed = false
            case KeyCode.S => Input.sPressed = false
            case KeyCode.D => Input.dPressed = false
            case KeyCode.Q => Input.qPressed = false
            case KeyCode.Space => Input.spacePressed = false
            case KeyCode.Up => Input.upPressed = false
            case KeyCode.Down => Input.downPressed = false
            case KeyCode.Left => Input.leftPressed = false
            case KeyCode.Right => Input.rightPressed = false
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
    val resource = getClass.getResource("view/Game.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val controller = loader.getController[nutriquest.view.GameController]()
    val gameRoot = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.center = gameRoot

  def showGameOver(): Unit =
    val resource = getClass.getResource("view/GameOver.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val controller = loader.getController[nutriquest.view.GameOverController]()
    controller.setFinalScore(gameManager.getCurrentScore)
    val gameOverRoot = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.center = gameOverRoot

  def showQuitConfirmation(): Unit =
    // Pause the game first
    if gameManager.gameState == nutriquest.model.game.GameState.Playing then
      gameManager.pauseGame()

    Platform.runLater(() => {
      val alert = new Alert(AlertType.Confirmation):
        title = "Quit Game"
        headerText = "Are you sure you want to quit?"
        contentText = "Your current progress will be lost."

      val result = alert.showAndWait()
      result match
        case Some(ButtonType.OK) =>
          gameManager.gameState = nutriquest.model.game.GameState.MainMenu
        case _ =>
          if gameManager.gameState == nutriquest.model.game.GameState.Paused then
            gameManager.pauseGame()
    })