# AI Portfolio — Spring Boot + HTML/CSS/JS + MySQL + NVIDIA AI

A complete personal portfolio website featuring:

- **Sections**: Home, About, Skills, Projects, Education, Experience, Certifications, Contact, Footer
- **Login / Register** with JWT authentication (Spring Security + BCrypt)
- **AI Assistant** chat widget powered by **NVIDIA NIM** (`meta/llama-3.1-70b-instruct`) — ask anything about the portfolio, get improvement suggestions, or general technical help
- **MySQL** persistence (with H2 fallback for instant startup)
- **Responsive** layout for mobile & desktop
- **Animations**: typing effect, scroll-reveal, hover effects, animated gradient hero, pulsing AI button
- **Dark / Light mode** toggle
- **Contact form** persisted to DB
- Single-page smooth navigation

## 🛠 Tech Stack

- Backend: Java 17, Spring Boot 3.3, Spring Security, Spring Data JPA, WebFlux (for NVIDIA API)
- Database: MySQL 8 (H2 in-memory by default)
- Auth: JWT (jjwt)
- Frontend: HTML5, CSS3, vanilla JavaScript
- AI: NVIDIA NIM Inference API

## 🚀 Quick Start

### 1. Run with H2 (zero config — works immediately)

```bash
mvn spring-boot:run
```

Open http://localhost:8080

### 2. Run with MySQL

1. Create a database:
   ```sql
   CREATE DATABASE portfolio_db;
   ```
2. Edit `src/main/resources/application.properties` — comment the H2 block and uncomment the MySQL block. Set your username/password.
3. Run `mvn spring-boot:run`.

### 3. Enable NVIDIA AI

1. Get a free API key at https://build.nvidia.com/
2. Either set the env var:
   ```bash
   export NVIDIA_API_KEY=nvapi-xxxxxxxxxxxx
   ```
   or edit `nvidia.api.key` in `application.properties`.
3. Restart the app.

Without a key, the AI widget still responds (with a friendly placeholder).

## 📂 Project Structure

```
portfolio/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/portfolio/
    │   ├── PortfolioApplication.java
    │   ├── config/SecurityConfig.java
    │   ├── controller/   (Auth, AI, Contact)
    │   ├── dto/AuthDtos.java
    │   ├── model/        (User, ContactMessage)
    │   ├── repository/
    │   ├── security/     (JwtUtil, JwtFilter)
    │   └── service/      (AuthService, NvidiaAiService)
    └── resources/
        ├── application.properties
        └── static/
            ├── index.html  (portfolio)
            ├── login.html
            ├── register.html
            ├── css/style.css
            └── js/app.js, auth.js
```

## 🔑 API Endpoints

| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| POST | `/api/auth/register` | – | Create account, returns JWT |
| POST | `/api/auth/login` | – | Login, returns JWT |
| POST | `/api/contact` | – | Save contact message |
| POST | `/api/ai/chat` | **JWT** | Ask the NVIDIA AI assistant |

## ✏️ Personalize

Edit `src/main/resources/static/index.html` and replace:
- "Your Name", title, summary, info
- Projects, education, experience, certifications
- Social links

Also update `portfolio.owner.*` in `application.properties` so the AI knows about you.

## 🌐 Deploy Guide

### Option 1: Render (Recommended - Free)
1. Push code to GitHub.
2. Go to [Render Dashboard](https://dashboard.render.com/) -> **New Web Service**.
3. Connect your GitHub repository.
4. Select **Docker** environment (it automatically detects `Dockerfile`).
5. Add Environment Variables in Render:
   - `NVIDIA_API_KEY`: Your NVIDIA NIM API key
   - `JWT_SECRET`: Random 32+ character string
6. Click **Deploy Web Service**!

### Option 2: Docker / Railway / AWS / Heroku
```bash
# Build Docker image
docker build -t portfolio-app .

# Run container locally or in cloud
docker run -p 8080:8080 -e NVIDIA_API_KEY="your_key" portfolio-app
```

---

Built with ❤️ using Spring Boot & NVIDIA AI.

