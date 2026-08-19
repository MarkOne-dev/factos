package pe.factos.billing.infrastructure.persistence;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import pe.factos.TestPersistenceApplication;

@SpringBootTest(classes = TestPersistenceApplication.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:factos_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.flyway.enabled=true"
})
public abstract class BasePersistenceTest {
}
