<div align="center">

# 🌐 ChibchaWeb

### Plataforma integral para la gestión de servicios de hosting web

Administración de usuarios, planes de hosting, dominios, pagos y soporte técnico,
construida con **arquitectura orientada al dominio (DDD)** y **patrones de diseño GoF**.

[![React](https://img.shields.io/badge/React-18-61DAFB?logo=react&logoColor=white&style=for-the-badge)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6?logo=typescript&logoColor=white&style=for-the-badge)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-5-646CFF?logo=vite&logoColor=white&style=for-the-badge)](https://vitejs.dev/)
[![TailwindCSS](https://img.shields.io/badge/TailwindCSS-3-06B6D4?logo=tailwindcss&logoColor=white&style=for-the-badge)](https://tailwindcss.com/)
[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white&style=for-the-badge)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4-6DB33F?logo=springboot&logoColor=white&style=for-the-badge)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white&style=for-the-badge)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white&style=for-the-badge)](https://www.docker.com/)

[![Estado](https://img.shields.io/badge/estado-en_desarrollo-yellow?style=flat-square)]()
[![Licencia](https://img.shields.io/badge/licencia-académica-lightgrey?style=flat-square)]()

</div>

---

## 📖 Descripción

**ChibchaWeb** es una solución orientada a la gestión integral de servicios de hosting web.
Permite administrar **clientes, empleados y administradores**, así como ejecutar procesos de
**contratación de planes, registro de dominios, gestión de pagos y atención de tickets de soporte**.

El proyecto fue diseñado bajo un enfoque orientado a objetos, incorporando **modelado de dominio,
persistencia objeto-relacional y patrones de diseño** para garantizar modularidad, escalabilidad
y mantenibilidad.

---

## 📑 Tabla de contenidos

- [✨ Funcionalidades](#-funcionalidades)
- [🛠️ Stack tecnológico](#-stack-tecnológico)
- [🏛️ Arquitectura](#-arquitectura)
- [🎯 Patrones de diseño](#-patrones-de-diseño)
- [📂 Estructura del proyecto](#-estructura-del-proyecto)
- [🚀 Instalación y ejecución](#-instalación-y-ejecución)
- [⚙️ Variables de entorno](#-variables-de-entorno)
- [👥 Roles y portales](#-roles-y-portales)
- [📊 Estado del proyecto](#-estado-del-proyecto)
- [✍️ Autor](#-autor)
- [📄 Licencia](#-licencia)

---

## ✨ Funcionalidades

| | Módulo | Descripción |
|---|---|---|
| 👥 | **Usuarios y acceso** | Gestión de clientes, empleados y administradores con control por roles y autenticación JWT. |
| 📦 | **Planes de hosting** | Administración de planes sobre plataformas Windows y Unix. |
| 🌐 | **Dominios** | Registro y gestión de dominios mediante integración con registradores. |
| 💳 | **Pagos** | Sistema de pagos extensible (Visa, Mastercard, Diners) con validación de tarjetas. |
| 🎫 | **Soporte técnico** | Gestión de tickets con niveles de escalamiento. |
| 🔐 | **Cifrado** | Protección de información sensible mediante múltiples algoritmos. |
| 🧩 | **Arquitectura** | Basada en patrones de diseño y separación por capas. |
| 🖥️ | **Interfaz gráfica** | Aplicación SPA desarrollada en React + TypeScript. |

---

## 🛠️ Stack tecnológico

<div align="center">

### Frontend
[![React](https://img.shields.io/badge/React-18-61DAFB?logo=react&logoColor=white)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-5-646CFF?logo=vite&logoColor=white)](https://vitejs.dev/)
[![React Router](https://img.shields.io/badge/React_Router-7-CA4245?logo=reactrouter&logoColor=white)](https://reactrouter.com/)
[![TailwindCSS](https://img.shields.io/badge/TailwindCSS-3-06B6D4?logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)
[![Axios](https://img.shields.io/badge/Axios-1-5A29E4?logo=axios&logoColor=white)](https://axios-http.com/)

### Backend
[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![JPA](https://img.shields.io/badge/Spring_Data_JPA-Hibernate-6DB33F?logo=hibernate&logoColor=white)](https://spring.io/projects/spring-data-jpa)
[![MapStruct](https://img.shields.io/badge/MapStruct-1.6-orange)](https://mapstruct.org/)

### Persistencia y DevOps
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![Nginx](https://img.shields.io/badge/Nginx-009639?logo=nginx&logoColor=white)](https://nginx.org/)
[![Vercel](https://img.shields.io/badge/Vercel-000000?logo=vercel&logoColor=white)](https://vercel.com/)

</div>

| Capa | Tecnologías |
|---|---|
| **Frontend** | React 18, TypeScript, Vite, React Router, TailwindCSS, Axios, Lucide React |
| **Backend** | Java 21, Spring Boot 4, Spring Security (JWT), Spring Data JPA, MapStruct, Lombok |
| **Persistencia** | PostgreSQL 16 (mapeo objeto-relacional / Hibernate) |
| **DevOps** | Docker, Docker Compose, Nginx, Vercel |
| **Modelado y diseño** | UML, PlantUML, Draw.io, Figma |

---

## 🏛️ Arquitectura

El backend sigue los principios de **Domain-Driven Design (DDD)** con separación en capas
(`domain`, `application`, `infrastructure`, `presentation`) y se organiza en módulos funcionales:

```text
acceso        → Autenticación, autorización y seguridad (JWT)
usuario       → Clientes, empleados y administradores
plan          → Planes de hosting (plataformas Windows / Unix)
dominio       → Registro y gestión de dominios y sitios web
pago          → Pagos, suscripciones, tarjetas y comisiones
soporte       → Tickets y niveles de soporte
distribuidor  → Gestión de distribuidores
cifrado       → Algoritmos de cifrado de información sensible
common/shared → Utilidades y componentes transversales
```

---

## 🎯 Patrones de diseño

El proyecto incorpora **patrones de diseño GoF** verificados en el código fuente:

| Patrón | Aplicación | Ubicación |
|---|---|---|
| **Factory Method** | Creación de usuarios | `usuario/domain/UsuarioFactory` |
| **Abstract Factory** | Creación de planes de hosting | `plan/domain/HostingPlanFactory` (`WindowsHostingFactory`, `UnixHostingFactory`) |
| **Strategy** | Métodos de pago | `pago/domain/EstrategiaPago` (`PagoVisa`, `PagoMastercard`, `PagoDiners`) |
| **Strategy** | Cálculo de comisiones | `pago/domain/EstrategiaComision` (`ComisionBasica`, `ComisionPremium`) |
| **Strategy** | Algoritmos de cifrado | `cifrado/domain/EstrategiaCifrado` (`CifradoAES`, `CifradoDES`, `CifradoRSA`) |
| **Adapter** | Integración con registradores de dominios | `dominio/domain/RegistradorDominio` |
| **Builder** | Construcción de solicitudes XML | `dominio/domain/Builder` |
| **Chain of Responsibility** | Escalamiento de tickets de soporte | `soporte/domain/ManejadorSoporte` (`SoporteNivel1/2/3`) |

---

## 📂 Estructura del proyecto

```text
chibchaweb/
│
├── frontend/                     # Aplicación SPA (React + Vite + TypeScript)
│   └── src/
│       ├── api/                  # Clientes HTTP por dominio (auth, clients, domains, payments...)
│       ├── components/           # Componentes comunes, layouts, tablas y UI
│       ├── constants/            # Configuración (API_BASE_URL, credenciales)
│       ├── hooks/                # Custom hooks
│       ├── mock/                 # Datos de prueba
│       ├── pages/                # Vistas por rol: admin, client, employee, auth, public
│       ├── routes/               # Definición y protección de rutas
│       ├── types/                # Tipos TypeScript
│       └── utils/                # Utilidades
│
├── backend/                      # API REST (Spring Boot)
│   ├── Dockerfile
│   └── chibchaweb/
│       ├── pom.xml
│       ├── seed-demo-data.sql
│       ├── docs/                 # Modelos UML, diagramas y scripts SQL
│       └── src/main/java/com/chibchaweb/chibchaweb/
│           ├── acceso/           # Módulo de autenticación
│           ├── usuario/          # Gestión de usuarios
│           ├── plan/             # Gestión de planes de hosting
│           ├── dominio/          # Gestión de dominios
│           ├── pago/             # Gestión de pagos
│           ├── soporte/          # Gestión de soporte técnico
│           ├── distribuidor/     # Gestión de distribuidores
│           ├── cifrado/          # Algoritmos de cifrado
│           ├── common/ shared/   # Componentes transversales
│           │
│           │   # Cada módulo se divide en:
│           │   #   domain/  application/  infrastructure/  presentation/
│
├── docker-compose.yml            # Orquestación (frontend + backend + base de datos)
└── README.md
```

---

## 🚀 Instalación y ejecución

### 📋 Prerrequisitos

- [Docker](https://www.docker.com/) y Docker Compose _(opción recomendada)_ **o**
- Node.js 20+, Java 21 y Maven para ejecución manual.

### 🐳 Opción 1 — Docker Compose (recomendada)

Levanta base de datos, backend y frontend con un solo comando:

```bash
docker compose up --build
```

| Servicio | URL |
|---|---|
| Frontend | http://localhost |
| API REST | http://localhost:8080 |
| PostgreSQL | `localhost:5432` |

### 💻 Opción 2 — Ejecución manual

**Backend** (Spring Boot + Maven Wrapper):

```bash
cd backend/chibchaweb
./mvnw spring-boot:run
```

**Frontend** (Vite):

```bash
cd frontend
npm install
npm run dev
```

La aplicación quedará disponible en `http://localhost:5173`.

---

## ⚙️ Variables de entorno

### Backend

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `SPRING_DATASOURCE_URL` | URL JDBC de la base de datos | Postgres local / Neon |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos | `chibcha_user` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos | `123456` (local) |
| `PORT` | Puerto del servidor | `8080` |

### Frontend

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `VITE_API_URL` | URL base de la API REST | `http://localhost:8080/api` |

---

## 👥 Roles y portales

La interfaz está diseñada manteniendo separación por roles:

| Portal | Descripción |
|---|---|
| 🧑‍💼 **Cliente** | Dashboard, planes, dominios, pagos, tarjetas, soporte y perfil. |
| 👔 **Empleado** | Dashboard y gestión de tickets de soporte. |
| 🛡️ **Administrador** | Clientes, empleados, distribuidores, planes, dominios y pagos. |
| 🌍 **Público** | Landing page, inicio de sesión y registro. |

---

## 📊 Estado del proyecto

> 🚧 **Proyecto en desarrollo.**

- [x] Diseño arquitectónico (DDD)
- [x] Modelado UML y modelo de dominio
- [x] Backend REST con Spring Boot
- [x] Autenticación y autorización con JWT
- [x] Persistencia objeto-relacional (PostgreSQL)
- [x] Interfaz gráfica en React
- [x] Navegación y rutas por roles
- [x] Contenerización con Docker
- [ ] Pruebas automatizadas
- [ ] Despliegue en producción

---

## ✍️ Autor

<div align="center">

**David Fernando Marín Lancheros**
Ingeniería de Sistemas

</div>

---

## 📄 Licencia

Proyecto desarrollado con **fines académicos**.
