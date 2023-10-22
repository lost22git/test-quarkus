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

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * NOTE:
 * JPA not support record entity since it depend on reflection of no-arg constructor and getter/setter
 */
@Schema(description = "格斗家")
@RegisterForReflection
@Entity
@Table(
    name = "fighter",
    indexes = {@Index(name = "uk-name", unique = true, columnList = "name")})
public class Fighter extends PanacheEntity {
    @Schema(description = "名称")
    @Column(nullable = false)
    public String name;

    @Schema(description = "技能名称列表")
    //    @Convert(converter = StringListConverter.class)
    @Type(ListArrayType.class)
    @Column(columnDefinition = "text[]")
    public List<String> skill;

    @Schema(description = "创建时间")
    @Column(nullable = false)
    public ZonedDateTime createdAt;

    @Schema(description = "最近修改时间")
    public ZonedDateTime updatedAt;

}
