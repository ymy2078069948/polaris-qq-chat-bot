package polaris.core;

import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import polaris.core.pojo.set.Config;
import polaris.core.pojo.set.ConfigVariable;

import java.io.File;
import java.io.IOException;

@EnableScheduling
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
