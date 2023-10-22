package lost.test.quarkus;

import io.quarkus.hibernate.validator.runtime.jaxrs.ResteasyReactiveViolationException;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import lost.test.quarkus.common.Result;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ErrorMapper {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorMapper.class);

    @ServerExceptionMapper
    public Response map(MyError err) {
        LOG.error(err.getClass().getSimpleName() + ": ", err);
        var result = Result.err(9999, err.getMessage());
        return Response.ok(result).build();
    }

    // hibernate orm
    @ServerExceptionMapper
    public Response map(ConstraintViolationException err) {
        LOG.error(err.getClass().getSimpleName() + ": ", err);
        if (err.getMessage().contains("time_range")) {
            var result = Result.err(9400, "时间范围冲突");
            return Response.ok(result).build();
        } else {
            var result = Result.err(9400, err.getMessage());
            return Response.ok(result).build();
        }
    }

    // hibernate validator
    @ServerExceptionMapper
    public Response map(ValidationException err) {
        LOG.error(err.getClass().getSimpleName() + ": ", err);
        var result = Result.err(9400, err.getMessage());
        return Response.ok(result).build();
    }

    @ServerExceptionMapper
    public Response map(ResteasyReactiveViolationException err) {
        LOG.error(err.getClass().getSimpleName() + ": ", err);
        var result = Result.err(9400, err.getConstraintViolations().iterator().next().getMessage());
        return Response.ok(result).build();
    }
}
