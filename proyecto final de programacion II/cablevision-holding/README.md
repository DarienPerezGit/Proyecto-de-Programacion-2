# Sistema de Gestión de Holding - Cablevision

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

## 📁 Arquitectura del Proyecto

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

## 🏗️ Principios SOLID Aplicados

### 1. **Single Responsibility Principle (SRP)**
Cada clase tiene una única responsabilidad:
- Servicios específicos por entidad (VendedorService, EmpresaService, etc.)
- Controladores dedicados por rol de usuario
- Repositorios enfocados en acceso a datos

### 2. **Open/Closed Principle (OCP)**
- Interfaces bien definidas permiten extensión sin modificación
- Jerarquía de herencia en entidades (Usuario → Vendedor, Asesor)

### 3. **Liskov Substitution Principle (LSP)**
- Las implementaciones de servicios pueden sustituir sus interfaces
- Herencia apropiada en el modelo de dominio empresarial

### 4. **Interface Segregation Principle (ISP)**
Interfaces específicas y cohesivas:
- `IAtmService` - Autenticación y validación
- `ICuentaService` - Operaciones de cuenta
- `IRetiroService` - Operaciones de retiro
- `IDepositoService` - Operaciones de depósito
- `ISolicitudSaldoService` - Consultas de saldo
- `IDispensadorService` - Gestión del dispensador

### 5. **Dependency Inversion Principle (DIP)**
- Dependencia de abstracciones (interfaces) no de implementaciones
- Inyección de dependencias por constructor
- Inversión de control mediante Spring Framework

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

### Documentación API (Swagger)
Una vez iniciada la aplicación, accede a:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`

### Consola H2
Accede a la consola H2 para ver la base de datos:
- URL: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:file:./data/cablevision_holding`
- **Usuario**: `sa`
- **Contraseña**: `password`

**IMPORTANTE**: Usa exactamente la URL JDBC mostrada arriba. La base de datos se crea en la carpeta `data` dentro del directorio del proyecto.

## 🗄️ Modelo de Datos

### Entidades Principales

##
## Cuenta
- `id`: Long
- `numeroCuenta`: String (único)
- `pin`: String (encriptado)
- `saldo`: BigDecimal
- `activa`: Boolean
- `fechaCreacion`: LocalDateTime
- `ultimaActualizacion`: LocalDateTime

#### Transaccion
- `id`: Long
- `cuenta`: Cuenta (FK)
- `tipo`: Enum (RETIRO, DEPOSITO, CONSULTA_SALDO)
- `monto`: BigDecimal
- `fecha`: LocalDateTime
- `descripcion`: String
- `exitosa`: Boolean
#### Dispensador
- `id`: Long
- `codigo`: String
- `saldoDisponible`: BigDecimal
- `ultimaRecarga`: LocalDateTime
- `activo`: Boolean

#### Usuario (Abstracta)
- `id`: Long
- `nombre`: String
- `direccion`: String

#### Vendedor (extends Usuario)
- Campos heredados de Usuario

#### Asesor (extends Usuario)
- Campos heredados de Usuario

#### Empresa
- `id`: Long
- `nombre`: String
- `pais`: Pais (FK)
- `areasMercado`: Set<AreaMercado>

#### Pais
- `id`: Long
- `nombre`: String
- `codigo`: String
- `empresas`: Set<Empresa>

#### AreaMercado
- `id`: Long
- `nombre`: String
- `descripcion`: String
- `empresas`: Set<Empresa>

## 🔌 API Endpoints

### Retiros
- **POST** `/api/retiros` - Realizar retiro

### Depósitos
- **POST** `/api/depositos` - Realizar depósito

### Consultas
- **POST** `/api/consultas/saldo` - Consultar saldo

## 🔐 Seguridad

- Spring Security configurado con autenticación básica
- Contraseñas encriptadas con BCrypt
- Endpoints públicos: `/api/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`, `/h2-console/**`
- Endpoints protegidos requieren autenticación

## 📊 Datos de Prueba

Al iniciar la aplicación, se crean automáticamente:

### Dispensador
- Código: ATM001
- Saldo: $50,000

### Cuentas de Prueba
1. **Cuenta 1**
   - Número: 1234567890
   - PIN: 1234
   - Saldo: $5,000

2. **Cuenta 2**
   - Número: 9876543210
   - PIN: 4321
   - Saldo: $10,000

## 🧪 Ejemplo de Uso

### Consultar Saldo
```json
POST /api/consultas/saldo
{
  "numeroCuenta": "1234567890",
  "pin": "1234"
}
```

### Realizar Retiro
```json
POST /api/retiros
{
  "numeroCuenta": "1234567890",
  "pin": "1234",
  "monto": 100.00
}
```

### Realizar Depósito
```json
POST /api/depositos
{
  "numeroCuenta": "1234567890",
  "pin": "1234",
  "monto": 500.00
}
```

## 📝 Características Implementadas

✅ Autenticación de usuarios
✅ Consulta de saldo
✅ Retiro de efectivo
✅ Depósito de efectivo
✅ Registro de transacciones
✅ Control de dispensador de efectivo
✅ Validaciones de operaciones
✅ Manejo global de excepciones
✅ Documentación API con Swagger
✅ Base de datos H2 embebida
✅ Seguridad con Spring Security
✅ Inicialización automática de datos

## 🏆 Principios de Diseño Aplicados

- **SOLID**: Todos los principios SOLID implementados
- **DRY** (Don't Repeat Yourself): Código reutilizable
- **KISS** (Keep It Simple, Stupid): Soluciones simples y efectivas
- **YAGNI** (You Aren't Gonna Need It): Solo funcionalidad necesaria
- **Separación de Responsabilidades**: Arquitectura por capas clara
- **Inyección de Dependencias**: Constructor-based DI
- **Programación Orientada a Interfaces**: Abstracción sobre implementación

## 📦 Estructura de Paquetes

- `config`: Configuraciones de Spring (Security, OpenAPI, Inicializador de Datos)
- `controller`: Controladores REST para endpoints
- `dto`: Data Transfer Objects para comunicación API
- `exception`: Manejo centralizado de excepciones
- `model`: Entidades JPA del dominio
- `repository`: Interfaces de acceso a datos
- `service`: Interfaces de lógica de negocio
- `service.impl`: Implementaciones concretas de servicios

## 🔍 Características Técnicas

- **Transacciones**: Gestión declarativa con `@Transactional`
- **Validaciones**: Bean Validation con anotaciones
- **Logging**: SLF4J con Logback
- **ORM**: JPA/Hibernate para persistencia
- **REST**: API RESTful con Spring Web
- **DTOs**: Separación de modelo de dominio y transferencia
- **Exception Handling**: Manejo global con `@ControllerAdvice`

## 👨‍💻 Autor

Proyecto desarrollado para la materia Programación II

## 📄 Licencia

Este proyecto es de uso académico.
