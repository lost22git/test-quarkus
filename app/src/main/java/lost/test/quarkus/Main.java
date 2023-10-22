package lost.test.quarkus;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;

@QuarkusMain
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("初始化 jvm 时区：UTC");
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
        LOG.info("初始化 jvm 时区：UTC, 完成");
        Quarkus.run(args);
    }
}
