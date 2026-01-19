package com.practice.refactoring.exercises

import com.practice.refactoring.exercises.InventoryRepository.apply
import scala.util.Try

/** EXERCISE 01: GOD CLASS - Quá nhiều responsibilities
  *
  * Code Smell: Class này làm quá nhiều việc - xử lý order, payment,
  * notification, inventory
  *
  * Design Pattern để fix:
  *   - Single Responsibility Principle (SRP)
  *   - Extract Class
  *
  * TODO: Tách thành các class riêng biệt:
  *   - OrderProcessor
  *   - PaymentProcessor
  *   - NotificationService
  *   - InventoryManager
  */
class OrderManager {

  private var orders: List[Order] = List()
  private var inventory: Map[String, Int] = Map()
  private var customerEmails: Map[String, String] = Map()

  // Order processing - responsibility 1
  def createOrder(customerId: String, items: List[OrderItem]): Order = {
    val order = Order(
      id = java.util.UUID.randomUUID().toString,
      customerId = customerId,
      items = items,
      status = "PENDING"
    )
    orders = orders :+ order

    // Kiểm tra inventory - responsibility 2
    for (item <- items) {
      val currentStock = inventory.getOrElse(item.productId, 0)
      if (currentStock < item.quantity) {
        throw new RuntimeException(s"Not enough stock for ${item.productId}")
      }
      inventory =
        inventory.updated(item.productId, currentStock - item.quantity)
    }

    // Process payment - responsibility 3
    val totalAmount = items.map(i => i.price * i.quantity).sum
    if (!processPayment(customerId, totalAmount)) {
      // Rollback inventory
      for (item <- items) {
        val currentStock = inventory.getOrElse(item.productId, 0)
        inventory =
          inventory.updated(item.productId, currentStock + item.quantity)
      }
      throw new RuntimeException("Payment failed")
    }

    // Send notification - responsibility 4
    val email = customerEmails.getOrElse(customerId, "")
    if (email.nonEmpty) {
      sendEmail(
        email,
        s"Order ${order.id} confirmed!",
        buildOrderEmailBody(order)
      )
    }

    // Update order status
    val updatedOrder = order.copy(status = "CONFIRMED")
    orders = orders.map(o => if (o.id == order.id) updatedOrder else o)

    updatedOrder
  }

  // Payment logic mixed in - smell!
  private def processPayment(customerId: String, amount: Double): Boolean = {
    // Hardcoded payment gateway logic
    println(s"Processing payment of $amount for customer $customerId")
    if (amount > 10000) {
      // Fraud check - another responsibility!
      println("High value order - running fraud check...")
      Thread.sleep(1000)
    }
    // Simulate payment success
    scala.util.Random.nextDouble() > 0.1
  }

  // Email logic mixed in - smell!
  private def sendEmail(to: String, subject: String, body: String): Unit = {
    // Hardcoded email logic
    println(s"Sending email to $to")
    println(s"Subject: $subject")
    println(s"Body: $body")
  }

  private def buildOrderEmailBody(order: Order): String = {
    val itemsStr = order.items
      .map(i => s"  - ${i.productId}: ${i.quantity} x ${i.price}")
      .mkString("\n")
    s"""
    |Dear Customer,
    |
    |Your order ${order.id} has been confirmed!
    |
    |Items:
    |$itemsStr
    |
    |Thank you for shopping with us!
    """.stripMargin
  }

  // Inventory management - another responsibility!
  def addStock(productId: String, quantity: Int): Unit = {
    val currentStock = inventory.getOrElse(productId, 0)
    inventory = inventory.updated(productId, currentStock + quantity)
  }

  def getStock(productId: String): Int = {
    inventory.getOrElse(productId, 0)
  }

  // Customer management - yet another responsibility!
  def registerCustomer(customerId: String, email: String): Unit = {
    customerEmails = customerEmails.updated(customerId, email)
  }

  // Reporting - responsibility overload!
  def generateDailyReport(): String = {
    val confirmedOrders = orders.filter(_.status == "CONFIRMED")
    val totalRevenue =
      confirmedOrders.flatMap(_.items).map(i => i.price * i.quantity).sum
    s"Daily Report: ${confirmedOrders.size} orders, Revenue: $totalRevenue"
  }
}

/// start


case class Order(
    id: String,
    customerId: String,
    items: List[OrderItem],
    status: String
)
case class OrderItem(productId: String, quantity: Int, price: Double)

case class Inventory(id: String)
case class Email(id: String)

// Start

trait Repository[T] {
  def findById(id: String): Option[T]
  def save(t: T): Unit
}
class OrderRepository extends Repository[Order] {
  val repo: List[Order] = List()
  override def findById(id: String): Option[Order] =
    repo.find(order => order.id == id)

  override def save(order: Order): Unit = repo :+ order;
}

object OrderRepository {
  def apply() = new OrderRepository
}

class InventoryRepository {
  private var repo: Map[String, Int] = Map()

  def findById(id: String): Option[Int] = repo.get(id)

  def save(productId: String, stock: Int): Unit = {
    repo = repo.updated(productId, stock)
  }

  def update(productId: String, stock: Int): Unit = {
    repo = repo.updated(productId, stock)
  }

  def delete(productId: String): Unit = {
    repo = repo - productId
  }

  def getStock(productId: String): Int = repo.getOrElse(productId, 0)
}

object InventoryRepository {
  def apply() = new InventoryRepository
}

class EmailRepository extends Repository[Email] {
  val repo: List[Email] = List()
  override def findById(id: String): Option[Email] =
    repo.find(inv => inv.id == id)
  override def save(email: Email): Unit = repo :+ email;
}

object EmailRepository {
  def apply() = new EmailRepository
}

class OrderProcessor {
  private val orderRepository = OrderRepository()

  def createOrder(customerId: String, items: List[OrderItem]): Order = {
    val order = Order(
      id = java.util.UUID.randomUUID().toString,
      customerId = customerId,
      items = items,
      status = "PENDING"
    )
    orderRepository.save(order)
    order
  }
}

class PaymentProcessor {

  def processPayment(customerId : String, items: List[OrderItem]): Either[PaymentException, Unit] = {
    val result = Try {
      val totalAmount = items.map(item => item.price * item.quantity).sum
      // Hardcoded payment gateway logic
      println(s"Processing payment of $totalAmount for customer $customerId")
      if (totalAmount > 10000) {
        // Fraud check - another responsibility!
        println("High value order - running fraud check...")
        Thread.sleep(1000)
      }
      // Simulate payment success
      scala.util.Random.nextDouble() > 0.1
    }.toEither
    Right(())
  }
}

class NotificationService {
  private val emailRepository = EmailRepository()
}

class InventoryManager {
  private val inventoryRepository = InventoryRepository()

  def checkInventory(items: List[OrderItem]) = {
    for (item <- items) {
      val currentStock = inventoryRepository.getStock(item.productId)
      if (currentStock < item.quantity) {
        throw new RuntimeException(s"Not enough stock for ${item.productId}")
      }
      inventoryRepository.update(item.productId, currentStock - item.quantity)
    }
  }

  def rollback(items: List[OrderItem]): Either[PaymentException, Nothing] = {
    for (item <- items) {
      val currentStock = inventoryRepository.getStock(item.productId)
      inventoryRepository.update(item.productId, currentStock + item.quantity)
    }
    Left(PaymentException())
  }
}

class PaymentException extends RuntimeException {
  override def getMessage(): String = s"Payment failed!"
}

object PaymentException {
  def apply() = new PaymentException()
}

object InventoryManager {
  def apply() = new InventoryManager
}
