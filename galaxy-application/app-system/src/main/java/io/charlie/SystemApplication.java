package io.charlie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Charlie Zhang
 * @version v1.0
 * @date 19/04/2025
 * @description 系统服务启动类
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCaching  // 开启缓存
public class SystemApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SystemApplication.class, args);
        Environment env = run.getEnvironment();
        log.info("""
                                                \s
                         -------------------------------------------------
                             Application is running! Access URLs:
                             Local:    http://localhost:{}
                             Doc:      http://localhost:{}/doc.html
                         -------------------------------------------------
                        \s""",
                env.getProperty("server.port"),
                env.getProperty("server.port")
        );
    }
}
