package polaris.core.function.impl;

import net.mamoe.mirai.message.code.MiraiCode;
import org.springframework.stereotype.Service;
import polaris.core.Set;
import polaris.core.function.Function;
import polaris.core.pojo.OneMessageEvent;
import polaris.core.pojo.OriginMessage;
import polaris.core.utils.DownloadMusicList;

import java.io.IOException;
import java.util.Random;

@Service("MusicShare")
public class MusicShareImpl implements Function {
    @Override
    public String getName() {
        return "MusicShare";
    }

    @Override
    public Boolean IsUsing() {
        return Set.CONFIG.FunctionSet.MusicShare.IsUsing;
    }

    @Override
    public Boolean getResponse(OneMessageEvent event) throws IOException, InterruptedException {
        if (Set.CONFIG.FunctionSet.MusicShare.IfNeedAt){
            if (event.getOriginMessageList().toString().contains("At")) {
                return musicSender(event);
            }
        }else {
            return musicSender(event);
        }

        return null;
    }

    private Boolean musicSender(OneMessageEvent event) throws IOException, InterruptedException {
        for (OriginMessage originMessage : event.getOriginMessageList()) {
            if (originMessage.getType().equals("PlainText")){
                String msg = originMessage.getContent();
                if (originMessage.getContent().startsWith(" ")){
                    msg = msg.substring(1);
                }
                if (msg.equals(Set.CONFIG.FunctionSet.MusicShare.ChangePlaylist)){
                    event.getMessageEvent().getSubject().sendMessage("开始更新歌单,请在web启动后扫码登录网易云,并耐心等待");
                    DownloadMusicList.downloadNeteaseCloudMusicList(Set.CONFIG.FunctionSet.MusicShare.Playlist);
                    return true;
                }
                if (Set.CONFIG.FunctionSet.MusicShare.CommandSet.contains(msg)){
                    Random random = new Random();
                    int n = random.nextInt(Set.CONFIG_VARIABLE.MusicData.size());
                    event.getMessageEvent().getSubject().sendMessage(MiraiCode.deserializeMiraiCode(Set.CONFIG_VARIABLE.MusicData.get(n)));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean setFunction(OneMessageEvent event) {
        return false;
    }
}
