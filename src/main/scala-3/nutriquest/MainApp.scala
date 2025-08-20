package nutriquest

import nutriquest.model.game.GameManager
import scalafx.application.JFXApp3

object MainApp extends JFXApp3:
  override def start(): Unit =
    println("Starting game controller...")
    val controller = GameManager()
    controller.startNewGame()
    println("Game initialized successfully!")
    println("Ready for GUI integration!")