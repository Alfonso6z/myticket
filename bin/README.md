# ticketTSIS

Configuración de postgreSQL a nivel local:

src/main/resources/application.properties
```
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.datasource.platform=postgres
spring.datasource.url=jdbc:postgresql://localhost:5432/<mi_base_de_datos>
spring.datasource.username=<username>
spring.datasource.password=<password>
