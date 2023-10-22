package lost.test.quarkus;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lost.test.quarkus.entity.Fighter;
import lost.test.quarkus.entity.Match;
import lost.test.quarkus.entity.MatchFighter;
import lost.test.quarkus.entity.MatchRound;
import lost.test.quarkus.mapper.FighterMapper;
import lost.test.quarkus.model.FighterCreateParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.time.ZonedDateTime.now;

@ApplicationScoped
public class StartupHandle {

    record StartupInfo(
        long pid,
        int port,
        Runtime.Version jvmVersion
    ) {
    }

    private static final Logger LOG = LoggerFactory.getLogger(StartupHandle.class);

    @IfBuildProfile("dev")
    @Transactional
    public void initDb(@Observes
                       StartupEvent startupEvent
    ) {

        LOG.info("清空所有表");
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
        var initData = List.of(
            FighterMapper.instance.fromFighterCreate(new FighterCreateParam("隆", List.of("波动拳"))),
            FighterMapper.instance.fromFighterCreate(new FighterCreateParam("肯", List.of("升龙拳")))
        );
        initData.forEach(v -> v.createdAt = now());
        Fighter.persist(initData);
        Fighter.flush();
        LOG.info("初始化数据，完成");

//        var startupInfo = new StartupInfo(ProcessHandle.current().pid(), httpServer.actualPort(),
// Runtime.version());
//        LOG.info("Startup info: {}", startupInfo);
    }


    @IfBuildProfile("prod")
    public void print(@Observes StartupEvent startupEvent) {
        System.out.println("-----------> test prod");
    }
}
