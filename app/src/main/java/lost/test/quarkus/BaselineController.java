package lost.test.quarkus;

import io.smallrye.common.annotation.NonBlocking;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static lost.test.quarkus.Result.ok;

@Path("/baseline")
public class BaselineController {

    @NonBlocking
    @GET
    @Path("/text")
    @Produces(MediaType.TEXT_PLAIN)
    public String text() {
        return "lost";
    }


    @NonBlocking
    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Result<String> json() {
        return ok("lost");
    }
}
