package com.practice.refactoring.exercises

/**
 * EXERCISE 06: GLOBAL STATE - Hidden dependencies
 * 
 * Code Smell: Sử dụng singleton/global state, khó test và debug
 * 
 * Design Pattern để fix:
 * - Dependency Injection thay vì Singleton
 * - Scoped instances
 * - Explicit passing of dependencies
 * 
 * TODO: Chuyển từ global singletons sang injected dependencies
 */
object GlobalConfig {
  // Mutable global state - SMELL!
  var databaseUrl: String = "jdbc:mysql://localhost:3306/prod"
  var apiKey: String = "secret-api-key"
  var maxRetries: Int = 3
  var debugMode: Boolean = false
  var currentEnvironment: String = "production"
}

object UserCache {
  // Global mutable cache - SMELL!
  private var cache: Map[String, CachedUser] = Map()
  private var hitCount: Int = 0
  private var missCount: Int = 0
  
  def get(userId: String): Option[CachedUser] = {
    cache.get(userId) match {
      case Some(user) =>
        hitCount += 1
        if (GlobalConfig.debugMode) {
          println(s"Cache HIT for $userId (total hits: $hitCount)")
        }
        Some(user)
      case None =>
        missCount += 1
        if (GlobalConfig.debugMode) {
          println(s"Cache MISS for $userId (total misses: $missCount)")
        }
        None
    }
  }
  
  def put(userId: String, user: CachedUser): Unit = {
    cache = cache.updated(userId, user)
  }
  
  def clear(): Unit = {
    cache = Map()
    hitCount = 0
    missCount = 0
  }
  
  def stats(): String = s"Hits: $hitCount, Misses: $missCount"
}

object Logger {
  // Global logger with side effects - SMELL!
  private var logs: List[String] = List()
  
  def log(level: String, message: String): Unit = {
    val timestamp = java.time.LocalDateTime.now()
    val entry = s"[$timestamp] [$level] $message"
    logs = logs :+ entry
    
    // Global config dependency
    if (GlobalConfig.debugMode || level == "ERROR") {
      println(entry)
    }
  }
  
  def info(message: String): Unit = log("INFO", message)
  def error(message: String): Unit = log("ERROR", message)
  def debug(message: String): Unit = if (GlobalConfig.debugMode) log("DEBUG", message)
  
  def getLogs(): List[String] = logs
  def clearLogs(): Unit = { logs = List() }
}

// Service that depends on multiple globals
class OrderService {
  
  def processOrder(userId: String, items: List[String]): String = {
    Logger.info(s"Processing order for user $userId")
    
    // Check cache first - hidden global dependency
    val user = UserCache.get(userId).getOrElse {
      Logger.debug(s"Fetching user $userId from database")
      // Simulate DB fetch using global config
      val dbUser = fetchFromDatabase(userId)
      UserCache.put(userId, dbUser)
      dbUser
    }
    
    // Use global config for retries
    var attempts = 0
    var success = false
    
    while (attempts < GlobalConfig.maxRetries && !success) {
      attempts += 1
      Logger.debug(s"Attempt $attempts of ${GlobalConfig.maxRetries}")
      
      // Simulate order processing
      if (scala.util.Random.nextDouble() > 0.3) {
        success = true
      } else {
        Logger.info(s"Attempt $attempts failed, retrying...")
      }
    }
    
    if (success) {
      Logger.info(s"Order processed successfully for ${user.name}")
      s"Order confirmed for ${user.name}"
    } else {
      Logger.error(s"Order failed after ${GlobalConfig.maxRetries} attempts")
      "Order failed"
    }
  }
  
  private def fetchFromDatabase(userId: String): CachedUser = {
    // Uses global config for database URL
    Logger.debug(s"Connecting to ${GlobalConfig.databaseUrl}")
    CachedUser(userId, s"User_$userId", s"user_$userId@email.com")
  }
}

case class CachedUser(id: String, name: String, email: String)

// Vấn đề:
// 1. Không thể test OrderService một cách isolated
// 2. Tests affect each other qua shared global state
// 3. Configuration không rõ ràng - ẩn trong objects
// 4. Khó chạy parallel tests
// 5. Production code có thể bị ảnh hưởng bởi test code modify globals
