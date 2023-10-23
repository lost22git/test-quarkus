package lost.test.quarkus.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lost.test.quarkus.common.Patterns;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(title = "FighterEditParam")
public record FighterEditParam(
    @Pattern(regexp = Patterns.name) @Schema(title = "名称") String name,
    @NotEmpty @Schema(title = "技能名称列表") List<@Pattern(regexp = Patterns.name) String> skill) {
}
