package nutriquest.view

import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Label, ProgressBar}
import nutriquest.MainApp

import java.net.URL
import java.util.ResourceBundle

class GameHUDController extends Initializable:

  @FXML
  private var scoreLabel: Label = _

  @FXML
  private var timeLabel: Label = _

  @FXML
  private var healthyFoodLabel: Label = _

  @FXML
  private var gameProgress: ProgressBar = _

  override def initialize(url: URL, rb: ResourceBundle): Unit =
    // Initialize HUD components
    ()

  def updateHUD(): Unit =
    val gm = MainApp.gameManager
    if scoreLabel != null then scoreLabel.setText(s"Score: ${gm.getCurrentScore}")
    if timeLabel != null then timeLabel.setText(s"Time: ${gm.getFormattedTime}")
    if healthyFoodLabel != null then healthyFoodLabel.setText(s"Healthy Food: ${gm.getHealthyFoodCount}")
    if gameProgress != null then gameProgress.setProgress(gm.getGameProgress)