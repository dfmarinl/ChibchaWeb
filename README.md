# ChibchaWeb

Plataforma de gestión de hosting web orientada a la administración de usuarios, planes de hosting, dominios, pagos y soporte técnico, desarrollada aplicando principios de arquitectura de software, modelado UML y patrones de diseño GoF.

---

# 📌 Descripción del proyecto

ChibchaWEB es una solución orientada a la gestión integral de servicios de hosting web. El sistema permite administrar clientes, empleados y administradores, así como realizar procesos relacionados con contratación de planes, registro de dominios, gestión de pagos y atención de tickets de soporte.

El proyecto fue diseñado bajo un enfoque orientado a objetos, incorporando modelado de dominio, persistencia objeto-relacional y patrones de diseño para garantizar modularidad, escalabilidad y mantenibilidad.

---

# 🚀 Funcionalidades principales

- Gestión de usuarios y control de acceso por roles.
- Administración de planes de hosting.
- Registro y gestión de dominios.
- Sistema de pagos extensible.
- Gestión de tickets de soporte técnico.
- Arquitectura basada en patrones de diseño.
- Persistencia mediante mapeo objeto-relacional.
- Interfaz gráfica desarrollada en React.

---

# 🏗️ Arquitectura del sistema

El sistema se encuentra dividido en diferentes módulos funcionales:

- Módulo de acceso y autenticación
- Gestión de clientes
- Gestión de empleados
- Gestión de planes hosting
- Gestión de dominios
- Gestión de pagos
- Gestión de soporte técnico

---

# 🎨 Patrones de diseño implementados

El proyecto incorpora diferentes patrones de diseño GoF para resolver necesidades específicas dentro de la arquitectura:

| Patrón | Aplicación |
|---|---|
| Factory Method | Creación de usuarios |
| Abstract Factory | Creación de planes hosting |
| Strategy | Métodos de pago |
| Strategy | Algoritmos de cifrado |
| Adapter | Integración con registradores de dominios |
| Facade | Simplificación del registro de dominios |
| Builder | Construcción de solicitudes XML |
| Chain of Responsibility | Gestión de tickets de soporte |

---

# 🧩 Tecnologías utilizadas

## Frontend
- React
- React Router
- TailwindCSS
- Lucide React

## Backend *(planeado)*
- Java
- Spring Boot

## Persistencia
- ORM / Mapeo Objeto-Relacional
- Base de datos relacional

## Modelado y diseño
- UML
- PlantUML
- Draw.io
- Figma

---

# 📂 Estructura del proyecto

```bash
chibchaweb/
│
├── frontend/
│   ├── src/
│   ├── components/
│   ├── routes/
│   └── assets/
│
├── backend/
│   ├── src/
│   ├── domain/
│   ├── application/
│   ├── infrastructure/
│   └── persistence/
│
├── docs/
│   ├── uml/
│   ├── mockups/
│   └── diagrams/
│
└── README.md
```

---

# 📖 Modelado realizado

El proyecto incluye:

- Modelo de dominio
- Diagramas de clases
- Modelo relacional
- Diagramas con soporte ORM
- Diccionario de clases
- Mapas de navegación
- Mockups de interfaz gráfica

---

# 🖥️ Interfaz gráfica

La interfaz gráfica fue diseñada utilizando Figma y posteriormente implementada en ReactJS, manteniendo separación por roles:

- Portal Cliente
- Portal Administrador
- Portal Empleado

---

# 🔐 Seguridad

El sistema contempla mecanismos de cifrado aplicados a información sensible como:

- Datos de usuario
- Información de tarjetas
- Credenciales de acceso

El cifrado se implementa mediante el patrón Strategy para permitir flexibilidad y extensibilidad de algoritmos.

---

# ⚙️ Ejecución del proyecto

## Frontend

```bash
cd frontend
npm install
npm run dev
```

---

# 📌 Estado del proyecto

🚧 Proyecto en desarrollo.

Actualmente se encuentran implementados:

- Diseño arquitectónico
- Modelado UML
- Navegación frontend
- Mockups
- Estructura inicial en React

---

# 👨‍💻 Autor

**David Fernando Marín Lancheros**  
Ingeniería de Sistemas

---

# 📄 Licencia

Proyecto desarrollado con fines académicos.
