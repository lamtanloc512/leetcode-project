package com.practice.refactoring.exercises

/**
 * EXERCISE 07: PRIMITIVE OBSESSION - Using primitives instead of domain objects
 * 
 * Code Smell: Sử dụng String, Int thay vì tạo proper domain types
 * 
 * Design Pattern để fix:
 * - Value Object Pattern
 * - Tiny Types / Wrapper Types
 * 
 * TODO: Tạo các value objects như Email, PhoneNumber, Money, etc.
 */
class UserRegistration {
  
  // All primitives - no type safety!
  def registerUser(
    firstName: String,
    lastName: String,
    email: String,           // Can be any string
    phone: String,           // Can be any string  
    age: Int,                // Can be negative
    country: String,         // Country code or full name?
    postalCode: String,      // Format varies by country
    creditCardNumber: String, // 16 digits? Who knows!
    cvv: String,
    cardExpiryMonth: Int,
    cardExpiryYear: Int
  ): Either[String, String] = {
    
    // Manual validation everywhere - smell!
    
    // Validate email
    if (email == null || email.isEmpty) {
      return Left("Email is required")
    }
    if (!email.contains("@") || !email.contains(".")) {
      return Left("Invalid email format")
    }
    
    // Validate phone - but what format?
    if (phone.length < 10 || phone.length > 15) {
      return Left("Invalid phone number")
    }
    if (!phone.forall(c => c.isDigit || c == '+' || c == '-' || c == ' ')) {
      return Left("Phone can only contain digits and +/-/space")
    }
    
    // Validate age
    if (age < 0 || age > 150) {
      return Left("Invalid age")
    }
    if (age < 18) {
      return Left("Must be 18 or older")
    }
    
    // Validate credit card
    val cleanCardNumber = creditCardNumber.replaceAll("\\s", "")
    if (cleanCardNumber.length != 16) {
      return Left("Credit card must be 16 digits")
    }
    if (!cleanCardNumber.forall(_.isDigit)) {
      return Left("Credit card must contain only digits")
    }
    
    // Validate CVV
    if (cvv.length < 3 || cvv.length > 4) {
      return Left("CVV must be 3 or 4 digits")
    }
    
    // Validate expiry
    if (cardExpiryMonth < 1 || cardExpiryMonth > 12) {
      return Left("Invalid expiry month")
    }
    val currentYear = java.time.Year.now().getValue
    if (cardExpiryYear < currentYear || cardExpiryYear > currentYear + 10) {
      return Left("Invalid expiry year")
    }
    
    // Process registration...
    Right(s"User $firstName $lastName registered with email $email")
  }
}

// Order với toàn primitives - easy to mix up!
class OrderProcessor2 {
  
  def createOrder(
    customerId: String,
    productId: String,
    quantity: Int,
    priceInCents: Long,      // Cents hay dollars? Who knows!
    discountPercent: Double, // 0-100 hay 0-1?
    shippingPriceInCents: Long,
    taxRate: Double,
    currency: String         // USD, VND, EUR - no validation
  ): OrderSummary = {
    
    // Easy to make mistakes with primitives
    val subtotal = quantity * priceInCents
    
    // Is discountPercent 0-100 or 0-1? Hope I guessed right!
    val discount = if (discountPercent > 1) {
      subtotal * discountPercent / 100
    } else {
      subtotal * discountPercent
    }
    
    val afterDiscount = subtotal - discount
    
    // Same problem with tax rate
    val tax = if (taxRate > 1) {
      afterDiscount * taxRate / 100  
    } else {
      afterDiscount * taxRate
    }
    
    val total = afterDiscount + tax + shippingPriceInCents
    
    // Converting cents to display - but what if it was already in dollars?
    val displayTotal = if (currency == "VND") {
      s"${total.toLong} VND"  // VND doesn't have cents
    } else {
      f"${total / 100.0}%.2f $currency"
    }
    
    OrderSummary(customerId, productId, displayTotal)
  }
  
  // Dễ gọi sai vì parameters giống nhau!
  // createOrder(productId, customerId, ...) - compiler không báo lỗi
  // createOrder(customerId, productId, priceInCents, quantity, ...) - sai thứ tự nhưng compile ok
}

case class OrderSummary(customerId: String, productId: String, totalDisplay: String)

// Vấn đề:
// 1. Không có type safety - String có thể là bất kỳ thứ gì
// 2. Validation logic lặp lại ở nhiều nơi
// 3. Dễ nhầm lẫn thứ tự parameters (String, String, Int, Long, Long...)
// 4. Units không rõ ràng (cents vs dollars, percent vs decimal)
// 5. Domain concepts bị ẩn trong primitives
