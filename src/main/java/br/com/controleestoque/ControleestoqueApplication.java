package br.com.controleestoque;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ControleestoqueApplication {

    public static void main(String[] args) {
        String runMigrationsOnly = System.getenv("RUN_MIGRATIONS_ONLY");
        if ("true".equalsIgnoreCase(runMigrationsOnly)) {
            // Start minimal Spring context, run Flyway, then exit
            ConfigurableApplicationContext ctx = SpringApplication.run(ControleestoqueApplication.class, args);
            try {
                Flyway flyway = ctx.getBean(Flyway.class);
                flyway.migrate();
            } finally {
                ctx.close();
            }
            // Exit successfully after migrations
            return;
        }
        SpringApplication.run(ControleestoqueApplication.class, args);
    }
}
