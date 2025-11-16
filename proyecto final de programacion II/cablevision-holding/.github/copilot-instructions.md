# Instrucciones para GitHub Copilot - Sistema de Gestión de Holding

## 📋 Contexto del Proyecto

Este es un **Sistema de Gestión de Holding Empresarial** desarrollado en **Spring Boot 3.1.5** con **Java 17**. El sistema permite administrar un holding multinacional con tres tipos de usuarios: Administradores, Vendedores y Asesores.

## 🎯 Dominio de Negocio

### Entidades Principales
- **Usuario** (clase abstracta base)
  - **Vendedor**: Gestiona captaciones y jerarquías
  - **Asesor**: Especialista en empresas por área de mercado
  - **Administrador**: Control total del sistema

- **Empresa**: Entidades del holding con información completa
- **País**: Información geográfica y económica
- **AreaMercado**: Sectores (Telecom, Streaming, Fintech)
- **Captación**: Registro de nuevas incorporaciones
- **AsesorEmpresaArea**: Relación M:N con metadata

### Reglas de Negocio
1. **Vendedores** tienen jerarquías (superior/subordinados)
2. **Asesores** se asignan por empresa y área de mercado
3. **Captaciones** las pueden realizar vendedores y administradores
4. **Acceso basado en roles** estricto por endpoint

## 🏗️ Arquitectura y Patrones

### Stack Técnico
- **Framework**: Spring Boot 3.1.5
- **Java**: 17 (usa records, pattern matching, nuevas APIs)
- **Database**: H2 embebida (desarrollo)
- **Security**: Spring Security con roles
- **ORM**: Spring Data JPA + Hibernate
- **Documentation**: OpenAPI 3 (Swagger)
- **Build**: Maven

### Estructura de Capas
```
controller/     # REST endpoints por rol (@PreAuthorize)
├── AdministradorController
├── VendedorController  
└── AsesorController

service/        # Lógica de negocio + conversión DTOs
├── VendedorService
├── EmpresaService
├── AsesorService
└── impl/

repository/     # Spring Data JPA
model/          # JPA Entities (@Entity, @JoinTable)
dto/           # Data Transfer Objects
config/        # Security, OpenAPI, DataInitializer
```

### Convenciones de Código
- **DTOs**: Siempre para transferencia entre capas
- **Services**: Interfaces con impl separados
- **Controllers**: Un controlador por rol de usuario
- **Security**: `@PreAuthorize` en todos los endpoints
- **Naming**: Español para dominio, inglés para técnico

## 🔒 Seguridad Implementada

### Roles y Acceso
- **ADMIN**: Acceso total (`/api/admin/**`)
- **VENDEDOR**: Operaciones de vendedor (`/api/vendedor/**`)
- **ASESOR**: Consultas de asesor (`/api/asesor/**`)

### Usuarios de Prueba
- `admin/admin123` (ADMIN)
- `vendedor1/password` (VENDEDOR)
- `asesor1/password` (ASESOR)

## 🎨 Pautas para Desarrollo

### Al Crear Nuevas Funcionalidades
1. **Verificar rol**: ¿Qué tipo de usuario puede acceder?
2. **Definir DTO**: Crear DTO específico para la respuesta
3. **Service Layer**: Lógica en service, no en controller
4. **Seguridad**: Siempre `@PreAuthorize` con rol correcto
5. **Validación**: `@Valid` en requests, validaciones en service

### Estilo de Código
- **Métodos**: Nombres descriptivos en español del dominio
- **Variables**: camelCase, nombres claros
- **Constants**: UPPER_SNAKE_CASE
- **Packages**: Inglés técnico estándar
- **Comments**: Solo para lógica compleja

### Ejemplos de Endpoints Típicos
```java
@RestController
@RequestMapping("/api/vendedor")
@PreAuthorize("hasRole('VENDEDOR')")
public class VendedorController {
    
    @GetMapping("/perfil")
    public ResponseEntity<VendedorDTO> obtenerMiPerfil(Authentication auth) {
        // Lógica para obtener datos del vendedor autenticado
    }
    
    @PostMapping("/captacion")
    public ResponseEntity<CaptacionDTO> realizarCaptacion(@Valid @RequestBody CaptacionDTO dto) {
        // Lógica para registrar nueva captación
    }
}
```

### DTOs Pattern
- **Request DTOs**: Para datos de entrada con validaciones
- **Response DTOs**: Para respuestas con datos específicos del rol
- **Service**: Siempre convertir Entity ↔ DTO en service layer

## 📊 Base de Datos

### Relaciones Principales
- **Usuario** → **Vendedor** (herencia JOINED)
- **Usuario** → **Asesor** (herencia JOINED)
- **Empresa** ←→ **País** (ManyToMany + ManyToOne)
- **Empresa** ←→ **AreaMercado** (ManyToMany)
- **Asesor** ←→ **Empresa** ←→ **AreaMercado** (tabla intermedia)

### Datos Iniciales (DataInitializer)
- 3 países con datos económicos reales
- 3 áreas de mercado del sector tech/telecom
- 3 empresas del holding con relaciones establecidas
- Jerarquía de vendedores multinivel
- Asesores con asignaciones específicas

## 🧪 Testing

### Datos para Pruebas
- Usar usuarios predefinidos en DataInitializer
- Empresas: "Cablevision", "Flow", "Telecom Brasil"
- Areas: "Telecom", "Streaming", "Fintech"
- Países: Argentina, Brasil, Chile

### Casos de Prueba Típicos
- Login con diferentes roles
- CRUD por rol (admin vs vendedor vs asesor)
- Validaciones de seguridad (@PreAuthorize)
- Consultas con filtros por empresa/área

## 🚨 Consideraciones Importantes

### Seguridad
- **NUNCA** exponer endpoints sin `@PreAuthorize`
- **SIEMPRE** validar que el usuario puede acceder a sus propios datos
- **DTOs obligatorios**: No exponer entities directamente

### Performance
- Usar `@Query` para consultas complejas
- Considerar lazy loading en relaciones
- DTOs con datos justos y necesarios

### Mantenibilidad
- Service interfaces para facilitar testing
- Separación clara: Controller → Service → Repository
- Excepciones específicas del dominio cuando sea necesario

## 📚 Recursos de Referencia

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **H2 Console**: `http://localhost:8080/h2-console`
- **Logs**: Configurados en `application.properties`
- **Database**: JDBC URL: `jdbc:h2:mem:holding`

## 💡 Tips para Copilot

1. **Contexto**: Siempre considerar el rol del usuario autenticado
2. **DTOs First**: Crear DTOs antes que endpoints
3. **Security**: Verificar `@PreAuthorize` en cada sugerencia
4. **Spanish Domain**: Usar nombres de métodos en español para el dominio
5. **Validation**: Incluir validaciones apropiadas (`@Valid`, `@NotNull`, etc.)

---
*Este sistema gestiona el holding Cablevision con foco en vendedores, asesores y empresas multinacionales. Priorizar siempre la seguridad y la separación de responsabilidades por rol.*