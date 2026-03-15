# The Clervinator

![Java Version](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-Framework-brightgreen?style=for-the-badge&logo=springboot)
![Build](https://img.shields.io/badge/Build-Maven-blue?style=for-the-badge&logo=apache-maven)
![License](https://img.shields.io/badge/License-Proprietary-red?style=for-the-badge)

**The Clervinator** is a web-based, pure interpreter for the **LEXOR** programming language, built with **Spring Boot** to provide an interactive, real-time playground for code execution and diagnostics.

---

## Features

- **Pure Interpreter:** Full execution engine for the LEXOR programming language specification.
- **Web-Based Playground:** Interactive web interface for writing and running code in real-time.
- **Real-Time Diagnostics:** Detailed error reporting and execution feedback for developers.
- **Spring Boot Backend:** Robust HTTP API and server-side execution environment.

## Tech Stack

- **Frontend:** HTML5, TailwindCSS, Monaco Editor
- **Backend:** Spring Boot (Java 25)
- **Interpreter Logic:** Pure Java Implementation

---

## Getting Started

### Prerequisites

- **Java 25 JDK** (Required for the latest language features)
- **Maven** (`mvn`)

### Run Locally

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/your-username/clervinator.git](https://github.com/your-username/clervinator.git)
   cd clervinator
   ```

2. **Run using Maven:**
    ```bash
    ./mvnw spring-boot:run
    ```

3. **Access the UI:**
    Open your browser to http://localhost:8080

> If your application is configured to run on a different port, check `application.properties` / `application.yml`.

## Usage

1. Open the web UI in your browser.
2. Paste or write LEXOR code in the editor.
3. Run/interpret the code.
4. Review output and diagnostics in the results panel.

## Project Structure (high-level)

Typical Spring Boot layout:

- `src/main/java/` — Spring Boot app + interpreter implementation
- `src/main/resources/` — templates/static assets/config
- `src/test/` — unit/integration tests

<hr />

## License
<p><b>© 2026. All Rights Reserved.</b></p> 

[View License Agreement](./LICENSE.md)
