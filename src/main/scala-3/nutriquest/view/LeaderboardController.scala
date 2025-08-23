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

    // Customize the empty table message
    val placeholderLabel = new javafx.scene.control.Label("🏆 No champions yet! 🏆\nBe the first to claim victory!")
    placeholderLabel.setStyle("-fx-text-alignment: center; -fx-font-size: 14px; -fx-text-fill: gray;")
    leaderboardTable.setPlaceholder(placeholderLabel)

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

      // Resize table to fit the actual data
      resizeTableToFitData(tableData.size())

    } catch {
      case e: Exception =>
        println(s"Error loading leaderboard: ${e.getMessage}")
        // Show empty table on error
        leaderboardTable.setItems(FXCollections.observableArrayList())
        resizeTableToFitData(0)
    }

  private def resizeTableToFitData(itemCount: Int): Unit =
    if itemCount > 0 then
      // Calculate height needed: header + (number of rows * row height) + padding
      val headerHeight = 28.0
      val rowHeight = 30.0 // This matches fixedCellSize in FXML
      val padding = 2.0

      val neededHeight = headerHeight + (itemCount * rowHeight) + padding

      // Set a reasonable max height to prevent it from getting too tall
      val maxHeight = 350.0
      val minHeight = 120.0 // Minimum height to show at least header + 2 rows

      val finalHeight = math.max(minHeight, math.min(neededHeight, maxHeight))

      // Apply the calculated height
      leaderboardTable.setPrefHeight(finalHeight)
      leaderboardTable.setMaxHeight(finalHeight)
      leaderboardTable.setMinHeight(finalHeight)

      println(s"Resized table to fit $itemCount rows: height = $finalHeight")
    else
      // If no data, set a minimal height to show "No content in table" message
      val emptyTableHeight = 100.0
      leaderboardTable.setPrefHeight(emptyTableHeight)
      leaderboardTable.setMaxHeight(emptyTableHeight)
      leaderboardTable.setMinHeight(emptyTableHeight)
      println("No data - set minimal table height")

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