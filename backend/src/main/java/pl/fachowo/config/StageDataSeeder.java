package pl.fachowo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Profile("stage")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StageDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(StageDataSeeder.class);

    private final DataSource dataSource;
    private final CacheManager cacheManager;

    public StageDataSeeder(DataSource dataSource, CacheManager cacheManager) {
        this.dataSource = dataSource;
        this.cacheManager = cacheManager;
    }

    @Override
    public void run(ApplicationArguments args) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setSeparator(";");
        populator.setSqlScriptEncoding("UTF-8");
        populator.addScript(new ClassPathResource("db/stage-seed.sql"));
        populator.execute(dataSource);
        for (String name : new String[] {"categories", "voivodeships", "compare", "nip-lookup"}) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        }
        log.info("Tryb stage: dane testowe gotowe. Hasło wszystkich kont demo: password");
        log.info("Logowanie: kowalski@demo.fachowo.pl, hydro@demo.fachowo.pl, instal@demo.fachowo.pl, "
                + "nowak@demo.fachowo.pl, rurka@demo.fachowo.pl, aqua@demo.fachowo.pl, "
                + "klima@demo.fachowo.pl, chlod@demo.fachowo.pl, arctic@demo.fachowo.pl, frost@demo.fachowo.pl, "
                + "elektro@demo.fachowo.pl, prad@demo.fachowo.pl, malarz@demo.fachowo.pl, "
                + "czysty@demo.fachowo.pl, drewno@demo.fachowo.pl, ogrod@demo.fachowo.pl, dach@demo.fachowo.pl");
    }
}
