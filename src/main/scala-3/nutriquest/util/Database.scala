package nutriquest.util

import scalikejdbc.*

trait Database :
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB;create=true"
  // initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "me", "mine")
  // ad-hoc session provider on the REPL
  given AutoSession = AutoSession

object Database extends Database:

  // Initialize database tables
  def initializeDatabase(): Unit = {
    try {
      // Create leaderboard table if it doesn't exist
      sql"""
            CREATE TABLE leaderboard (
              id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
              player_name VARCHAR(50) NOT NULL,
              score INTEGER NOT NULL,
              game_time DOUBLE NOT NULL,
              play_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
              PRIMARY KEY (id)
            )
          """.execute.apply()
      println("Leaderboard table created successfully")
    } catch {
      case e: Exception if e.getMessage.contains("already exists") =>
        println("Leaderboard table already exists")
      case e: Exception =>
        println(s"Error creating table: ${e.getMessage}")
        throw e
    }
  }

  // Save a game score to leaderboard
  def saveScore(playerName: String, score: Int, gameTime: Double): Unit = {
    try {
      sql"""
        INSERT INTO leaderboard (player_name, score, game_time)
        VALUES (${playerName}, ${score}, ${gameTime})
      """.update.apply()
      println(s"Score saved to database: $playerName - $score points")
    } catch {
      case e: Exception =>
        println(s"Error saving score: ${e.getMessage}")
        throw e
    }
  }

  // Get top scores (limit to top 10)
  def getTopScores(limit: Int = 10): List[(String, Int, Double, String)] = {
    try {
      val results = sql"""
        SELECT player_name, score, game_time, play_date
        FROM leaderboard
        ORDER BY score DESC, game_time ASC
        FETCH FIRST ${limit} ROWS ONLY
      """.map(rs => (
        rs.string("player_name"),
        rs.int("score"),
        rs.double("game_time"),
        rs.timestamp("play_date").toString
      )).list.apply()

      println(s"Retrieved ${results.size} scores from database")
      results
    } catch {
      case e: Exception =>
        println(s"Error retrieving scores: ${e.getMessage}")
        List.empty
    }
  }

  // Get all scores for a specific player
  def getPlayerScores(playerName: String): List[(Int, Double, String)] = {
    sql"""
      SELECT score, game_time, play_date
      FROM leaderboard
      WHERE player_name = ${playerName}
      ORDER BY score DESC
    """.map(rs => (
      rs.int("score"),
      rs.double("game_time"),
      rs.timestamp("play_date").toString
    )).list.apply()
  }

  // Clear all scores from leaderboard
  def clearLeaderboard(): Unit = {
    sql"DELETE FROM leaderboard".update.apply()
  }

  // Check if table exists (helper method)
  def tableExists(tableName: String): Boolean = {
    try {
      sql"SELECT COUNT(*) FROM ${tableName}".map(_.int(1)).single.apply()
      true
    } catch {
      case _: Exception => false
    }
  }