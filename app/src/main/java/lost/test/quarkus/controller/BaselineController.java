package lost.test.quarkus.controller;

import io.smallrye.common.annotation.NonBlocking;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import lost.test.quarkus.common.Result;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;
import static lost.test.quarkus.common.Result.ok;

@Tag(name = "Baseline API")
@Path("/baseline")
public class BaselineController {

    @NonBlocking
    @GET
    @Path("/text")
    @Produces(TEXT_PLAIN)
    public String text() {
        return "lost";
    }


    @NonBlocking
    @GET
    @Path("/json")
    @Produces(APPLICATION_JSON)
    public Result<String> json() {
        return ok("lost");
    }
}
