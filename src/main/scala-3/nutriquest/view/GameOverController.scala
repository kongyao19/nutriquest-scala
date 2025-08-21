package nutriquest.view

import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Alert, Label, TextField}
import nutriquest.MainApp

import java.net.URL
import java.util.ResourceBundle

class GameOverController extends Initializable:

  @FXML
  private var finalScoreLabel: Label = _

  @FXML
  private var playerNameField: TextField = _

  private var finalScore: Int = 0

  override def initialize(url: URL, rb: ResourceBundle): Unit =
    // Initialize components
    ()

  def setFinalScore(score: Int): Unit =
    finalScore = score
    if finalScoreLabel != null then
      finalScoreLabel.setText(s"Final Score: $score")

  @FXML
  def handleSaveScore(): Unit =
    val playerName = playerNameField.getText.trim

    if playerName.isEmpty then
      val alert = new Alert(Alert.AlertType.WARNING)
      alert.initOwner(MainApp.stage)
      alert.setTitle("Invalid Name")
      alert.setHeaderText("Please enter your name")
      alert.setContentText("Name cannot be empty.")
      alert.showAndWait()
    else
      // Save score to leaderboard
      MainApp.leaderboardManager.addScore(playerName, finalScore)

      val alert = new Alert(Alert.AlertType.INFORMATION)
      alert.initOwner(MainApp.stage)
      alert.setTitle("Score Saved")
      alert.setHeaderText("Congratulations!")
      alert.setContentText(s"Your score of $finalScore has been saved to the leaderboard!")
      alert.showAndWait()

      MainApp.showLeaderboard()

  @FXML
  def handlePlayAgain(): Unit =
    MainApp.showGame()

  @FXML
  def handleBackToMenu(): Unit =
    MainApp.showMainMenu()