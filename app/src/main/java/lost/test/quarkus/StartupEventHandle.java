package lost.test.quarkus;

import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import lost.test.quarkus.entity.*;
import lost.test.quarkus.mapper.FighterMapper;
import lost.test.quarkus.model.FighterCreateParam;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.time.ZonedDateTime.now;

@ApplicationScoped
public class StartupEventHandle {
    private static final Logger LOG = LoggerFactory.getLogger(StartupEventHandle.class);
    @ConfigProperty(name = "quarkus.http.port")
    int port;

    @Transactional
    public void initDb(@Observes
                       StartupEvent startupEvent,
                       LaunchMode launchMode
    ) {
        if (LaunchMode.DEVELOPMENT == launchMode) {
            LOG.info("清空所有表");
            MatchRoundFighter.deleteAll();
            MatchRound.deleteAll();
            MatchFighter.deleteAll();
            Match.deleteAll();
            Fighter.deleteAll();
            LOG.info("清空所有表,完成");

            LOG.info("重置所有 sequence");
            var entityManager = Fighter.getEntityManager();
            entityManager.createNativeQuery("""
                ALTER SEQUENCE fighter_seq RESTART;
                ALTER SEQUENCE match_seq RESTART;
                ALTER SEQUENCE match_fighter_seq RESTART;
                ALTER SEQUENCE match_round_seq RESTART;
                 """).executeUpdate();
            LOG.info("重置所有 sequence, 完成");

            LOG.info("初始化数据");
            List<Fighter> initData = List.of(
                FighterMapper.instance.fromFighterCreate(new FighterCreateParam("隆", List.of("波动拳"))),
                FighterMapper.instance.fromFighterCreate(new FighterCreateParam("肯", List.of("升龙拳")))
            );
            initData.forEach(v -> v.createdAt = now());
            Fighter.persist(initData);
            Fighter.flush();
            LOG.info("初始化数据，完成");
        }

        var startupInfo = new StartupInfo(ProcessHandle.current().pid(), port, Runtime.version());
        LOG.info("Startup info: {}", startupInfo);
    }


    record StartupInfo(
        long pid,
        int port,
        Runtime.Version jvmVersion
    ) {
    }
}
