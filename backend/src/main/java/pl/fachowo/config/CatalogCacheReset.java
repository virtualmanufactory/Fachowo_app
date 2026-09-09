package pl.fachowo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CatalogCacheReset implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(CatalogCacheReset.class);
    private final CacheManager cacheManager;

    public CatalogCacheReset(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void run(ApplicationArguments args) {
        for (String name : new String[] {"categories", "voivodeships", "compare", "nip-lookup"}) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        }
        log.info("Wyczyszczono cache katalogu");
    }
}
