package lost.test.quarkus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(description = "FighterCreate")
public record FighterCreate(

    @Pattern(regexp = Patterns.name)
    @Schema(description = "名称")
    String name,
    @NotEmpty
    @Schema(description = "技能名称列表")
    List<@Pattern(regexp = Patterns.name) String> skill) {
}

