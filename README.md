# SERVICIO-PRODUCTO

## 1. Descripción

`servicio-producto` es un microservicio construido con Spring Boot WebFlux (reactivo) cuya responsabilidad es gestionar todos los tipos de productos.

Principales responsabilidades:
- Crear y almacenar productos.

Tecnologías principales: Java, Spring Boot (WebFlux), WebTestClient, Reactor, Spring Data R2DBC (repositorio reactivo).

## 2. Tecnologias

- Java 21
- Maven
- Spring Boot 3.5.15
- JUnit 5, Mockito y Reactor Test
- Base de datos H2 configurada según `application.properties` y el script de esquema `src/main/resources/schema.sql`.


## 3. Construir y ejecutar localmente (Windows)

Pasos mínimos:

1) Compilar y empaquetar:

```powershell
mvn clean install
```

2) Correr el servicio:

```powershell
mvn spring-boot:run
```

3) Correr el servicio mediante docker:

```powershell
docker build -t servicio-producto .
docker run -d -p 8081:8081 --producto servicio-producto-container servicio-producto
```

## 4. Endpoints disponibles

Todos los endpoints están bajo la ruta base `/products` (ver `ProductController`).


```json
{
  "name":"Inka Kola",
  "price":2.50,
  "stock":19
}
```


## 5. Base de datos y `schema.sql`

- El archivo `src/main/resources/schema.sql` contiene el script para crear las tablas iniciales usadas por el servicio.
- Archivo `src/main/resources/application.properties` para ver la configuración de la conexión a la base de datos (driver, URL, usuario/password).

