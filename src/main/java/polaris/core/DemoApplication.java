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
    public static void main(String[] args) throws IOException {
        SpringApplication.run(DemoApplication.class, args);

        Yaml yml = new Yaml(new Constructor(Config.class));
        Set.CONFIG = yml.load(FileUtils.openInputStream(new File("config.yml")));
        Set.CONFIG_VARIABLE = new ConfigVariable();
        if (OnStartLogin()){
            RunVariable.BOT.getLogger().info("config.yml 存在账号密码,使用内账号密码登录");
        }else {
            RunVariable.BOT.getLogger().warning("config.yml不存在默认账号密码,请通过WebUI登录");
        }
    }

    private static Boolean OnStartLogin() {
        if (Set.CONFIG.BotSet.QQ != null && Set.CONFIG.BotSet.Password != null) {
            RunVariable.BOT = BotFactory.INSTANCE.newBot(Set.CONFIG.BotSet.QQ,Set.CONFIG.BotSet.Password,new BotConfiguration(){
                {
                    setWorkingDir(new File(Set.CONFIG.BotSet.Workspace));
                    setCacheDir(new File(Set.CONFIG.BotSet.CacheDir));
                    setHeartbeatStrategy(HeartbeatStrategy.STAT_HB);
                    setProtocol(MiraiProtocol.ANDROID_PHONE);
                    fileBasedDeviceInfo("DeviceInfo.json");
                    enableContactCache();
                    setLoginCacheEnabled(true);
                }
            });
            RunVariable.BOT.login();
            new HandlerSetter().setHandler();
            return true;
        }else {
            return false;
        }

    }

}
