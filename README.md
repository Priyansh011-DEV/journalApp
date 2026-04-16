#  AI-Powered Journal App (Full-Stack | Cloud Deployed)

A modern, full-stack journaling application with **secure multi-user support**, **AI-powered mood detection**, and a **responsive aesthetic UI**. Built using Spring Boot and deployed on cloud infrastructure, this project combines backend engineering with user-centric design.

---

## 🚀 Live Demo

🔗 Swagger UI: https://journalapp-ma29.onrender.com/login

---

## ✨ Features

### 🔐 Authentication & Security

* Role-Based Access Control (ADMIN / USER)
* Secure login & registration system
* BCrypt password hashing
* Token-based password reset (email integration)
* Strict user-level data isolation

---

### ✍️ Journal System

* Create, update, and delete journal entries
* View latest entries with clean UI
* Optimistic UI updates (instant delete without refresh)
* Date-based journaling system

---

### 🧠 AI Integration (NEW 🚀)

* AI-powered **mood detection** from journal text
* Returns mood (Happy / Sad / Angry / Neutral)
* Designed for future expansion (recommendations, insights)

---

### 🎨 Frontend UI

* Fully responsive (mobile + desktop)
* Soft aesthetic glassmorphism design
* Calendar dashboard (Flatpickr integration)
* Mood widget, clock, and music section
* Clean 3-column layout (Dashboard / Controls / Journals)

---

### ☁️ Cloud & Deployment

* MongoDB Atlas (cloud database)
* Docker containerization
* Deployed on Render
* Environment-based configuration (production-ready)

---

## 🛠 Tech Stack

**Backend**

* Java
* Spring Boot
* Spring Security
* Spring Data MongoDB

**Frontend**

* HTML
* CSS (Glassmorphism UI)
* JavaScript (Vanilla)

**Database**

* MongoDB Atlas

**DevOps & Tools**

* Docker
* Render (Cloud Deployment)
* Swagger / OpenAPI
* Maven

**AI**

* External AI API (OpenAI / Gemini)

---

## 📂 Architecture

Follows clean layered architecture:

Controller → Service → Repository → Database

Includes:

* DTO-based design
* Global exception handling
* Validation & structured responses

---

## 🐳 Deployment Details

* Containerized using Docker
* Deployed on Render
* Uses environment variables for:

    * `MONGODB_URI`
    * API keys (AI integration)

---

## ▶️ Running Locally

```bash
git clone https://github.com/your-username/journal-app.git
cd journal-app
```

Configure:

```yaml
application-local.yml
```

Add your MongoDB URI.

Run:

```bash
mvn spring-boot:run
```

Access:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 📸 Screenshots (Add Tomorrow 📷)

* Dashboard UI
* Journal Entry Page
* Mobile View
* AI Mood Detection

---

## 💡 Future Improvements

* Mood analytics dashboard (charts)
* AI-based recommendations (music, quotes)
* “Talk to your journal” (chat with past entries)
* Dark mode toggle
* Mobile app version

---

## 👤 Author

**Priyansh Kumar Singh**
🔗 GitHub: https://github.com/Priyansh011-DEV
🔗 LinkedIn: https://www.linkedin.com/in/priyanshsingh01

---

## ⭐ Final Note

This project demonstrates:

* Full-stack development
* Secure backend engineering
* Cloud deployment & DevOps
* UI/UX design
* AI integration

Built as a portfolio project with real-world production practices.
