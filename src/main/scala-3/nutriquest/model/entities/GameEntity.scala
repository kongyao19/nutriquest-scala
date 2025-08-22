package nutriquest.model.entities

import nutriquest.model.Positionable
import scalafx.scene.image.{Image, ImageView}

abstract class GameEntity(_x: Double, _y: Double, _imagePath: String) extends Positionable:
  // Load the image from the specified path
  private val image = new Image(_imagePath)

  // Create an ImageView with the loaded image
  var imageView = new ImageView(image):
    x = _x
    y = _y
    fitHeight = 50
    fitWidth = 50

  // Returns the x position of the GameEntity
  def posX: Double = imageView.x.toDouble

  // Returns the y position of the GameEntity  
  def posY: Double = imageView.y.toDouble

  // Setter methods for position
  def posX_=(newX: Double): Unit = imageView.x = newX

  def posY_=(newY: Double): Unit = imageView.y = newY