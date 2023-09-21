package lost.test.quarkus;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * NOTE:
 * JPA not support record entity since it depend on reflection of no-arg constructor and getter/setter
 */
@RegisterForReflection
@Entity
@Table(name = "fighter", indexes = {
    @Index(name = "uk_name", unique = true, columnList = "name")
})
public class Fighter extends PanacheEntity {
    @Column(nullable = false)
    public String name;
    @Convert(converter = StringListConverter.class)
    public List<String> skill;
    @Column(nullable = false)
    public OffsetDateTime createdAt;
    public OffsetDateTime updatedAt;

}
