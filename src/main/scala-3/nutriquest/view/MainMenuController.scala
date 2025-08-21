package nutriquest.view

import javafx.fxml.FXML
import javafx.scene.control.{Alert, ButtonType}
import nutriquest.MainApp

@FXML
class MainMenuController:

  def handleStartGame(): Unit =
    MainApp.showGame()

  def handleInstructions(): Unit =
    MainApp.showInstructions()

  def handleLeaderboard(): Unit =
    MainApp.showLeaderboard()

  def handleQuit(): Unit =
    val alert = new Alert(Alert.AlertType.CONFIRMATION):
      initOwner(MainApp.stage)
      setTitle("Quit NutriQuest")
      setHeaderText("Are you sure you want to quit?")
      setContentText("Your progress will be lost.")
      getButtonTypes.setAll(ButtonType.YES, ButtonType.NO)

    val result = alert.showAndWait()
    result match
      case Some(ButtonType.YES) => System.exit(0)
      case _ =>