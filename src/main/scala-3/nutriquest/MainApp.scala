package nutriquest

import javafx.fxml.FXMLLoader
import javafx.scene as jfxs
import nutriquest.model.Input
import nutriquest.model.game.GameManager
import nutriquest.util.Database
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.image.Image
import scalafx.scene.input.{KeyCode, KeyEvent}


object MainApp extends JFXApp3:
  // Global managers
  var gameManager: GameManager = null

  var roots: scalafx.scene.layout.BorderPane = null

  override def start(): Unit =
    // Initialize database
    try {
      Database.initializeDatabase()
    } catch {
      case e: Exception =>
        println(s"Database initialization error: ${e.getMessage}")
    }

    gameManager = GameManager()

    // Load root layout
    val rootResource = getClass.getResource("view/RootLayout.fxml")
    val loader = new FXMLLoader(rootResource)
    loader.load()
    roots = loader.getRoot[jfxs.layout.BorderPane]

    // Initialize stage
    stage = new PrimaryStage:
      title = "NutriQuest🍌 - Nutrition Adventure Game"
      icons += new Image(getClass.getResource("/nutriquest/images/sid.png").toExternalForm)
      resizable = true
      minWidth = 510 //800
      minHeight = 630//600
      width = 510 //1200
      height = 630 //800
      scene = new Scene:
        root = roots
        stylesheets += getClass.getResource("/nutriquest/Style.css").toExternalForm
//        fill = Color.Black

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
    val instructionsRoot = loader.getRoot[jfxs.layout.BorderPane]
    this.roots.center = instructionsRoot

  def showLeaderboard(): Unit =
    val resource = getClass.getResource("view/Leaderboard.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val controller = loader.getController[nutriquest.view.LeaderboardController]()
    controller.loadLeaderboard()
    val leaderboardRoot = loader.getRoot[jfxs.layout.BorderPane]
    this.roots.center = leaderboardRoot

  def showGame(): Unit =
    val resource = getClass.getResource("view/Game.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val controller = loader.getController[nutriquest.view.GameController]()
    val gameRoot = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.center = gameRoot
    stage.setMaximized(true)

  def showGameOver(): Unit =
    val resource = getClass.getResource("view/GameOver.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val controller = loader.getController[nutriquest.view.GameOverController]()
    controller.setFinalScore(gameManager.getCurrentScore)
    val gameOverRoot = loader.getRoot[jfxs.layout.BorderPane]
    this.roots.center = gameOverRoot
    stage.setMaximized(false)
    stage.setWidth(510)
    stage.setHeight(630)
    stage.centerOnScreen()