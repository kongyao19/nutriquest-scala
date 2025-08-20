package nutriquest.model.game

import nutriquest.model.{Direction, FoodFactory, Position}
import nutriquest.model.entities.{Food, HealthyFood, Player, PowerUp, ScoreMultiplier, SpeedBoost}
import scalafx.scene.input.KeyCode

import scala.collection.mutable.ListBuffer
import scala.util.Random

class GameState(gridSize: Int = 20):
  private val grid = GameGrid(gridSize)
  private val player = Player(Position(gridSize / 2, gridSize / 2))
  private val random = Random()
  private val foods: ListBuffer[Food] = ListBuffer()
  private val powerUps: ListBuffer[PowerUp] = ListBuffer()
  private var gameRunning: Boolean = false
  private var gameTime: Long = 0
  private val maxGameTime: Long = 120000

  def startGame(): Unit =
    gameRunning = true
    gameTime = System.currentTimeMillis()
    grid.clear()
    foods.clear()
    powerUps.clear()
    grid.placeItem(player)
    generateFoodItems(30)
    generatePowerUps(5)

  def endGame(): Unit =
    gameRunning = false
    println("Game Over!")

  private def generateFoodItems(count: Int): Unit =
    val foodFactory = FoodFactory()
    for _ <- 0 until count do
      val food = foodFactory.createRandomFood(getRandomPosition())
      foods += food
      grid.placeItem(food)

  private def generatePowerUps(count: Int): Unit =
    for _ <- 0 until count do
      val powerUp = if random.nextBoolean() then 
        SpeedBoost(getRandomPosition())
      else 
        ScoreMultiplier(getRandomPosition())
      powerUps += powerUp
      grid.placeItem(powerUp)
      
  private def getRandomPosition(): Position =
    var pos: Position = Position(random.nextInt(gridSize), random.nextInt(gridSize))
    while !grid.isEmpty(pos) do 
      pos = Position(random.nextInt(gridSize), random.nextInt(gridSize))
    pos
    
  def movePlayer(keyCode: KeyCode): Unit = 
    if !gameRunning then return
    val direction = keyCode match
      case KeyCode.W => Some(Direction.up)
      case KeyCode.S => Some(Direction.down)
      case KeyCode.A => Some(Direction.left)
      case KeyCode.D => Some(Direction.right)
      case _ => None
  
    direction.foreach {dir =>
      val newPosition = player.getPosition + dir
      if player.canMoveTo(newPosition, gridSize) then 
        grid.removeItem(player.getPosition)
        player.move(dir)
        checkCollisions()
        grid.placeItem(player)
        checkGameStatus()
    }
    
  private def checkCollisions(): Unit =
    val playerPos = player.getPosition
    foods.find(food => food.getPosition == playerPos && !food.isCollected).foreach {food =>
      food.onCollect(player)
      grid.removeItem(food.getPosition)
    }
    powerUps.find(powerUp => powerUp.getPosition == playerPos && !powerUp.isCollected).foreach {powerUp =>
      powerUp.collect()
      powerUp.activate(player)
      grid.removeItem(powerUp.getPosition)
    }
    
  private def checkGameStatus(): Unit =
    val currentTime = System.currentTimeMillis()
    val timeElapsed = currentTime - gameTime
    if timeElapsed >= maxGameTime then {
      endGame()
      return
    }
    val healthyFoodsRemaining = foods.count(food => food.isInstanceOf[HealthyFood] && !food.isCollected)
    if healthyFoodsRemaining == 0 then {
      endGame()
      return
    }
    
  def getPlayer: Player = player
  
  def getGrid: GameGrid = grid
  
  def getAllFoods: List[Food] = foods.toList
  
  def getAllPowerUps: List[PowerUp] = powerUps.toList
  
  def isGameRunning: Boolean = gameRunning
  
  def getRemainingTime: Long = math.max(0, maxGameTime - (System.currentTimeMillis() - gameTime))
    
        