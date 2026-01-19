package com.practice.refactoring.exercises

/**
 * EXERCISE 03: DUPLICATED CODE - Copy-paste programming
 * 
 * Code Smell: Same logic repeated across multiple classes
 * 
 * Design Pattern để fix:
 * - Template Method Pattern
 * - Extract superclass/trait
 * 
 * TODO: Tạo abstract class DataExporter với template method
 */

// Xuất dữ liệu ra CSV
class CsvExporter {
  
  def exportUsers(users: List[User]): String = {
    // Step 1: Validate - DUPLICATED
    if (users.isEmpty) {
      return "No data to export"
    }
    
    // Step 2: Log start - DUPLICATED
    println(s"Starting CSV export at ${java.time.LocalDateTime.now()}")
    val startTime = System.currentTimeMillis()
    
    // Step 3: Build header - SPECIFIC
    val header = "id,name,email,createdAt"
    
    // Step 4: Build rows - SPECIFIC  
    val rows = users.map { user =>
      s"${user.id},${user.name},${user.email},${user.createdAt}"
    }
    
    // Step 5: Combine - DUPLICATED pattern
    val result = (header +: rows).mkString("\n")
    
    // Step 6: Log complete - DUPLICATED
    val endTime = System.currentTimeMillis()
    println(s"CSV export completed in ${endTime - startTime}ms")
    
    result
  }
  
  def exportProducts(products: List[Product]): String = {
    // Step 1: Validate - DUPLICATED
    if (products.isEmpty) {
      return "No data to export"  
    }
    
    // Step 2: Log start - DUPLICATED
    println(s"Starting CSV export at ${java.time.LocalDateTime.now()}")
    val startTime = System.currentTimeMillis()
    
    // Step 3: Build header - SPECIFIC
    val header = "id,name,price,stock"
    
    // Step 4: Build rows - SPECIFIC
    val rows = products.map { product =>
      s"${product.id},${product.name},${product.price},${product.stock}"
    }
    
    // Step 5: Combine - DUPLICATED pattern
    val result = (header +: rows).mkString("\n")
    
    // Step 6: Log complete - DUPLICATED  
    val endTime = System.currentTimeMillis()
    println(s"CSV export completed in ${endTime - startTime}ms")
    
    result
  }
}

// Xuất dữ liệu ra JSON - Same duplication!
class JsonExporter {
  
  def exportUsers(users: List[User]): String = {
    // Step 1: Validate - DUPLICATED
    if (users.isEmpty) {
      return "[]"
    }
    
    // Step 2: Log start - DUPLICATED
    println(s"Starting JSON export at ${java.time.LocalDateTime.now()}")
    val startTime = System.currentTimeMillis()
    
    // Step 3-4: Build JSON - SPECIFIC
    val jsonObjects = users.map { user =>
      s"""{"id":"${user.id}","name":"${user.name}","email":"${user.email}","createdAt":"${user.createdAt}"}"""
    }
    
    // Step 5: Combine - DIFFERENT format but same pattern
    val result = jsonObjects.mkString("[", ",", "]")
    
    // Step 6: Log complete - DUPLICATED
    val endTime = System.currentTimeMillis()
    println(s"JSON export completed in ${endTime - startTime}ms")
    
    result
  }
  
  def exportProducts(products: List[Product]): String = {
    // Step 1: Validate - DUPLICATED
    if (products.isEmpty) {
      return "[]"
    }
    
    // Step 2: Log start - DUPLICATED
    println(s"Starting JSON export at ${java.time.LocalDateTime.now()}")
    val startTime = System.currentTimeMillis()
    
    // Step 3-4: Build JSON - SPECIFIC
    val jsonObjects = products.map { product =>
      s"""{"id":"${product.id}","name":"${product.name}","price":${product.price},"stock":${product.stock}}"""
    }
    
    // Step 5: Combine
    val result = jsonObjects.mkString("[", ",", "]")
    
    // Step 6: Log complete - DUPLICATED
    val endTime = System.currentTimeMillis()
    println(s"JSON export completed in ${endTime - startTime}ms")
    
    result
  }
}

// Xuất dữ liệu ra XML - More duplication!
class XmlExporter {
  
  def exportUsers(users: List[User]): String = {
    if (users.isEmpty) {
      return "<users/>"
    }
    
    println(s"Starting XML export at ${java.time.LocalDateTime.now()}")
    val startTime = System.currentTimeMillis()
    
    val xmlElements = users.map { user =>
      s"""  <user id="${user.id}">
         |    <name>${user.name}</name>
         |    <email>${user.email}</email>
         |    <createdAt>${user.createdAt}</createdAt>
         |  </user>""".stripMargin
    }
    
    val result = s"<users>\n${xmlElements.mkString("\n")}\n</users>"
    
    val endTime = System.currentTimeMillis()
    println(s"XML export completed in ${endTime - startTime}ms")
    
    result
  }
}

case class User(id: String, name: String, email: String, createdAt: String)
case class Product(id: String, name: String, price: Double, stock: Int)
