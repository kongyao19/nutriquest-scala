package nutriquest.model.leaderboard

case class Entry(
                  playerName: String,
                  score: Int,
                  timeStamp: Long = System.currentTimeMillis()
                ):
  def getFormattedDate: String =
    val date = new java.util.Date(timeStamp)
    val formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
    formatter.format(date)