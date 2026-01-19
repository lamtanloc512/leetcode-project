package com.practice.refactoring.exercises

/**
 * EXERCISE 09: LONG PARAMETER LIST - Too many method parameters
 * 
 * Code Smell: Methods với quá nhiều parameters, khó nhớ thứ tự
 * 
 * Design Pattern để fix:
 * - Builder Pattern
 * - Parameter Object
 * - Named Parameters (Scala có sẵn)
 * 
 * TODO: Sử dụng Builder pattern hoặc Parameter Object
 */
class EmailSender {
  
  // Quá nhiều parameters - không thể nhớ thứ tự!
  def sendEmail(
    to: String,
    cc: String,
    bcc: String,
    from: String,
    replyTo: String,
    subject: String,
    bodyHtml: String,
    bodyPlainText: String,
    isHighPriority: Boolean,
    requestReadReceipt: Boolean,
    attachmentPaths: List[String],
    attachmentNames: List[String],
    inlineImagePaths: List[String],
    inlineImageIds: List[String],
    customHeaders: Map[String, String],
    scheduledTime: java.time.LocalDateTime,
    expiryTime: java.time.LocalDateTime,
    trackOpens: Boolean,
    trackClicks: Boolean,
    unsubscribeUrl: String,
    templateId: String,
    templateVariables: Map[String, String]
  ): String = {
    // Imagine calling this method:
    // sendEmail("a@b.com", "", "", "sender@company.com", "", "Hi", "<p>Hello</p>", 
    //           "Hello", false, false, List(), List(), List(), List(), 
    //           Map(), null, null, true, true, "", null, Map())
    // Which string is which?!
    
    println(s"Sending email to $to from $from")
    println(s"Subject: $subject")
    s"Email sent to $to"
  }
}

class ReportBuilder {
  
  // Another horrible example
  def generateReport(
    reportType: String,
    title: String,
    subtitle: String,
    author: String,
    department: String,
    startDate: java.time.LocalDate,
    endDate: java.time.LocalDate,
    includeCharts: Boolean,
    chartTypes: List[String],
    includeSummary: Boolean,
    includeDetails: Boolean,
    includeAppendix: Boolean,
    pageOrientation: String,
    pageSize: String,
    marginTop: Double,
    marginBottom: Double,
    marginLeft: Double,
    marginRight: Double,
    fontSize: Int,
    fontFamily: String,
    headerText: String,
    footerText: String,
    watermarkText: String,
    outputFormat: String,
    compressionLevel: Int,
    encryptOutput: Boolean,
    password: String
  ): Array[Byte] = {
    // Calling this is a nightmare!
    println(s"Generating $reportType report: $title")
    Array.emptyByteArray
  }
}

class DatabaseConnection {
  
  // Connection with many optional parameters
  def connect(
    host: String,
    port: Int,
    database: String,
    username: String,
    password: String,
    useSSL: Boolean,
    sslCertPath: String,
    sslKeyPath: String,
    connectionTimeout: Int,
    readTimeout: Int,
    writeTimeout: Int,
    maxPoolSize: Int,
    minPoolSize: Int,
    maxIdleTime: Int,
    autoReconnect: Boolean,
    failoverHost: String,
    failoverPort: Int,
    charset: String,
    timezone: String,
    applicationName: String
  ): Boolean = {
    println(s"Connecting to $host:$port/$database")
    true
  }
}

class OrderCreator {
  
  // Boolean parameters are especially bad - what does 'true, true, false' mean?
  def createOrder(
    customerId: String,
    items: List[String],
    useDefaultAddress: Boolean,
    saveAddress: Boolean,
    isGift: Boolean,
    giftWrap: Boolean,
    includeReceipt: Boolean,
    expressShipping: Boolean,
    insuranceRequired: Boolean,
    requireSignature: Boolean,
    scheduledDelivery: Boolean,
    notifyBySms: Boolean,
    notifyByEmail: Boolean,
    applyLoyaltyPoints: Boolean,
    useStoredPayment: Boolean,
    splitPayment: Boolean
  ): String = {
    // createOrder("c123", items, true, false, true, true, false, true, false, true, false, true, true, false, true, false)
    // Vô nghĩa khi đọc!
    
    "Order created"
  }
}

// Vấn đề:
// 1. Không thể nhớ thứ tự parameters
// 2. Boolean parameters không có ý nghĩa khi đọc (true, false, true...)
// 3. Dễ nhầm parameters có cùng type (String, String, String...)
// 4. Khi thêm parameter mới phải sửa tất cả callers
// 5. Very hard to provide defaults
// 6. IDE autocomplete không giúp ích nhiều
