package com.practice.refactoring.solutions

import scala.util.Try

/**
 * SOLUTION: Exercise 01 - God Class Refactoring
 *
 * Original Problem: OrderManager class had too many responsibilities
 *   - Order processing
 *   - Payment processing
 *   - Fraud detection
 *   - Notification/Email
 *   - Inventory management
 *   - Customer management
 *   - Reporting
 *
 * Applied Patterns:
 *   - Single Responsibility Principle (SRP)
 *   - Dependency Injection
 *   - Repository Pattern
 */

// ============================================================================
// Domain Models
// ============================================================================

case class Order(
    id: String,
    customerId: String,
    items: List[OrderItem],
    status: String
)

case class OrderItem(productId: String, quantity: Int, price: Double)

case class Customer(id: String, email: String)

case class PaymentResult(success: Boolean, transactionId: Option[String] = None)

// ============================================================================
// Exceptions
// ============================================================================

sealed trait OrderError extends Throwable {
  def message: String
  override def getMessage: String = message
}

case class InsufficientStockException(productId: String, requested: Int, available: Int) extends OrderError {
  override val message: String = s"Not enough stock for $productId: requested $requested, available $available"
}

case class PaymentFailedException(reason: String) extends OrderError {
  override val message: String = s"Payment failed: $reason"
}

case class FraudDetectedException(customerId: String, amount: Double) extends OrderError {
  override val message: String = s"Fraud detected for customer $customerId with amount $amount"
}

// ============================================================================
// Repositories - Data Access Layer
// ============================================================================

trait Repository[T, ID] {
  def findById(id: ID): Option[T]
  def save(entity: T): Unit
  def delete(id: ID): Unit
}

class OrderRepository extends Repository[Order, String] {
  private var orders: List[Order] = List()

  override def findById(id: String): Option[Order] =
    orders.find(_.id == id)

  override def save(order: Order): Unit = {
    orders = orders.filterNot(_.id == order.id) :+ order
  }

  override def delete(id: String): Unit = {
    orders = orders.filterNot(_.id == id)
  }

  def findAll(): List[Order] = orders

  def findByStatus(status: String): List[Order] =
    orders.filter(_.status == status)
}

class InventoryRepository {
  private var stock: Map[String, Int] = Map()

  def getStock(productId: String): Int =
    stock.getOrElse(productId, 0)

  def updateStock(productId: String, quantity: Int): Unit = {
    stock = stock.updated(productId, quantity)
  }

  def addStock(productId: String, quantity: Int): Unit = {
    val current = getStock(productId)
    stock = stock.updated(productId, current + quantity)
  }
}

class CustomerRepository extends Repository[Customer, String] {
  private var customers: Map[String, Customer] = Map()

  override def findById(id: String): Option[Customer] =
    customers.get(id)

  override def save(customer: Customer): Unit = {
    customers = customers.updated(customer.id, customer)
  }

  override def delete(id: String): Unit = {
    customers = customers - id
  }
}

// ============================================================================
// Services - Business Logic Layer (Each with Single Responsibility)
// ============================================================================

/**
 * Responsibility: Calculate order pricing
 */
class PricingService {
  def calculateTotal(items: List[OrderItem]): Double =
    items.map(item => item.price * item.quantity).sum
}

/**
 * Responsibility: Manage inventory stock
 */
class InventoryService(repository: InventoryRepository) {

  def checkAndReserveStock(items: List[OrderItem]): Either[InsufficientStockException, Unit] = {
    // First, validate all items have sufficient stock
    for (item <- items) {
      val available = repository.getStock(item.productId)
      if (available < item.quantity) {
        return Left(InsufficientStockException(item.productId, item.quantity, available))
      }
    }

    // Then, reserve stock for all items
    for (item <- items) {
      val current = repository.getStock(item.productId)
      repository.updateStock(item.productId, current - item.quantity)
    }

    Right(())
  }

  def rollbackStock(items: List[OrderItem]): Unit = {
    for (item <- items) {
      val current = repository.getStock(item.productId)
      repository.updateStock(item.productId, current + item.quantity)
    }
  }

  def addStock(productId: String, quantity: Int): Unit =
    repository.addStock(productId, quantity)

  def getStock(productId: String): Int =
    repository.getStock(productId)
}

/**
 * Responsibility: Fraud detection
 */
class FraudCheckService(threshold: Double = 10000.0) {

  def checkForFraud(customerId: String, amount: Double): Either[FraudDetectedException, Unit] = {
    if (amount > threshold) {
      println(s"High value order ($amount) - running fraud check for customer $customerId...")
      // In real implementation: call fraud detection API
      // For demo: always passes
      Right(())
    } else {
      Right(())
    }
  }
}

/**
 * Responsibility: Process payments via gateway
 */
class PaymentService(fraudCheckService: FraudCheckService) {

  def processPayment(customerId: String, amount: Double): Either[OrderError, PaymentResult] = {
    for {
      _ <- fraudCheckService.checkForFraud(customerId, amount)
      result <- chargePayment(customerId, amount)
    } yield result
  }

  private def chargePayment(customerId: String, amount: Double): Either[PaymentFailedException, PaymentResult] = {
    println(s"Processing payment of $amount for customer $customerId")

    // Simulate payment processing
    val success = scala.util.Random.nextDouble() > 0.1

    if (success) {
      Right(PaymentResult(success = true, transactionId = Some(java.util.UUID.randomUUID().toString)))
    } else {
      Left(PaymentFailedException("Transaction declined by payment gateway"))
    }
  }
}

/**
 * Responsibility: Send notifications (email, SMS, etc.)
 */
class NotificationService(customerRepository: CustomerRepository) {

  def sendOrderConfirmation(order: Order): Unit = {
    customerRepository.findById(order.customerId).foreach { customer =>
      if (customer.email.nonEmpty) {
        sendEmail(customer.email, s"Order ${order.id} confirmed!", buildOrderEmailBody(order))
      }
    }
  }

  private def sendEmail(to: String, subject: String, body: String): Unit = {
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
}

/**
 * Responsibility: Generate reports
 */
class ReportingService(orderRepository: OrderRepository, pricingService: PricingService) {

  def generateDailyReport(): String = {
    val confirmedOrders = orderRepository.findByStatus("CONFIRMED")
    val totalRevenue = confirmedOrders
      .flatMap(_.items)
      .map(i => i.price * i.quantity)
      .sum
    s"Daily Report: ${confirmedOrders.size} orders, Revenue: $totalRevenue"
  }
}

// ============================================================================
// Order Processor - Orchestrates the order flow
// ============================================================================

/**
 * OrderProcessor now only orchestrates the order creation flow.
 * All sub-responsibilities are delegated to specialized services.
 */
class OrderProcessor(
    orderRepository: OrderRepository,
    inventoryService: InventoryService,
    paymentService: PaymentService,
    pricingService: PricingService,
    notificationService: NotificationService
) {

  def createOrder(customerId: String, items: List[OrderItem]): Either[OrderError, Order] = {
    val orderId = java.util.UUID.randomUUID().toString
    val totalAmount = pricingService.calculateTotal(items)

    for {
      _ <- inventoryService.checkAndReserveStock(items)
      _ <- processPaymentWithRollback(customerId, totalAmount, items)
      confirmedOrder = Order(orderId, customerId, items, status = "CONFIRMED")
      _ = orderRepository.save(confirmedOrder)
      _ = notificationService.sendOrderConfirmation(confirmedOrder)
    } yield confirmedOrder
  }

  /** Helper: Process payment and rollback inventory on failure */
  private def processPaymentWithRollback(
      customerId: String,
      amount: Double,
      items: List[OrderItem]
  ): Either[OrderError, PaymentResult] = {
    paymentService.processPayment(customerId, amount) match {
      case Left(error) =>
        inventoryService.rollbackStock(items)
        Left(error)
      case success => success
    }
  }
}

// ============================================================================
// Factory - Easy instantiation with wired dependencies
// ============================================================================

object OrderSystem {
  def create(): (OrderProcessor, InventoryService, ReportingService, CustomerRepository) = {
    // Repositories
    val orderRepository = new OrderRepository
    val inventoryRepository = new InventoryRepository
    val customerRepository = new CustomerRepository

    // Services
    val pricingService = new PricingService
    val inventoryService = new InventoryService(inventoryRepository)
    val fraudCheckService = new FraudCheckService()
    val paymentService = new PaymentService(fraudCheckService)
    val notificationService = new NotificationService(customerRepository)
    val reportingService = new ReportingService(orderRepository, pricingService)

    // Processor
    val orderProcessor = new OrderProcessor(
      orderRepository,
      inventoryService,
      paymentService,
      pricingService,
      notificationService
    )

    (orderProcessor, inventoryService, reportingService, customerRepository)
  }
}

// ============================================================================
// Usage Example
// ============================================================================

object OrderSystemDemo extends App {
  val (orderProcessor, inventoryService, reportingService, customerRepository) = OrderSystem.create()

  // Setup: Add stock and register customer
  inventoryService.addStock("LAPTOP-001", 10)
  inventoryService.addStock("MOUSE-001", 50)
  customerRepository.save(Customer("CUST-123", "customer@example.com"))

  // Create order`3we`
  val items = List(
    OrderItem("LAPTOP-001", quantity = 1, price = 1500.0),
    OrderItem("MOUSE-001", quantity = 2, price = 25.0)
  )

  println("=" * 60)
  println("Creating order...")
  println("=" * 60)

  orderProcessor.createOrder("CUST-123", items) match {
    case Right(order) =>
      println(s"\n✅ Order created successfully!")
      println(s"   Order ID: ${order.id}")
      println(s"   Status: ${order.status}")
      println(s"\n📊 ${reportingService.generateDailyReport()}")

    case Left(InsufficientStockException(productId, requested, available)) =>
      println(s"\n❌ Insufficient stock: $productId (requested: $requested, available: $available)")

    case Left(PaymentFailedException(reason)) =>
      println(s"\n❌ Payment failed: $reason")
      println(s"   Stock has been rolled back")

    case Left(FraudDetectedException(customerId, amount)) =>
      println(s"\n🚨 Fraud detected for customer $customerId")
  }
}
