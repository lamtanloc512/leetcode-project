package com.practice.refactoring.exercises

/**
 * EXERCISE 04: HARDCODED DEPENDENCIES - Tight coupling
 * 
 * Code Smell: Classes tạo dependencies trực tiếp bên trong, không thể test hoặc thay thế
 * 
 * Design Pattern để fix:
 * - Dependency Injection (Constructor Injection)
 * - Factory Pattern
 * 
 * TODO: Inject dependencies qua constructor, tạo interfaces/traits
 */
class ReportGenerator {
  
  // Hardcoded dependencies - SMELL!
  private val database = new MySqlDatabase()        // Không thể thay bằng PostgreSQL
  private val emailService = new SmtpEmailService() // Không thể test mà không gửi email thật
  private val pdfGenerator = new ITextPdfGenerator() // Không thể dùng thư viện khác
  
  def generateMonthlyReport(month: Int, year: Int): Unit = {
    // Fetch data - hardcoded to MySQL
    val salesData = database.query(s"SELECT * FROM sales WHERE month=$month AND year=$year")
    val customerData = database.query(s"SELECT * FROM customers")
    
    // Generate PDF - hardcoded to iText
    val reportContent = buildReportContent(salesData, customerData)
    val pdfBytes = pdfGenerator.generate(reportContent)
    
    // Save to disk - hardcoded path!
    val fileName = s"/reports/monthly_${year}_${month}.pdf"
    java.nio.file.Files.write(java.nio.file.Paths.get(fileName), pdfBytes)
    
    // Send email - hardcoded to SMTP
    emailService.send(
      to = "manager@company.com", // Hardcoded recipient!
      subject = s"Monthly Report $month/$year",
      body = "Please find attached the monthly report.",
      attachment = pdfBytes
    )
    
    // Log - using println instead of proper logger
    println(s"Report generated and sent for $month/$year")
  }
  
  private def buildReportContent(sales: List[Map[String, Any]], customers: List[Map[String, Any]]): String = {
    s"Sales: ${sales.size} records, Customers: ${customers.size} records"
  }
}

// Concrete implementations that should be abstracted
class MySqlDatabase {
  def query(sql: String): List[Map[String, Any]] = {
    println(s"Executing MySQL query: $sql")
    // Giả lập kết quả
    List(Map("id" -> 1, "amount" -> 100.0))
  }
  
  def execute(sql: String): Int = {
    println(s"Executing MySQL statement: $sql")
    1
  }
}

class SmtpEmailService {
  def send(to: String, subject: String, body: String, attachment: Array[Byte] = Array()): Boolean = {
    println(s"Sending email via SMTP to: $to")
    println(s"Subject: $subject")
    // Thực tế sẽ connect tới SMTP server
    true
  }
}

class ITextPdfGenerator {
  def generate(content: String): Array[Byte] = {
    println(s"Generating PDF with iText: ${content.take(50)}...")
    // Giả lập PDF bytes
    content.getBytes
  }
}

// Vấn đề với code này:
// 1. Không thể unit test ReportGenerator mà không có database thật
// 2. Không thể test mà không gửi email thật
// 3. Không thể chuyển sang database khác (PostgreSQL, MongoDB)
// 4. Không thể dùng email service khác (SendGrid, AWS SES)
// 5. Paths, recipients hardcoded - khó configure cho environments khác nhau
