package nutriquest.view

import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.{Alert, ButtonType, TableColumn, TableView}
import nutriquest.MainApp

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

    rankColumn.setPrefWidth(80)
    nameColumn.setPrefWidth(200)
    scoreColumn.setPrefWidth(100)
    dateColumn.setPrefWidth(150)

  def loadLeaderboard(): Unit =
    val entries = MainApp.leaderboardManager.getTopScores(10)
    val tableData: ObservableList[Entry] = FXCollections.observableArrayList()

    entries.zipWithIndex.foreach { case (entry, index) =>
      tableData.add(new Entry(index + 1, entry.playerName, entry.score, entry.getFormattedDate))
    }

    leaderboardTable.setItems(tableData)

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
      MainApp.leaderboardManager.clear()
      loadLeaderboard()