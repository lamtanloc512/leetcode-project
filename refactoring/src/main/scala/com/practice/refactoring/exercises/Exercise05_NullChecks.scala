package com.practice.refactoring.exercises

/**
 * EXERCISE 05: EXCESSIVE NULL CHECKS - Null paranoia
 * 
 * Code Smell: Null checks everywhere, defensive programming gone wrong
 * 
 * Design Pattern để fix:
 * - Null Object Pattern
 * - Option/Maybe monad
 * - Smart defaults
 * 
 * TODO: Sử dụng Option, Null Object pattern thay vì null checks
 */
class CustomerService {
  
  private var customers: Map[String, Customer] = Map()
  
  def getCustomerDetails(customerId: String): String = {
    val customer = customers.get(customerId).orNull // Returns null if not found
    
    // Null check hell begins!
    if (customer == null) {
      return "Customer not found"
    }
    
    var result = s"Customer: ${customer.name}"
    
    // Check address
    if (customer.address != null) {
      result += s"\nAddress: "
      if (customer.address.street != null) {
        result += customer.address.street
      } else {
        result += "N/A"
      }
      if (customer.address.city != null) {
        result += s", ${customer.address.city}"
      }
      if (customer.address.country != null) {
        result += s", ${customer.address.country}"
      }
    } else {
      result += "\nAddress: Not provided"
    }
    
    // Check contact info
    if (customer.contact != null) {
      if (customer.contact.email != null) {
        result += s"\nEmail: ${customer.contact.email}"
      }
      if (customer.contact.phone != null) {
        result += s"\nPhone: ${customer.contact.phone}"
      }
    }
    
    // Check payment method
    if (customer.paymentMethod != null) {
      if (customer.paymentMethod.cardType != null) {
        result += s"\nPayment: ${customer.paymentMethod.cardType}"
        if (customer.paymentMethod.lastFourDigits != null) {
          result += s" ending in ${customer.paymentMethod.lastFourDigits}"
        }
      }
    } else {
      result += "\nPayment: No payment method on file"
    }
    
    result
  }
  
  def calculateDiscount(customerId: String): Double = {
    val customer = customers.get(customerId).orNull
    
    if (customer == null) {
      return 0.0
    }
    
    var discount = 0.0
    
    // Check membership
    if (customer.membership != null) {
      if (customer.membership.tier != null) {
        customer.membership.tier match {
          case "GOLD" => discount += 0.15
          case "SILVER" => discount += 0.10
          case "BRONZE" => discount += 0.05
          case _ => // no discount
        }
      }
      
      // Check if membership is still valid
      if (customer.membership.expiryDate != null) {
        // More null checks...
      }
    }
    
    // Check loyalty points
    if (customer.loyaltyPoints != null) {
      if (customer.loyaltyPoints > 1000) {
        discount += 0.05
      }
    }
    
    discount
  }
  
  def sendPromotion(customerId: String, promoMessage: String): Boolean = {
    val customer = customers.get(customerId).orNull
    
    if (customer == null) {
      return false
    }
    
    if (customer.contact == null) {
      return false
    }
    
    if (customer.contact.email == null || customer.contact.email.isEmpty) {
      if (customer.contact.phone == null || customer.contact.phone.isEmpty) {
        return false // No way to contact
      }
      // Send SMS
      println(s"Sending SMS to ${customer.contact.phone}: $promoMessage")
    } else {
      // Send email
      println(s"Sending email to ${customer.contact.email}: $promoMessage")
    }
    
    true
  }
}

// Data classes với nullable fields - source of the problem!
case class Customer(
  id: String,
  name: String,
  address: Address,        // nullable
  contact: ContactInfo,    // nullable
  paymentMethod: PaymentMethod, // nullable
  membership: Membership,   // nullable
  loyaltyPoints: java.lang.Integer // nullable boxed Integer
)

case class Address(
  street: String,   // nullable
  city: String,     // nullable
  country: String   // nullable
)

case class ContactInfo(
  email: String,    // nullable
  phone: String     // nullable
)

case class PaymentMethod(
  cardType: String,        // nullable
  lastFourDigits: String   // nullable
)

case class Membership(
  tier: String,            // nullable
  expiryDate: String       // nullable
)
