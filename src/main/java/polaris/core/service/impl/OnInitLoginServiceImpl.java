package polaris.core.service.impl;

import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.stereotype.Service;
import polaris.core.HandlerSetter;
import polaris.core.RunVariable;
import polaris.core.Set;
import polaris.core.service.LoginService;

import java.io.File;

@Service("initLogin")
public class OnInitLoginServiceImpl implements LoginService {
    @Override
    public Boolean loginBot(Long qq, String password, String protocol) {
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
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean switchBot(Long qq, String password, String protocol) {
        return null;
    }

    @Override
    public Boolean closeBot() {
        return null;
    }
}
