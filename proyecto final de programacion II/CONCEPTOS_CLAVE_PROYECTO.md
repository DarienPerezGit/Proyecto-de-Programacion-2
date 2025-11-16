# 📋 GUÍA COMPLETA DEL PROYECTO - CyberVision Holding

## 🎯 RESUMEN EJECUTIVO

**CyberVision Holding** es un sistema de gestión empresarial desarrollado como proyecto final de Programación II. Combina un **backend Spring Boot** con un **frontend Bootstrap/JavaScript** para administrar empresas, empleados y operaciones de un holding empresarial.

---

## 🏗️ ARQUITECTURA DEL SISTEMA

### **Stack Tecnológico**
```
Frontend:
├── HTML5 + CSS3
├── Bootstrap 5.3.0
├── JavaScript (ES6+)
├── Font Awesome 6.0.0
└── API REST Consumer

Backend:
├── Spring Boot 3.1.5
├── Spring Data JPA
├── Spring Security
├── H2 Database
├── Maven 3.8+
└── Java 21
```

### **Estructura de Directorios**
```
proyecto/
├── Bootstrap/                 # Frontend
│   ├── css/                  # Estilos personalizados
│   ├── js/                   # Lógica de negocio frontend
│   ├── dashboard-admin.html  # Panel principal
│   └── *.html               # Vistas del sistema
└── cablevision-holding/      # Backend
    ├── src/main/java/        # Código fuente Java
    ├── src/main/resources/   # Configuraciones
    ├── target/               # Compilados
    └── pom.xml              # Dependencias Maven
```

---

## 🗄️ MODELO DE DATOS

### **Entidades Principales**

#### **1. País**
```sql
CREATE TABLE pais (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    capital VARCHAR(100),
    numero_habitantes BIGINT,
    pib DECIMAL(15,2)
);
```

#### **2. Empresa**
```sql
CREATE TABLE empresa (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    ciudad_sede VARCHAR(100),
    facturacion_anual DECIMAL(15,2),
    fecha_entrada_holding DATE,
    numero_vendedores INTEGER,
    pais_sede_id BIGINT
);
```

#### **3. Usuario (Tabla base)**
```sql
CREATE TABLE usuario (
    id BIGINT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    direccion VARCHAR(300)
);
```

#### **4. Vendedor (Hereda de Usuario)**
```sql
CREATE TABLE vendedor (
    id BIGINT PRIMARY KEY,
    codigo_vendedor VARCHAR(10) UNIQUE,
    empresa_id BIGINT,
    vendedor_superior_id BIGINT,
    FOREIGN KEY (id) REFERENCES usuario(id),
    FOREIGN KEY (empresa_id) REFERENCES empresa(id)
);
```

#### **5. Asesor (Hereda de Usuario)**
```sql
CREATE TABLE asesor (
    id BIGINT PRIMARY KEY,
    codigo_asesor VARCHAR(10) UNIQUE,
    titulacion VARCHAR(200),
    FOREIGN KEY (id) REFERENCES usuario(id)
);
```

### **Relaciones Many-to-Many**
- **Empresa ↔ País**: Una empresa puede operar en múltiples países
- **Empresa ↔ Área Mercado**: Una empresa puede tener múltiples áreas
- **Asesor ↔ Empresa**: Un asesor puede trabajar en múltiples empresas

---

## 🚀 FUNCIONALIDADES PRINCIPALES

### **1. Gestión de Países**
- ✅ Crear, Listar, Editar, Eliminar países
- ✅ Validación de datos (PIB, habitantes, capital)
- ✅ Mostrar empresas por país
- ✅ Formato de números (millones para habitantes/PIB)

### **2. Gestión de Empresas**
- ✅ Wizard de creación completa con empleados
- ✅ Asignación de países de operación y sede
- ✅ Selección de áreas de mercado
- ✅ Cálculo automático de facturación
- ✅ Gestión de estados (Activa, Pendiente, Inactiva)

### **3. Gestión de Empleados**
- ✅ Vendedores con jerarquía (captador/captado)
- ✅ Asesores especializados por área
- ✅ Códigos automáticos (V001, A001, etc.)
- ✅ Asignación de credenciales de acceso

### **4. Sistema de Autenticación**
- ✅ Roles: Admin, Vendedor, Asesor
- ✅ Sesiones persistentes
- ✅ Protección de endpoints con Spring Security
- ✅ Modo híbrido (backend + localStorage fallback)

### **5. Dashboards Especializados**
- ✅ **Admin**: Gestión completa del sistema
- ✅ **Vendedor**: CRUD de captaciones y jerarquías
- ✅ **Asesor**: Gestión de asesorías y áreas

---

## 🔧 CONFIGURACIÓN TÉCNICA

### **Base de Datos H2**
```properties
# application.properties
spring.datasource.url=jdbc:h2:file:./data/cablevision_holding
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
```

### **Seguridad (Spring Security)**
```java
@Configuration
public class SecurityConfig {
    // Configuración de autenticación básica
    // CORS habilitado para frontend
    // Endpoints públicos: /api/auth/**
    // Endpoints protegidos: /api/**
}
```

### **API REST Endpoints**

#### **Autenticación**
```http
POST /api/auth/login        # Iniciar sesión
GET  /api/auth/me          # Obtener usuario actual
```

#### **Administración**
```http
# Países
GET    /api/admin/paises
POST   /api/admin/paises
PUT    /api/admin/paises/{id}
DELETE /api/admin/paises/{id}

# Empresas
GET    /api/admin/empresas
POST   /api/admin/empresas/completa
PUT    /api/admin/empresas/{id}
DELETE /api/admin/empresas/{id}

# Empleados
GET    /api/admin/vendedores
POST   /api/admin/vendedores
PUT    /api/admin/vendedores/{id}
DELETE /api/admin/vendedores/{id}
```

---

## 💻 COMPONENTES FRONTEND

### **Archivos JavaScript Principales**

#### **1. api-service.js**
```javascript
// Maneja todas las comunicaciones con el backend
class ApiService {
    constructor(baseURL = 'http://localhost:8080/api')
    async login(credentials)
    async obtenerPaises()
    async crearPais(pais)
    // ... más métodos
}
```

#### **2. auth.js**
```javascript
// Sistema de autenticación y autorización
- Manejo de sesiones
- Validación de roles
- Redirecciones automáticas
- Persistencia en sessionStorage
```

#### **3. admin-crud.js**
```javascript
// Operaciones CRUD para administradores
- cargarTablaPaises()
- nuevoPais(), editarPais(), eliminarPais()
- cargarTablaEmpresas()
- renderizarTablaPaises()
```

#### **4. backend-integration.js**
```javascript
// Integración híbrida Backend + LocalStorage
- Fallback automático si backend no disponible
- Sincronización de datos
- Manejo de errores de conexión
```

#### **5. empresa-wizard.js**
```javascript
// Wizard de creación de empresas completas
- Formulario multi-paso
- Validación en tiempo real
- Creación de empleados asociados
- Generación automática de credenciales
```

### **Archivos CSS Principales**

#### **1. dashboard.css**
```css
/* Estilos del panel administrativo */
- Sidebar navigation
- Dark theme
- Cards y tablas responsivas
- Animaciones y transiciones
```

#### **2. login.css**
```css
/* Estilos del login */
- Formulario centrado
- Validación visual
- Efectos de hover
```

---

## 🔐 SEGURIDAD Y VALIDACIONES

### **Backend (Spring Security)**
- Autenticación básica HTTP
- Protección CSRF deshabilitada para API
- CORS configurado para desarrollo
- Validación con `@Valid` en controladores

### **Frontend**
- Sanitización de inputs
- Validación de formularios
- Manejo seguro de tokens
- Protección contra XSS básica

---

## 📊 FLUJOS DE TRABAJO PRINCIPALES

### **1. Flujo de Autenticación**
```
1. Usuario ingresa credenciales
2. Frontend envía POST /api/auth/login
3. Backend valida y retorna datos del usuario
4. Frontend guarda sesión en sessionStorage
5. Redirección al dashboard correspondiente
```

### **2. Flujo de Creación de Empresa**
```
1. Admin accede al wizard de empresas
2. Completa datos básicos (nombre, sede, etc.)
3. Selecciona países de operación
4. Asigna áreas de mercado
5. Crea vendedores y asesores
6. Sistema genera credenciales automáticamente
7. Empresa se guarda con todas las relaciones
```

### **3. Flujo CRUD de Países**
```
1. Cargar tabla desde backend
2. Mostrar países con formato (habitantes en millones)
3. Edición inline con modal
4. Validación de datos
5. Guardado vía API PUT /api/admin/paises/{id}
6. Actualización automática de la tabla
```

---

## 🧪 TESTING Y DEBUGGING

### **Herramientas Incluidas**
1. **Console H2**: `http://localhost:8080/h2-console`
2. **Swagger UI**: `http://localhost:8080/swagger-ui.html`
3. **Script de limpieza**: `Bootstrap/LIMPIAR-CACHE.html`
4. **Test de conexión**: `Bootstrap/test-backend.html`

### **Logs Importantes**
```javascript
// Frontend (Consola del navegador)
console.log("✅ ApiService inicializado")
console.log("🔐 Usuario autenticado:", user)
console.log("📊 Datos cargados desde backend")

// Backend (Terminal)
INFO: Started CablevisionHoldingApplication
INFO: Tomcat started on port(s): 8080
Hibernate: INSERT INTO pais (...)
```

---

## 🚨 PROBLEMAS COMUNES Y SOLUCIONES

### **1. Error "Failed to load resource: net::ERR_FILE_NOT_FOUND"**
**Causa**: Archivo JavaScript faltante
**Solución**: Verificar rutas en HTML, crear archivo faltante

### **2. "NaNM" en tabla de países**
**Causa**: Campo `habitants` vs `numeroHabitantes`
**Solución**: Usar `(pais.numeroHabitantes || pais.habitantes)`

### **3. "undefined" en datos de empresas**
**Causa**: localStorage con datos viejos
**Solución**: Ejecutar `localStorage.clear(); location.reload();`

### **4. Backend no responde**
**Causa**: Servidor detenido o puerto ocupado
**Solución**: Reiniciar con `java -jar target/cablevision-0.0.1-SNAPSHOT.jar`

### **5. Datos hardcodeados aparecen**
**Causa**: `models.js` con `initialData` poblado
**Solución**: Verificar que `initialData` esté vacío

---

## 📈 MÉTRICAS DEL PROYECTO

### **Estadísticas de Código**
- **Líneas de código Java**: ~2,500 líneas
- **Líneas de código JavaScript**: ~4,000 líneas
- **Archivos HTML**: 6 archivos principales
- **Archivos CSS**: 3 hojas de estilo
- **Endpoints API**: 25+ endpoints

### **Funcionalidades Implementadas**
- ✅ **CRUD completo**: Países, Empresas, Vendedores, Asesores
- ✅ **Autenticación multi-rol**: Admin, Vendedor, Asesor
- ✅ **Base de datos relacional**: 10+ tablas con relaciones
- ✅ **API RESTful**: Documentada con Swagger
- ✅ **Frontend responsivo**: Bootstrap + JavaScript
- ✅ **Validaciones**: Cliente y servidor
- ✅ **Manejo de errores**: Graceful degradation

---

## 🎓 CONCEPTOS ACADÉMICOS APLICADOS

### **Programación Orientada a Objetos**
- Herencia: `Usuario` → `Vendedor`, `Asesor`
- Encapsulación: DTOs para transferencia de datos
- Polimorfismo: Diferentes tipos de usuarios
- Abstracción: Servicios e interfaces

### **Patrones de Diseño**
- **MVC**: Separación clara de responsabilidades
- **Repository**: Abstracción de acceso a datos
- **DTO**: Transferencia segura de datos
- **Facade**: API unificada para frontend

### **Arquitectura de Software**
- **API REST**: Comunicación stateless
- **SPA Híbrida**: Single Page + Server Side
- **Layered Architecture**: Controller → Service → Repository
- **Dependency Injection**: Spring Framework

### **Base de Datos**
- **Normalización**: 3FN aplicada
- **Relaciones**: One-to-Many, Many-to-Many
- **Integridad referencial**: Foreign Keys
- **Índices**: Optimización de consultas

---

## 🚀 COMANDOS DE EJECUCIÓN

### **Iniciar Backend**
```bash
cd cablevision-holding
mvn clean package -DskipTests
java -jar target/cablevision-0.0.1-SNAPSHOT.jar
```

### **Acceder al Frontend**
```bash
# Abrir en navegador
Bootstrap/dashboard-admin.html
```

### **Limpiar Datos Locales**
```bash
# Abrir en navegador
Bootstrap/LIMPIAR-CACHE.html
```

---

## 🎯 CREDENCIALES DE ACCESO

### **Usuario Administrador**
- **Username**: `admin`
- **Password**: `admin`
- **Rol**: Administrador completo

### **Base de Datos H2**
- **URL**: `jdbc:h2:file:./data/cablevision_holding`
- **Username**: `sa`
- **Password**: (vacío)
- **Console**: `http://localhost:8080/h2-console`

---

## 📚 RECURSOS ADICIONALES

### **Documentación Técnica**
- `README.md`: Instrucciones de instalación
- `CONEXION.md`: Guía de conexión backend-frontend
- `EXPLICACION_DTOS.txt`: Documentación de DTOs
- Comentarios inline en código

### **Scripts de Utilidad**
- `limpiar-cache.html`: Limpieza de localStorage
- `test-backend.html`: Test de conectividad
- `sql/database-schema.sql`: Esquema de base de datos

---

*Documento generado automáticamente - Proyecto Final Programación II*
*CyberVision Holding System v1.0*