package nutriquest.model.entities

import nutriquest.model.{Collectible, Drawable, Position}
import scalafx.scene.paint.Color

abstract class PowerUp(
                      position: Position,
                      val duration: Int,
                      val name: String
                      ) extends Drawable, Collectible:
  protected var collected: Boolean = false

  def getPosition: Position = position

  def isCollected: Boolean = collected

  def collect(): Unit = collected = true

  def activate(player: Player): Unit

  def deactivate(player: Player): Unit

class SpeedBoost(position: Position) extends PowerUp(position, 5, "Speed Boost"):
  def getColor: Color = Color.Yellow

  def getSymbol: String = "⚡"

  def activate(player: Player): Unit = player.activateSpeedBoost()

  def deactivate(player: Player): Unit = player.deactivateSpeedBoost()

class ScoreMultiplier(position: Position) extends PowerUp(position, 10, "2x Score"):
  def getColor: Color = Color.Gold

  def getSymbol: String = "✨"

  def activate(player: Player): Unit = player.activateScoreMultiplier()

  def deactivate(player: Player): Unit = player.deactivateScoreMultiplier()