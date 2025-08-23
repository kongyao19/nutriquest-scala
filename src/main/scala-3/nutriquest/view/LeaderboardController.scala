package nutriquest.view

import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.{Alert, ButtonType, TableColumn, TableView}
import nutriquest.MainApp
import nutriquest.util.Database

import java.net.URL
import java.util.ResourceBundle

@FXML
class LeaderboardController extends Initializable:

  @FXML
  private var leaderboardTable: TableView[Entry] = _

  @FXML
  private var rankColumn: TableColumn[Entry, Integer] = _

  @FXML
  private var nameColumn: TableColumn[Entry, String] = _

  @FXML
  private var scoreColumn: TableColumn[Entry, Integer] = _

  @FXML
  private var dateColumn: TableColumn[Entry, String] = _

  // Data class for table display
  class Entry(rank: Int, name: String, score: Int, date: String):
    def getRank: Integer = rank
    def getName: String = name
    def getScore: Integer = score
    def getDate: String = date

  override def initialize(url: URL, rb: ResourceBundle): Unit =
    // Setup table columns
    rankColumn.setCellValueFactory(new PropertyValueFactory[Entry, Integer]("rank"))
    nameColumn.setCellValueFactory(new PropertyValueFactory[Entry, String]("name"))
    scoreColumn.setCellValueFactory(new PropertyValueFactory[Entry, Integer]("score"))
    dateColumn.setCellValueFactory(new PropertyValueFactory[Entry, String]("date"))

  def loadLeaderboard(): Unit =
    try {
      val scores = Database.getTopScores(10)
      val tableData: ObservableList[Entry] = FXCollections.observableArrayList()

      scores.zipWithIndex.foreach { case ((playerName, score, gameTime, playDate), index) =>
        // Format the date string (remove timestamp details if needed)
        val formattedDate = playDate.split(" ")(0) // Just get date part
        tableData.add(new Entry(index + 1, playerName, score, formattedDate))
      }

      leaderboardTable.setItems(tableData)

    } catch {
      case e: Exception =>
        println(s"Error loading leaderboard: ${e.getMessage}")
        // Show empty table on error
        leaderboardTable.setItems(FXCollections.observableArrayList())
    }

  @FXML
  def handleBackToMenu(): Unit =
    MainApp.showMainMenu()

  @FXML
  def handleClearLeaderboard(): Unit =
    val alert = new Alert(Alert.AlertType.CONFIRMATION)
    alert.initOwner(MainApp.stage)
    alert.setTitle("Clear Leaderboard")
    alert.setHeaderText("Are you sure you want to clear all scores?")
    alert.setContentText("This action cannot be undone.")

    val result = alert.showAndWait()
    if result.isPresent && result.get() == ButtonType.OK then
      try {
        Database.clearLeaderboard()
        loadLeaderboard() // Refresh the display
      } catch {
        case e: Exception =>
          val errorAlert = new Alert(Alert.AlertType.ERROR)
          errorAlert.initOwner(MainApp.stage)
          errorAlert.setTitle("Error")
          errorAlert.setHeaderText("Failed to clear leaderboard")
          errorAlert.setContentText(s"Error: ${e.getMessage}")
          errorAlert.showAndWait()
      }