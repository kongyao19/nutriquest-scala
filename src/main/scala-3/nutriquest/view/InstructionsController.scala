package nutriquest.view

import javafx.fxml.FXML
import nutriquest.MainApp

@FXML
class InstructionsController:
  @FXML
  def handleBackToMenu(): Unit =
    MainApp.showMainMenu()