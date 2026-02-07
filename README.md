
# UpsStream

Full-stack enterprise web application built with Spring Boot 3 & React. Features JWT authentication, RESTful API with Swagger documentation, JPA persistence, and comprehensive testing with JaCoCo coverage.

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Uso](#uso)
- [Testing](#testing)
- [Documentación API](#documentación-api)
- [Estructura del Proyecto](#estructura-del-proyecto)


## 📖 Descripción

UpsStream es una aplicación web empresarial full-stack desarrollada como proyecto académico para el curso de Diseño y Testing de Software. La aplicación implementa las mejores prácticas de desarrollo moderno con un backend robusto en Spring Boot 3 y un frontend interactivo en React.

### Características Principales

- 🔐 **Autenticación JWT**: Sistema de autenticación seguro basado en tokens
- 🌐 **RESTful API**: API bien estructurada siguiendo principios REST
- 📚 **Documentación Swagger**: Documentación automática e interactiva de la API
- 💾 **Persistencia JPA**: Gestión de datos con Spring Data JPA
- 🧪 **Testing Completo**: Suite de pruebas con cobertura JaCoCo
- ⚛️ **Frontend React**: Interfaz de usuario moderna y responsive
- 🐳 **Docker Ready**: Configuración Docker para base de datos MySQL

## 🛠️ Tecnologías

### Backend
- **Java** - Lenguaje principal
- **Spring Boot 3** - Framework de aplicación
- **Spring Security** - Autenticación y autorización
- **JWT** - JSON Web Tokens para autenticación
- **JPA/Hibernate** - ORM para persistencia de datos
- **MySQL** - Base de datos relacional
- **Swagger/OpenAPI** - Documentación de API
- **JaCoCo** - Cobertura de código
- **Maven** - Gestión de dependencias

### Frontend
- **React** - Biblioteca de interfaz de usuario
- **JavaScript/JSX** - Lenguaje de programación

### DevOps
- **Docker Compose** - Contenedorización
- **Travis CI** - Integración continua

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java 17** o superior
- **Node.js 16** o superior
- **npm** o **yarn**
- **Maven 3.8+**
- **Docker** y **Docker Compose** (opcional, para base de datos)

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/angelmanuelferrer/UpsStream.git
cd UpsStream
```

### 2. Configurar la Base de Datos

#### Opción A: Con Docker (Recomendado)

```bash
docker-compose up -d
```

Esto iniciará una instancia de MySQL 5.7 con las siguientes credenciales:
- **Usuario**: petclinic
- **Contraseña**: petclinic
- **Base de datos**: petclinic
- **Puerto**: 3306

#### Opción B: MySQL Local

Crea una base de datos MySQL manualmente y actualiza las credenciales en `application.properties`.

### 3. Instalar Dependencias del Backend

```bash
./mvnw clean install
```

### 4. Instalar Dependencias del Frontend

```bash
cd frontend
npm install
cd ..
```

## ⚙️ Configuración

### Backend

Configura las propiedades de la aplicación en `src/main/resources/application.properties`:

```properties
# Configuración de Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/petclinic
spring.datasource.username=petclinic
spring.datasource.password=petclinic

# Configuración JWT
jwt.secret=your-secret-key
jwt.expiration=86400000

# Configuración JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Frontend

Si es necesario, configura las variables de entorno en `frontend/.env`:

```env
REACT_APP_API_URL=http://localhost:8080/api
```

## 💻 Uso

### Ejecutar el Backend

```bash
./mvnw spring-boot:run
```

El servidor estará disponible en `http://localhost:8080`

### Ejecutar el Frontend

```bash
cd frontend
npm start
```

La aplicación estará disponible en `http://localhost:3000`

### Ejecutar Todo con un Solo Comando

```bash
# Terminal 1 - Backend
./mvnw spring-boot:run

# Terminal 2 - Frontend
cd frontend && npm start
```

## 🧪 Testing

### Ejecutar Tests del Backend

```bash
./mvnw test
```

### Generar Reporte de Cobertura JaCoCo

```bash
./mvnw clean test jacoco:report
```

El reporte estará disponible en `target/site/jacoco/index.html`

### Ejecutar Tests del Frontend

```bash
cd frontend
npm test
```

## 📚 Documentación API

Una vez que la aplicación esté en ejecución, accede a la documentación interactiva de Swagger:

```
http://localhost:8080/swagger-ui.html
```

## 📁 Estructura del Proyecto

```
UpsStream/
├── src/
│   ├── main/
│   │   ├── java/          # Código fuente Java
│   │   └── resources/     # Archivos de configuración
│   └── test/              # Tests unitarios y de integración
├── frontend/
│   ├── public/            # Archivos públicos
│   ├── src/               # Código fuente React
│   └── package.json       # Dependencias del frontend
├── docs/                  # Documentación adicional
├── docker-compose.yml     # Configuración Docker
├── pom.xml               # Configuración Maven
├── .travis.yml           # Configuración CI/CD
└── README.md             # Este archivo
```
