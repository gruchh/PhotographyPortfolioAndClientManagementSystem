# 📸 Photography Portfolio And Client System

<div align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.5.3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" />
  <img src="https://img.shields.io/badge/Angular-20.1.0-DD0031?style=for-the-badge&logo=angular&logoColor=white" />
  <img src="https://img.shields.io/badge/TailwindCSS-4.1.11-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white" />
</div>

<div align="center">
  <h3>🎨 Professional Photography Portfolio & Client Management System</h3>
  <p><em>A modern platform for photographers to showcase their work, publish blogs, and manage client relationships</em></p>
</div>

## 🌟 Overview
A secure, feature-rich web application tailored for photographers. Built with enterprise-grade technologies, it provides a seamless experience for showcasing portfolios, publishing blog articles, and managing client photo sessions. The backend is powered by Spring Boot, while the frontend is built with Angular and styled with Tailwind CSS.

## ✨ Features

### 📝 Blog Management
- 📰 SEO-friendly article publishing with slugs
- 🏷️ Category organization for structured content
- 🔖 Tag system for enhanced discoverability
- 📄 Pagination for optimal performance
- 🔍 Content filtering by publication status

### 🖼️ Portfolio Management
- 📷 Photo uploads with automatic thumbnail generation
- 🔍 EXIF metadata extraction and display
- ⭐ Featured photos showcase
- 👁️ Public/private visibility control
- 📊 Organized collections by photo shoots

### 🎬 Photo Session Management
- 👥 Client session organization
- 📸 Photo grouping by sessions
- ✅ Activity status tracking
- 🗂️ Professional workflow management

### 🔒 Security & Authentication
- 🔐 JWT-based authentication
- 🛡️ Role-based authorization (ADMIN/USER)
- 🔑 Secure endpoint protection
- 👤 User management capabilities

## 🛠️ Tech Stack

### Backend
| Category         | Technology        | Version       |
|------------------|-------------------|---------------|
| Language         | Java              | 17            |
| Framework        | Spring Boot       | 3.5.3         |
| Security         | Spring Security   | Latest        |
| Database         | PostgreSQL        | Production    |
| Database         | H2                | Testing       |
| Migration        | Flyway            | Latest        |
| Build Tool       | Maven             | 3.8+          |

### Frontend
| Category         | Technology        | Version       |
|------------------|-------------------|---------------|
| Framework        | Angular           | 20.1.0        |
| Styling          | Tailwind CSS      | 4.1.11        |
| Pagination       | ngx-pagination    | 6.0.3         |
| Build Tool       | Angular CLI       | 20.1.0        |
| PostCSS          | PostCSS           | 8.5.6         |
| Autoprefixer     | Autoprefixer      | 10.4.21       |

### 📚 Additional Libraries
- **Backend**:
  - 🔑 **JWT**: JSON Web Token authentication
  - 🗺️ **MapStruct**: Object mapping
  - ⚡ **Lombok**: Boilerplate reduction
  - 🖼️ **Thumbnailator**: Image processing
  - 📋 **Metadata Extractor**: EXIF data reading
- **Frontend**:
  - 📦 **Angular Material**: UI components (20.1.0)
  - ⚡ **RxJS**: Reactive programming (~7.8.0)
  - 🛠️ **tslib**: TypeScript utilities (~2.3.0)
  - 🕹️ **zone.js**: Angular zone management (~0.15.0)

## 🚀 API Endpoints

### 📝 Blog Posts
| Method | Endpoint                     | Description                | Auth        |
|--------|------------------------------|----------------------------|-------------|
| GET    | `/api/blog-posts`           | 📋 Get paginated posts     | Public      |
| GET    | `/api/blog-posts/{id}`      | 📄 Get post by ID          | Public      |
| GET    | `/api/blog-posts/slug/{slug}` | 🔗 Get post by slug      | Public      |
| POST   | `/api/blog-posts`           | ✍️ Create new post         | 🔒 ADMIN    |
| DELETE | `/api/blog-posts/{id}`      | 🗑️ Delete post            | 🔒 ADMIN    |

### 🏷️ Categories
| Method | Endpoint                     | Description                | Auth        |
|--------|------------------------------|----------------------------|-------------|
| GET    | `/api/categories`           | 📋 Get all categories      | Public      |
| GET    | `/api/categories/{id}`      | 📄 Get category by ID      | Public      |
| GET    | `/api/categories/slug/{slug}` | 🔗 Get category by slug  | Public      |
| POST   | `/api/categories`           | ➕ Create category         | 🔒 ADMIN    |
| PUT    | `/api/categories/{id}`      | ✏️ Update category        | 🔒 ADMIN    |
| DELETE | `/api/categories/{id}`      | 🗑️ Delete category        | 🔒 ADMIN    |

### 📸 Photos
| Method | Endpoint                     | Description                | Auth        |
|--------|------------------------------|----------------------------|-------------|
| GET    | `/api/photos`               | 🖼️ Get paginated photos   | Public      |
| GET    | `/api/photos/{id}`          | 📄 Get photo by ID         | Public      |
| GET    | `/api/photos/featured`      | ⭐ Get featured photos     | Public      |
| POST   | `/api/photos`               | 📤 Upload photo           | 🔒 ADMIN    |
| PUT    | `/api/photos/{id}`          | ✏️ Update photo           | 🔒 ADMIN    |
| DELETE | `/api/photos/{id}`          | 🗑️ Delete photo           | 🔒 ADMIN    |

### 🎬 Photo Sessions
| Method | Endpoint                     | Description                | Auth        |
|--------|------------------------------|----------------------------|-------------|
| GET    | `/api/photo-shoots`         | 📋 Get paginated sessions  | Public      |
| GET    | `/api/photo-shoots/{id}`    | 📄 Get session by ID       | Public      |
| POST   | `/api/photo-shoots`         | ➕ Create session          | 🔒 ADMIN    |
| DELETE | `/api/photo-shoots/{id}`    | 🗑️ Delete session         | 🔒 ADMIN    |

### 🔖 Tags
| Method | Endpoint                     | Description                | Auth        |
|--------|------------------------------|----------------------------|-------------|
| GET    | `/api/tags`                 | 📋 Get paginated tags      | Public      |
| GET    | `/api/tags/{id}`            | 📄 Get tag by ID           | Public      |
| GET    | `/api/tags/slug/{slug}`     | 🔗 Get tag by slug        | Public      |
| POST   | `/api/tags`                 | ➕ Create tag              | 🔒 ADMIN    |
| DELETE | `/api/tags/{id}`            | 🗑️ Delete tag             | 🔒 ADMIN    |

## 🏃‍♂️ Getting Started

### 📋 Prerequisites
- ☕ **Java**: 17+
- 📦 **Maven**: 3.8+
- 🐘 **PostgreSQL**: 12+ (for production)
- 🟦 **Node.js**: 20+ (for frontend)
- 🛠️ **Angular CLI**: 20.1.0

### 🔧 Installation
1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd photography-blog
   ```

2. **Backend Setup**:
   - Navigate to the backend directory:
     ```bash
     cd backend
     ```
   - Configure PostgreSQL in `application.properties`:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/photography_db
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```
   - Build and run the backend:
     ```bash
     mvn clean install
     mvn spring-boot:run
     ```

3. **Frontend Setup**:
   - Navigate to the frontend directory:
     ```bash
     cd frontend
     ```
   - Install dependencies:
     ```bash
     npm install
     ```
   - Start the Angular development server:
     ```bash
     ng serve
     ```

4. **Access the Application**:
   - Backend API: `http://localhost:8080`
   - Frontend: `http://localhost:4200`

### 🧪 Testing
- **Backend**:
  - Run unit and integration tests:
    ```bash
    mvn test
    ```
- **Frontend**:
  - Run unit tests with Jasmine and Karma:
    ```bash
    ng test
    ```
  - Generate code coverage report:
    ```bash
    ng test --code-coverage
    ```