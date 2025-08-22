package nutriquest.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Alert, ButtonType}
import nutriquest.MainApp
import nutriquest.util.Database

@FXML
class RootLayoutController:
  @FXML
  def handleClose(action: ActionEvent): Unit =
    System.exit(0)

  @FXML
  def handleReset(action: ActionEvent): Unit =
    val alert = new Alert(Alert.AlertType.CONFIRMATION)
    alert.initOwner(MainApp.stage)
    alert.setTitle("Clear Leaderboard")
    alert.setHeaderText("Are you sure you want to clear all scores?")
    alert.setContentText("This action cannot be undone.")
    val result = alert.showAndWait()
    if result.isPresent && result.get() == ButtonType.OK then {
        Database.clearLeaderboard()
        MainApp.showMainMenu()
    }

  @FXML
  def handleAbout(action: ActionEvent): Unit =
    MainApp.showInstructions()