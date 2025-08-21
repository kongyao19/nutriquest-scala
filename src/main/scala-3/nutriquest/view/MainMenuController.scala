package nutriquest.view

import javafx.fxml.FXML
import javafx.scene.control.{Alert, ButtonType}
import nutriquest.MainApp

@FXML
class MainMenuController:
  @FXML
  def handleStartGame(): Unit =
    MainApp.showGame()
  @FXML
  def handleInstructions(): Unit =
    MainApp.showInstructions()
  @FXML
  def handleLeaderboard(): Unit =
    MainApp.showLeaderboard()
  @FXML
  def handleQuit(): Unit =
    val alert = new Alert(Alert.AlertType.CONFIRMATION):
      initOwner(MainApp.stage)
      setTitle("Quit NutriQuest")
      setHeaderText("Are you sure you want to quit?")
      setContentText("Your progress will be lost.")

    val result = alert.showAndWait()
    if result.isPresent && result.get() == ButtonType.OK then
      System.exit(0)