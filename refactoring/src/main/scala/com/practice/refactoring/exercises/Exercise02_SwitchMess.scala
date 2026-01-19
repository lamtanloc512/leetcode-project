package com.practice.refactoring.exercises

/**
 * EXERCISE 02: SWITCH MESS - Giant switch/case statements
 * 
 * Code Smell: Long switch statements that grow with each new type
 * 
 * Design Pattern để fix:
 * - Strategy Pattern
 * - Polymorphism
 * 
 * TODO: Tạo PaymentStrategy trait và các concrete implementations
 */
class PaymentProcessor_Bad {
  
  def processPayment(paymentType: String, amount: Double, details: Map[String, String]): PaymentResult = {
    paymentType match {
      case "CREDIT_CARD" =>
        val cardNumber = details.getOrElse("cardNumber", "")
        val cvv = details.getOrElse("cvv", "")
        val expiry = details.getOrElse("expiry", "")
        
        // Validate credit card
        if (cardNumber.length != 16) {
          return PaymentResult(success = false, "Invalid card number")
        }
        if (cvv.length != 3) {
          return PaymentResult(success = false, "Invalid CVV")
        }
        
        // Process credit card payment
        println(s"Processing credit card payment: $cardNumber")
        // Giả lập call API của bank
        Thread.sleep(100)
        PaymentResult(success = true, s"Credit card payment of $amount processed")
        
      case "PAYPAL" =>
        val email = details.getOrElse("email", "")
        val password = details.getOrElse("password", "")
        
        // Validate PayPal
        if (!email.contains("@")) {
          return PaymentResult(success = false, "Invalid email")
        }
        
        // Process PayPal payment
        println(s"Processing PayPal payment for: $email")
        // Redirect to PayPal...
        Thread.sleep(200)
        PaymentResult(success = true, s"PayPal payment of $amount processed")
        
      case "BANK_TRANSFER" =>
        val accountNumber = details.getOrElse("accountNumber", "")
        val bankCode = details.getOrElse("bankCode", "")
        
        // Validate bank transfer
        if (accountNumber.length < 10) {
          return PaymentResult(success = false, "Invalid account number")
        }
        
        // Process bank transfer (slower)
        println(s"Processing bank transfer to: $accountNumber")
        Thread.sleep(500)
        PaymentResult(success = true, s"Bank transfer of $amount initiated")
        
      case "CRYPTO" =>
        val walletAddress = details.getOrElse("walletAddress", "")
        val cryptoType = details.getOrElse("cryptoType", "BTC")
        
        // Validate crypto
        if (walletAddress.length < 20) {
          return PaymentResult(success = false, "Invalid wallet address")
        }
        
        // Process crypto payment
        println(s"Processing $cryptoType payment to: $walletAddress")
        // Check blockchain...
        Thread.sleep(300)
        PaymentResult(success = true, s"Crypto payment of $amount in $cryptoType processed")
        
      case "MOMO" =>
        val phone = details.getOrElse("phone", "")
        
        if (!phone.startsWith("0") || phone.length != 10) {
          return PaymentResult(success = false, "Invalid phone number")
        }
        
        println(s"Processing MoMo payment for: $phone")
        Thread.sleep(150)
        PaymentResult(success = true, s"MoMo payment of $amount processed")
        
      case "VNPAY" =>
        val bankCode = details.getOrElse("bankCode", "")
        
        println(s"Processing VNPay payment with bank: $bankCode")
        Thread.sleep(200)
        PaymentResult(success = true, s"VNPay payment of $amount processed")
        
      case _ =>
        PaymentResult(success = false, s"Unknown payment type: $paymentType")
    }
  }
  
  // Khi thêm payment type mới, phải modify method này - vi phạm Open/Closed Principle!
  // Mỗi payment type có logic riêng, validation riêng, nhưng bị nhét chung vào 1 method
}

case class PaymentResult(success: Boolean, message: String)
