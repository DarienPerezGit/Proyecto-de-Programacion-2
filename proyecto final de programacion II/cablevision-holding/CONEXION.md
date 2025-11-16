Guía rápida: mostrar SQL al profesor y conectar la app a H2 y a MySQL

Archivos SQL que debes presentar al profesor (explica brevemente cada uno):
- `cablevision_holding_export.sql`  -> Exportación directa desde H2 (formato H2). Útil para mostrar export original.
- `cablevision_holding_mysql.sql`   -> Versión convertida y preparada para MySQL Workbench (lista para importar).
- `database-schema.sql`            -> Script adicional con estructura y ejemplos (plantilla de diseño).

¿Dónde dejarlos?
- Deja los `.sql` (los tres) en el directorio raíz del proyecto o en una carpeta `sql/` para entregarlos.
- La carpeta `data/` idealmente sólo debe contener los archivos H2 (`*.mv.db` y `*.trace.db`).

Cómo importar `cablevision_holding_mysql.sql` en MySQL (dos opciones):

1) MySQL Workbench (GUI)
- Abrir MySQL Workbench y conectarte a tu servidor.
- En la pestaña SQL Editor, abrir `cablevision_holding_mysql.sql`.
- Ejecutar todo el script (botón rayo o Ctrl+Shift+Enter).
- Verifica que la base de datos `cablevision_holding` se haya creado y que las tablas estén pobladas.

2) Línea de comandos (PowerShell) — recomendado si prefieres rapidez:
# Primero crea la BD (si el script no la incluye o falla):
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS cablevision_holding CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
# Importa el script (desde la carpeta del proyecto):
mysql -u root -p cablevision_holding < "cablevision_holding_mysql.sql"

-- Reemplaza `root` y la opción `-p` según tu usuario/contraseña.

Cómo ejecutar la aplicación con MySQL (perfil `mysql`):

Opción A — Maven (PowerShell):
$env:SPRING_PROFILES_ACTIVE = 'mysql'; mvn spring-boot:run

Opción B — java -jar:
# Empaqueta primero
mvn clean package -DskipTests
# Ejecuta con perfil mysql
java -jar -Dspring.profiles.active=mysql target\cablevision-0.0.1-SNAPSHOT.jar

Notas importantes para `application-mysql.properties`:
- Ajusta `spring.datasource.username` y `spring.datasource.password` al usuario MySQL real.
- `spring.jpa.hibernate.ddl-auto=validate` evita que Hibernate modifique la estructura. Cambia a `update` sólo si quieres que Hibernate cree/ajuste tablas (no recomendado en producción).
- Asegúrate de tener el conector MySQL en el `pom.xml` (dependencia `mysql-connector-java`). Si no está, agrega:

<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <scope>runtime</scope>
</dependency>

Cómo usar la base H2 (actual, embebida) — ya presente en `application.properties`:
- Ejecuta la app normalmente (sin perfil): mvn spring-boot:run
- Abre la consola H2: http://localhost:8080/h2-console
- JDBC URL por defecto: jdbc:h2:file:./data/cablevision_holding
- Usuario/Pass: sa / password (según `application.properties` del proyecto)

Consejos sobre la carpeta `data/`:
- Mantén `cablevision_holding.mv.db` y `cablevision_holding.trace.db` dentro de `data/`.
- Mueve `cablevision_holding_export.sql` y `cablevision_holding_mysql.sql` fuera de `data/` a `sql/` o a la raíz del proyecto para entregar.

Verificación rápida (post-import):
- En MySQL Workbench, ejecutar: SELECT COUNT(*) FROM usuario; SELECT COUNT(*) FROM empresa; etc.

Solución de problemas comunes:
- Error de conexión MySQL: verifica que el servidor MySQL esté en ejecución y que el puerto (3306) no esté bloqueado.
- Error de dialecto/ddl: si MySQL tiene tipos distintos, ajusta `spring.jpa.hibernate.ddl-auto` a `update` temporalmente para pruebas.

Si quieres, puedo:
- Añadir el conector MySQL en `pom.xml` si no existe.
- Mover los `.sql` a una carpeta `sql/` y limpiar `data/` (mover archivos no DB fuera).
- Probar arrancar la app con perfil `mysql` desde aquí (si me autorizas a ejecutar maven). 

Dime cuál de estas acciones quieres que haga y la hago.
