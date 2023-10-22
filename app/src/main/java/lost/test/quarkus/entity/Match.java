package lost.test.quarkus.entity;

import io.hypersistence.utils.hibernate.type.range.PostgreSQLRangeType;
import io.hypersistence.utils.hibernate.type.range.Range;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Type;

import java.time.ZonedDateTime;
import java.util.Set;

@Schema(description = "格斗比赛")
@RegisterForReflection
@Entity
@Table(
    name = "match"
)
public class Match extends PanacheEntity {

    @Schema(description = "参赛格斗家列表")
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "match")
    public Set<MatchFighter> matchFighters;


    @Schema(description = "回合列表")
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "match")
    public Set<MatchRound> matchRounds;

    @Schema(description = "时间范围")
    @Type(PostgreSQLRangeType.class)
    @Column(columnDefinition = "tstzrange", nullable = false)
    public Range<ZonedDateTime> timeRange;


    @Schema(description = "创建时间")
    @Column(nullable = false)
    public ZonedDateTime createdAt;

    @Schema(description = "最近更新时间")
    public ZonedDateTime updatedAt;
}
