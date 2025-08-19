package nutriquest.model

import scalafx.scene.paint.Color

trait Drawable:
  def getColor: Color

  def getSymbol: String

  def getPosition: Position

trait Movable:
  def move(direction: Position): Unit

  def canMoveTo(position: Position, gridSize: Int): Boolean

trait Collectible: 
  def collect(): Unit
  
  def isCollected: Boolean