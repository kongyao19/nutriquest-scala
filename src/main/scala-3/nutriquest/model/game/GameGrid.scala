package nutriquest.model.game

import nutriquest.model.{Drawable, Position}

class GameGrid(val size: Int):
  private val grid: Array[Array[Option[Drawable]]] = Array.fill(size, size)(None)

  def placeItem(item: Drawable): Boolean =
    val pos = item.getPosition
    if pos.isValidOn(size) && isEmpty(pos) then
      grid(pos.y)(pos.x) = Some(item)
      true
    else false

  def removeItem(position: Position): Unit =
    if position.isValidOn(size) then 
      grid(position.y)(position.x) = None
      
  def getItem(position: Position): Option[Drawable] =
    if position.isValidOn(size) then 
      grid(position.y)(position.x) else None
      
  def isEmpty(position: Position): Boolean =
    getItem(position).isEmpty
    
  def getAllItems: List[Drawable] =
    grid.flatten.flatten.toList
    
  def clear(): Unit =
    for i <- grid.indices; j <- grid(i).indices do 
      grid(i)(j) = None