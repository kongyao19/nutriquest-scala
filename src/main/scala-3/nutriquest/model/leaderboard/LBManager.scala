package nutriquest.model.leaderboard

import scala.collection.mutable.ListBuffer

class LBManager:
  private val leaderboard: ListBuffer[Entry] = ListBuffer.empty
  private val maxEntries = 10

  def addScore(playerName: String, score: Int): Unit =
    val entry = Entry(playerName, score)
    leaderboard += entry

    // Sort by score (descending) and keep only top entries
    val sortedEntries = leaderboard.sortBy(-_.score).take(maxEntries)
    leaderboard.clear()
    leaderboard ++= sortedEntries

  def getAllScores: List[Entry] =
    leaderboard.sortBy(-_.score).toList

  def getTopScores(limit: Int = maxEntries): List[Entry] =
    leaderboard.sortBy(-_.score).take(limit).toList

  def isHighScore(score: Int): Boolean =
    leaderboard.size < maxEntries || score > leaderboard.map(_.score).min

  def getRank(score: Int): Int =
    val betterScores = leaderboard.count(_.score > score)
    betterScores + 1

  def clear(): Unit =
    leaderboard.clear()

object LBManager:
  def apply(): LBManager = new LBManager()