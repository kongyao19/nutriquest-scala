package nutriquest.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import nutriquest.MainApp

@FXML
class RootLayoutController:
  @FXML
  def handleClose(action: ActionEvent): Unit =
    System.exit(0)

  @FXML
  def handleAbout(action: ActionEvent): Unit =
    MainApp.showInstructions()