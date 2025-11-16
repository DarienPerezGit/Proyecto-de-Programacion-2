# Sistema de Gestión de Holding - Cablevisión

Sistema empresarial para la gestión de un holding multinacional desarrollado con Spring Boot siguiendo principios SOLID y arquitectura por capas.

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA** - Persistencia de datos
- **Spring Security** - Seguridad y autenticación basada en roles
- **H2 Database** - Base de datos embebida
- **Hibernate** - ORM
- **Lombok** - Reducción de código boilerplate
- **SpringDoc OpenAPI** - Documentación de API
- **Maven** - Gestión de dependencias

## 🗂️ Arquitectura del Proyecto

El proyecto sigue una arquitectura por capas siguiendo los principios SOLID:

```
src/main/java/com/holding/cablevision/
├── config/              # Configuraciones (Security, OpenAPI, DataInitializer)
├── controller/          # Controladores REST por rol de usuario
├── dto/                 # Objetos de transferencia de datos
├── exception/           # Manejo de excepciones
├── model/               # Entidades JPA del dominio empresarial
├── repository/          # Repositorios Spring Data
└── service/             # Interfaces y servicios de negocio
    └── impl/            # Implementaciones de servicios
```

## 📄 Diagramas UML

Los diagramas UML del sistema están disponibles en la carpeta `docs/uml`:
- **Diagrama de Clases:** `docs/uml/diagrama_clases.png`
- **Diagrama de Casos de Uso:** `docs/uml/diagrama_casos_uso.png`

## 📚 Documentación Javadoc

La documentación generada con Javadoc está disponible en la carpeta `docs/javadoc`. Para visualizarla, abre el archivo `index.html` en un navegador web.

## 🔧 Configuración y Ejecución

### Requisitos Previos
- Java 17 o superior
- Maven 3.6+

### Compilar el Proyecto
```bash
mvn clean install
```

### Ejecutar la Aplicación
```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

### Ejecución desde Consola
Si el sistema requiere entrada y salida exclusivamente por consola, ejecuta el archivo `.jar` generado:
```bash
java -jar target/cablevision-0.0.1-SNAPSHOT.jar
```

### Documentación API (Swagger)
Una vez iniciada la aplicación, accede a:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`

## 🏆 Características Implementadas

✅ Gestión de empresas, vendedores, asesores, países y áreas
✅ Relación jerárquica entre vendedores
✅ Relación entre empresas, áreas y países
✅ Persistencia con JPA/Hibernate
✅ Seguridad con Spring Security
✅ Documentación API con Swagger
✅ Diagramas UML y documentación Javadoc

## 👨‍💻 Autor

Proyecto desarrollado para la materia Programación II.

## 📜 Licencia

Este proyecto es de uso académico.
