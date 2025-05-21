# 🗂️ SEDS519 Homework 1 – Weekly Course Schedule Uploader

This project is a Spring Boot-based web application that allows users to upload course schedules in **Excel (.xlsx)** or **PDF (.pdf)** format and renders them as a structured **weekly HTML timetable**.

## 📦 Project Structure

```
com.seds519.hw1
│
├── Hw1Application.java              # Main Spring Boot application
│
├── controller
│   └── FileUploadController.java   # Handles file upload and delegates parsing/rendering
│
├── model
│   └── Course.java                 # POJO representing a course session
│
├── parser
│   ├── FileParser.java            # Interface for file parsers
│   ├── ExcelFileParser.java       # Parses Excel (.xlsx) files
│   └── PdfFileParser.java         # Parses PDF (.pdf) files
│
├── service
│   ├── FileParserContext.java     # Chooses the correct parser based on file extension
│   └── HtmlScheduleRenderer.java  # Renders the parsed schedule into HTML format
```

---

## ✅ Features
- 📁 Upload Excel or PDF file.
- 📊 Extracts structured schedule data (day, time, course, instructor, classroom).
- 🖼️ Converts schedule into a weekly HTML table with stylized layout.
- 🚫 Validates input file and handles errors gracefully.

---

## 🔧 Technologies Used
- **Java 17**
- **Spring Boot 3.4.5**
- **Apache POI** – For Excel file parsing
- **Tabula** – For parsing tables from PDF files
- **HTML/CSS** – For rendering schedules in a clean tabular view

---

## 🚀 Running the Application

### 1. Clone and Build
```bash
git clone https://github.com/nakahraman/SEDS519_G1_HW1.git
cd hw1-schedule-uploader
mvn clean install
```

### 2. Run the Application
```bash
mvn spring-boot:run
```
The app will start on `http://localhost:8080`

### 3. Access Frontend
You can access the upload interface directly via your browser:
```
http://localhost:8080/upload.html
```
Alternatively, you can open `upload.html` directly from your project at the following path:
```
src/main/resources/static/upload.html
```
---


## 📝 Example Usage
Upload either of the following:
- Excel file with time slots in column A and blocks of 3 columns per day.
- PDF file containing extracted weekly schedules in tabular layout.

Result: a styled HTML table showing the full weekly schedule.

---

## 💡 Design Patterns Used

| Pattern          | Where Used                         | Purpose                                            |
|------------------|------------------------------------|----------------------------------------------------|
| Strategy Pattern | `FileParserContext`                | Dynamically select parser based on file extension. |
| Factory Pattern  | `FileParserContext#getParser`      | Creates parser instance from map.                  |
| Template Pattern | `ExcelFileParser`, `PdfFileParser` | Provide consistent `parse()` structure.            |
| MVC Pattern      | Overall Architecture               | Separates controller, model, and view.             |

---

## 👨‍🏫 Class Responsibilities
- **Course.java** – Represents a course session with attributes.
- **FileUploadController** – Receives file, triggers parsing and rendering.
- **ExcelFileParser & PdfFileParser** – Convert raw files into `Course` objects.
- **FileParserContext** – Selects the correct parser using file extension.
- **HtmlScheduleRenderer** – Groups and formats `Course` objects into an HTML table.

---

## 🧪 Testing
You can add unit tests to verify:
- FileParser implementations produce correct `Course` lists.
- HTML output matches expected format.
- Controller properly returns HTML or error responses.

---

## 📄 License
This project is created for academic purposes as part of the **SEDS519 Software Design Patterns** course.

---

