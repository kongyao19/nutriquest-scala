package nutriquest.view

import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Alert, Label, TextField}
import nutriquest.MainApp
import nutriquest.util.Database

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
      try {
        // Save score directly to database
        val gameTime = MainApp.gameManager.getElapsedTime
        Database.saveScore(playerName, finalScore, gameTime)

        val alert = new Alert(Alert.AlertType.INFORMATION)
        alert.initOwner(MainApp.stage)
        alert.setTitle("Score Saved")
        alert.setHeaderText("Congratulations!")
        alert.setContentText(s"Your score of $finalScore has been saved to the leaderboard!")
        alert.showAndWait()

        MainApp.showLeaderboard()

      } catch {
        case e: Exception =>
          val alert = new Alert(Alert.AlertType.ERROR)
          alert.initOwner(MainApp.stage)
          alert.setTitle("Error")
          alert.setHeaderText("Failed to save score")
          alert.setContentText(s"Error: ${e.getMessage}")
          alert.showAndWait()
      }

  @FXML
  def handlePlayAgain(): Unit =
    MainApp.showGame()

  @FXML
  def handleBackToMenu(): Unit =
    MainApp.showMainMenu()