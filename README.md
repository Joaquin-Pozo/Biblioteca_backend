# 📚 Proyecto Biblioteca (Spring Boot + PostgreSQL)

Aplicación backend desarrollada en **Spring Boot 3.5.7** y **Java 17** que gestiona autores, libros, socios, copias y préstamos de una biblioteca.

---

## ⚙️ Requisitos previos

Asegúrate de tener instalados los siguientes componentes:

| Componente | Versión recomendada |
|-------------|------------------|
| Java | 17 o superior |
| Maven | 3.9+ |
| PostgreSQL | 14.19 o superior |
| Git | (opcional, para clonar el proyecto) |

---

## 🧩 Creación de la base de datos

1. Abrir una terminal Bash o PowerShell.
2. Iniciar sesión con el usuario postgres:
   ```bash
   sudo -u postgres psql
   ```
   o en Windows:
   ```bash
   psql -U postgres
   ```
3. Crear la base de datos y configurar la contraseña:
   ```sql
   CREATE DATABASE bd_biblioteca;
   ALTER USER postgres WITH PASSWORD 'password123';
   \q
   ```
4. Verificar conexión (puerto por default de PostgreSQL = 5432):
   ```bash
   psql -U postgres -d bd_biblioteca -h localhost -p 5432
   ```
   
---

## ⚙️ Configuración de la aplicación

Actualiza el archivo src/main/resources/application.properties:
   ```sql
   spring.datasource.url=jdbc:postgresql://localhost:5432/bd_biblioteca
   spring.datasource.username=postgres
   spring.datasource.password=password123
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   ```

---

## 🚀 Compilar y ejecutar
Desde la raíz del proyecto, ejecuta:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
La aplicación se levantará en: http://localhost:8080