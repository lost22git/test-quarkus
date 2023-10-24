package lost.test.quarkus.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Type;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Schema(title = "Fighter")
@RegisterForReflection
@Entity
@Table(
    name = "fighter",
    indexes = {@Index(name = "uk-name", unique = true, columnList = "name")})
public class Fighter extends PanacheEntity {
    @Schema(title = "名称")
    @Column(nullable = false)
    public String name;

    @Schema(title = "技能名称列表")
    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    public List<String> skill;

    @Schema(title = "创建时间")
    @Column(nullable = false)
    public ZonedDateTime createdAt;

    @Schema(title = "最近修改时间")
    public ZonedDateTime updatedAt;

    public static List<Fighter> findByIds(Collection<Long> fighterIds) {
        return Fighter.find("id in (?1)", fighterIds).list();
    }

    public static Optional<Fighter> findOneByName(String name) {
        return Fighter.find("name", name).singleResultOptional();
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Fighter fighter = (Fighter) object;
        return Objects.equals(id, fighter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
