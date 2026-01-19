package com.practice.refactoring.exercises

/**
 * EXERCISE 10: CONDITIONAL COMPLEXITY - Complex nested conditionals
 * 
 * Code Smell: Deep nested if/else, complex state transitions
 * 
 * Design Pattern để fix:
 * - State Pattern
 * - Command Pattern
 * - Replace Conditional with Polymorphism
 * 
 * TODO: Sử dụng State pattern để quản lý order states
 */
class OrderProcessor3 {
  
  // Complex conditional logic for order state transitions
  def processOrderAction(order: OrderData, action: String): Either[String, OrderData] = {
    // Nested conditionals nightmare!
    
    if (order.status == "DRAFT") {
      if (action == "SUBMIT") {
        if (order.items.nonEmpty) {
          if (order.paymentMethod != null) {
            Right(order.copy(status = "PENDING_PAYMENT"))
          } else {
            Left("Payment method required to submit order")
          }
        } else {
          Left("Cannot submit empty order")
        }
      } else if (action == "DELETE") {
        Right(order.copy(status = "DELETED"))
      } else if (action == "ADD_ITEM") {
        // Handle add item...
        Right(order)
      } else {
        Left(s"Invalid action $action for DRAFT order")
      }
      
    } else if (order.status == "PENDING_PAYMENT") {
      if (action == "PAY") {
        if (order.paymentMethod != null) {
          // Process payment...
          if (validatePayment(order)) {
            Right(order.copy(status = "PAID"))
          } else {
            Right(order.copy(status = "PAYMENT_FAILED"))
          }
        } else {
          Left("No payment method")
        }
      } else if (action == "CANCEL") {
        Right(order.copy(status = "CANCELLED"))
      } else if (action == "UPDATE_PAYMENT") {
        // Update payment method...
        Right(order)
      } else {
        Left(s"Invalid action $action for PENDING_PAYMENT order")
      }
      
    } else if (order.status == "PAID") {
      if (action == "SHIP") {
        if (order.shippingAddress != null) {
          Right(order.copy(status = "SHIPPED"))
        } else {
          Left("Shipping address required")
        }
      } else if (action == "REFUND") {
        // Initiate refund...
        Right(order.copy(status = "REFUND_PENDING"))
      } else if (action == "HOLD") {
        Right(order.copy(status = "ON_HOLD"))
      } else {
        Left(s"Invalid action $action for PAID order")
      }
      
    } else if (order.status == "SHIPPED") {
      if (action == "DELIVER") {
        Right(order.copy(status = "DELIVERED"))
      } else if (action == "RETURN") {
        if (isWithinReturnWindow(order)) {
          Right(order.copy(status = "RETURN_REQUESTED"))
        } else {
          Left("Return window expired")
        }
      } else if (action == "LOST") {
        Right(order.copy(status = "LOST_IN_TRANSIT"))
      } else {
        Left(s"Invalid action $action for SHIPPED order")
      }
      
    } else if (order.status == "DELIVERED") {
      if (action == "RETURN") {
        if (isWithinReturnWindow(order)) {
          Right(order.copy(status = "RETURN_REQUESTED"))
        } else {
          Left("Return window expired")
        }
      } else if (action == "COMPLETE") {
        Right(order.copy(status = "COMPLETED"))
      } else {
        Left(s"Invalid action $action for DELIVERED order")
      }
      
    } else if (order.status == "PAYMENT_FAILED") {
      if (action == "RETRY_PAYMENT") {
        Right(order.copy(status = "PENDING_PAYMENT"))
      } else if (action == "CANCEL") {
        Right(order.copy(status = "CANCELLED"))
      } else {
        Left(s"Invalid action $action for PAYMENT_FAILED order")
      }
      
    } else if (order.status == "ON_HOLD") {
      if (action == "RELEASE") {
        Right(order.copy(status = "PAID"))
      } else if (action == "CANCEL") {
        Right(order.copy(status = "CANCELLED"))
      } else {
        Left(s"Invalid action $action for ON_HOLD order")
      }
      
    } else if (order.status == "RETURN_REQUESTED") {
      if (action == "APPROVE_RETURN") {
        Right(order.copy(status = "RETURN_APPROVED"))
      } else if (action == "REJECT_RETURN") {
        Right(order.copy(status = "DELIVERED"))
      } else {
        Left(s"Invalid action $action for RETURN_REQUESTED order")
      }
      
    } else if (order.status == "RETURN_APPROVED") {
      if (action == "RECEIVE_RETURN") {
        Right(order.copy(status = "RETURNED"))
      } else {
        Left(s"Invalid action $action for RETURN_APPROVED order")
      }
      
    } else if (order.status == "RETURNED") {
      if (action == "REFUND") {
        Right(order.copy(status = "REFUNDED"))
      } else {
        Left(s"Invalid action $action for RETURNED order")
      }
      
    } else if (order.status == "CANCELLED" || order.status == "DELETED" ||
               order.status == "COMPLETED" || order.status == "REFUNDED") {
      // Terminal states - no actions allowed
      Left(s"Order is in terminal state ${order.status}, no actions allowed")
      
    } else {
      Left(s"Unknown order status: ${order.status}")
    }
  }
  
  private def validatePayment(order: OrderData): Boolean = {
    scala.util.Random.nextDouble() > 0.2
  }
  
  private def isWithinReturnWindow(order: OrderData): Boolean = {
    true // Simplified
  }
  
  // Tương tự cho các operations khác - cùng pattern lặp lại!
  def getAvailableActions(order: OrderData): List[String] = {
    order.status match {
      case "DRAFT" => List("SUBMIT", "DELETE", "ADD_ITEM")
      case "PENDING_PAYMENT" => List("PAY", "CANCEL", "UPDATE_PAYMENT")
      case "PAID" => List("SHIP", "REFUND", "HOLD")
      case "SHIPPED" => List("DELIVER", "RETURN", "LOST")
      case "DELIVERED" => List("RETURN", "COMPLETE")
      case "PAYMENT_FAILED" => List("RETRY_PAYMENT", "CANCEL")
      case "ON_HOLD" => List("RELEASE", "CANCEL")
      case "RETURN_REQUESTED" => List("APPROVE_RETURN", "REJECT_RETURN")
      case "RETURN_APPROVED" => List("RECEIVE_RETURN")
      case "RETURNED" => List("REFUND")
      case _ => List()
    }
  }
}

case class OrderData(
  id: String,
  status: String,
  items: List[String],
  paymentMethod: String,
  shippingAddress: String
)

// Vấn đề:
// 1. Quá nhiều nested conditionals - cyclomatic complexity cao
// 2. State transitions không rõ ràng
// 3. Dễ quên case khi thêm status mới
// 4. Logic lặp lại ở nhiều methods (getAvailableActions)
// 5. Không thể extend behaviors mà không modify code
// 6. Testing cực kỳ khó khăn
