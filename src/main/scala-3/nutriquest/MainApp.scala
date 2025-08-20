package nutriquest

import javafx.fxml.FXMLLoader
import nutriquest.model.game.GameManager
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.Includes.*
import scalafx.scene.Scene

import java.net.URL

object MainApp extends JFXApp3:
  override def start(): Unit =
    val rootLayoutResource: URL = getClass.getResource("/nutriquest/view/GameView.fxml")
    val loader = new FXMLLoader(rootLayoutResource)
    val rootLayout = loader.load[javafx.scene.layout.BorderPane]()
    stage = new PrimaryStage:
      title = "NutriQuest🍌"
      scene = new Scene:
        root = rootLayout
