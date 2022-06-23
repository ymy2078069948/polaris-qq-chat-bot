package polaris.core;

import net.mamoe.mirai.event.ListenerHost;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import polaris.core.pojo.set.Config;
import polaris.core.pojo.set.ConfigVariable;
import polaris.core.service.LoginService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Component
public class HandlerSetter {
    @Resource(name = "initLogin")
    LoginService loginService;

    @PostConstruct
    public void Init() throws IOException {
        Yaml yml = new Yaml(new Constructor(Config.class));
        Set.CONFIG = yml.load(FileUtils.openInputStream(new File("config.yml")));
        Set.CONFIG_VARIABLE = new ConfigVariable();

        if (loginService.loginBot(0L,"","")){
            RunVariable.BOT.getLogger().info("config.yml 存在账号密码,使用内账号密码登录");
        }else {
            RunVariable.BOT.getLogger().warning("config.yml不存在默认账号密码,请通过WebUI登录");
        }
        setHandler();
    }

    public void setHandler() {
        for (String s : Set.CONFIG.BotSet.ActivateHandler) {
            try {
                RunVariable.BOT.getEventChannel().registerListenerHost((ListenerHost) Class.forName("polaris.core.handlers."+s).getDeclaredConstructor().newInstance());

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void setFunctionKey(){
        Set.FunctionSet.RemindMe.CommandSet.forEach(key -> {
            RunVariable.FunctionKey.put("RemindMe",key);
        });

        Set.FunctionSet.WeiboTop10.CommandSet.forEach(key -> {
            RunVariable.FunctionKey.put("WeiboTop10",key);
        });

        Set.FunctionSet.SpecificReply.CommandSet.forEach(key -> {
            RunVariable.FunctionKey.put("SpecificReply",key);
        });

        Set.FunctionSet.SignIn.CommandSet.forEach(key -> {
            RunVariable.FunctionKey.put("SignIn",key);
        });

        Set.FunctionSet.MusicShare.CommandSet.forEach(key -> {
            RunVariable.FunctionKey.put("MusicShare",key);
        });

        Set.FunctionSet.WeatherInfo.CommandSet.forEach(key -> {
            RunVariable.FunctionKey.put("WeatherInfo",key);
        });

        Set.FunctionSet.BrowserCrawling.TriggerWordList.forEach(key -> {
            RunVariable.FunctionKey.put("BrowserCrawling",key);
        });

        Set.FunctionSet.BaiduImageSearch.CommandSet.forEach(key -> {
            RunVariable.FunctionKey.put("BaiduImageSearch",key);
        });

        Set.FunctionSet.BaiduTranslate.CommandSet.forEach(key -> {
            RunVariable.FunctionKey.put("BaiduTranslate",key);
        });

    }
}
