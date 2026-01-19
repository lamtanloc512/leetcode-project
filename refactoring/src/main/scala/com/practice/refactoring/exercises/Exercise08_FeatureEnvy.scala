package com.practice.refactoring.exercises

/**
 * EXERCISE 08: FEATURE ENVY - Methods that use other class's data more than their own
 * 
 * Code Smell: Method ở class A nhưng chủ yếu truy cập data của class B
 * 
 * Design Pattern để fix:
 * - Move Method
 * - Encapsulate Collection
 * - Tell Don't Ask
 * 
 * TODO: Di chuyển methods về class có data tương ứng
 */
class InvoiceCalculator {
  
  // This method belongs in Customer class - it uses customer data exclusively!
  def calculateCustomerDiscount(customer: CustomerData): Double = {
    var discount = 0.0
    
    // Accessing customer's membership level
    if (customer.membershipLevel == "PLATINUM") {
      discount += 0.20
    } else if (customer.membershipLevel == "GOLD") {
      discount += 0.15
    } else if (customer.membershipLevel == "SILVER") {
      discount += 0.10
    }
    
    // Accessing customer's purchase history
    val totalPurchases = customer.purchaseHistory.map(_.amount).sum
    if (totalPurchases > 10000) {
      discount += 0.05
    } else if (totalPurchases > 5000) {
      discount += 0.03
    }
    
    // Accessing customer's registration date
    val yearsAsCustomer = java.time.Year.now().getValue - customer.registrationYear
    if (yearsAsCustomer > 5) {
      discount += 0.05
    } else if (yearsAsCustomer > 2) {
      discount += 0.02
    }
    
    // Accessing customer's referral count
    if (customer.referralCount > 10) {
      discount += 0.03
    }
    
    Math.min(discount, 0.30) // Cap at 30%
  }
  
  // This method should be in ShippingAddress class!
  def formatShippingLabel(address: ShippingAddress): String = {
    val sb = new StringBuilder()
    
    sb.append(address.recipientName.toUpperCase)
    sb.append("\n")
    
    if (address.companyName != null && address.companyName.nonEmpty) {
      sb.append(address.companyName)
      sb.append("\n")
    }
    
    sb.append(address.streetLine1)
    sb.append("\n")
    
    if (address.streetLine2 != null && address.streetLine2.nonEmpty) {
      sb.append(address.streetLine2)
      sb.append("\n")
    }
    
    sb.append(s"${address.city}, ${address.state} ${address.postalCode}")
    sb.append("\n")
    sb.append(address.country.toUpperCase)
    
    sb.toString()
  }
  
  // This method should be in OrderItem class!
  def calculateItemTotal(item: OrderItemData): Double = {
    var total = item.unitPrice * item.quantity
    
    // Apply item-specific discount
    if (item.discountPercent > 0) {
      total = total * (1 - item.discountPercent / 100)
    }
    
    // Apply bulk discount
    if (item.quantity >= item.bulkThreshold) {
      total = total * (1 - item.bulkDiscountPercent / 100)
    }
    
    // Add tax based on item category
    val taxRate = item.category match {
      case "FOOD" => 0.0
      case "ELECTRONICS" => 0.10
      case "LUXURY" => 0.20
      case _ => 0.08
    }
    
    total * (1 + taxRate)
  }
  
  // This logic should be in Product class!
  def isProductAvailable(product: ProductData): Boolean = {
    if (product.stockQuantity <= 0) {
      return false
    }
    
    if (product.status != "ACTIVE") {
      return false
    }
    
    if (product.expiryDate != null) {
      val today = java.time.LocalDate.now()
      if (product.expiryDate.isBefore(today)) {
        return false
      }
    }
    
    if (product.minOrderQuantity > product.stockQuantity) {
      return false
    }
    
    true
  }
}

// Data classes
case class CustomerData(
  membershipLevel: String,
  purchaseHistory: List[PurchaseRecord],
  registrationYear: Int,
  referralCount: Int
)

case class PurchaseRecord(date: String, amount: Double)

case class ShippingAddress(
  recipientName: String,
  companyName: String,
  streetLine1: String,
  streetLine2: String,
  city: String,
  state: String,
  postalCode: String,
  country: String
)

case class OrderItemData(
  unitPrice: Double,
  quantity: Int,
  discountPercent: Double,
  bulkThreshold: Int,
  bulkDiscountPercent: Double,
  category: String
)

case class ProductData(
  stockQuantity: Int,
  status: String,
  expiryDate: java.time.LocalDate,
  minOrderQuantity: Int
)

// Vấn đề:
// 1. InvoiceCalculator biết quá nhiều về internal structure của Customer
// 2. Logic nên thuộc về data class nhưng lại nằm ở class khác
// 3. Khi Customer thay đổi, InvoiceCalculator phải sửa theo
// 4. Vi phạm "Tell, Don't Ask" principle
// 5. Khó test và maintain
