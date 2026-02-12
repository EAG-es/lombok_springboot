# Lombok with Spring Boot Microservices Case Study

This project explores the implementation of Lombok in a microservices architecture using Spring Boot 3.3.4 and Java 17.

## Architecture
The project consists of three microservices:
- **Product Service**: Manages product catálogo (Port 8081)
- **Inventory Service**: Manages stock levels (Port 8082)
- **Order Service**: Manages customer orders (Port 8083)

## Lombok Usage
We use several Lombok annotations to reduce boilerplate:
- `@Data`: Generates getters, setters, toString, equals, and hashCode.
- `@NoArgsConstructor` & `@AllArgsConstructor`: Generates constructors.
- `@Builder`: Implements the Builder pattern for fluent object creation.
- `@RequiredArgsConstructor`: Facilitates constructor-based dependency injection.
- `@Slf4j`: Provides a logger instance.

## API Endpoints

### Product Service
- `GET /products` - List all products
- `GET /products/{id}` - Get product by ID
- `POST /products` - Create product
- `PUT /products/{id}` - Update product
- `DELETE /products/{id}` - Delete product

### Inventory Service
- `GET /inventory/{productId}` - Get stock level
- `POST /inventory` - Update stock level

### Order Service
- `POST /orders` - Place new order
- `GET /orders/{id}` - Get order details
- `DELETE /orders/{id}` - Cancel order

## API Documentation (Swagger UI)
Each service provides interactive API documentation:
- **Product Service**: `http://localhost:8081/swagger-ui/index.html`
- **Inventory Service**: `http://localhost:8082/swagger-ui/index.html`
- **Order Service**: `http://localhost:8083/swagger-ui/index.html`

## Running the Project

### Prerequisites
- JDK 17
- Maven
- Docker & Docker Compose (optional)

### Build
```bash
mvn clean package
```

### Run with Docker
```bash
docker-compose up --build
```

### Run Locally
Ensure a MySQL instance is running with databases: `product_db`, `inventory_db`, `order_db`.
Then run each service:
```bash
# In separate terminals
java -jar product-service/target/*.jar
java -jar inventory-service/target/*.jar
java -jar order-service/target/*.jar
```

## Testing
The project includes:
- **Unit Tests**: Verifying Lombok generated methods in DTOs and Models.
- **Integration Tests**: Using MockMvc and H2 database to verify REST endpoints.

Run tests with:
```bash
mvn test
```
