package lost.test.quarkus;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

import static java.time.OffsetDateTime.now;
import static java.time.ZoneOffset.UTC;
import static lost.test.quarkus.Result.ok;

@Tag(name = "fighter", description = "fighter api")
@Path("/fighter")
public class FighterController {
    @Operation(summary = "查询所有 fighter")
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<List<Fighter>> findAll() {
        return ok(Fighter.findAll().list());
    }

    @Operation(summary = "查询一个 fighter, by name")
    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<Fighter> findByName(@PathParam("name") String name) {
        return ok((Fighter) Fighter.find("name", name).singleResultOptional().orElse(null));
    }

    @Operation(summary = "新增一个 fighter")
    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Result<Fighter> add(@Valid FighterCreate fighterCreate) {
        var fighterInsert = FighterMapper.instance.fromFighterCreate(fighterCreate);
        fighterInsert.createdAt = now(UTC);
        fighterInsert.persistAndFlush();
        return ok(fighterInsert);
    }

    @Operation(summary = "编辑一个 fighter")
    @PUT
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Result<Fighter> edit(@Valid FighterEdit fighterEdit) {
        var fighterUpdate = (Fighter) Fighter.find("name", fighterEdit.name()).singleResult();
        fighterUpdate.skill = fighterEdit.skill();
        fighterUpdate.updatedAt = now(UTC);
        fighterUpdate.persistAndFlush();
        return ok(fighterUpdate);
    }

    @Operation(summary = "删除一个 fighter")
    @DELETE
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Result<Fighter> delete(@PathParam("name") String name) {
        var found = Fighter.find("name", name).singleResultOptional();
        found.ifPresent(PanacheEntityBase::delete);
        return ok((Fighter) found.orElse(null));
    }
}
