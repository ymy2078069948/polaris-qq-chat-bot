package polaris.core.pojo.set;

import org.apache.commons.io.FileUtils;
import polaris.core.Set;
import polaris.core.utils.FileLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigVariable {
    //通用回复
    public Map<String, String> SpecificReply;

    // 音乐数据
    public List<String> MusicData;

    // ban禁词
    public List<String> BanWords = new ArrayList<>();

    // 最后发言时间
    public Long LastSendTime = null;

    public Map<Long,Integer> NudgeTimes = new HashMap<>();

    public Map<String,String> CityCode;



    public ConfigVariable() {
        CityCode = FileLoader.loadSpecificReplyFile("tools/data/cityData.txt");

        Map<String, String> SpecificReplyTxt = FileLoader.loadSpecificReplyFile(Set.CONFIG.FunctionSet.SpecificReply.SpecificReplyFilePath);
        Map<String, String> SpecificReplyLocal = FileLoader.loadSpecificReplyFile(Set.CONFIG.FunctionSet.SpecificReply.SpecificReplyLocalFilePath);
        Map<String, String> SpecificReplyNetwork = FileLoader.loadSpecificReplyFile(Set.CONFIG.FunctionSet.SpecificReply.SpecificReplyNetworkFilePath);
        SpecificReply = new HashMap<>(SpecificReplyTxt);
        SpecificReplyLocal.forEach(
                (key, value) -> SpecificReply.merge(key,value,(v1,v2) ->v1+"&&"+v2)
        );
        SpecificReplyNetwork.forEach(
                (key, value) -> SpecificReply.merge(key,value,(v1,v2) ->v1+"&&"+v2)
        );


        try {
            MusicData = FileUtils.readLines(new File("tools/data/MusicData.txt"),"UTF-8");
            BanWords = FileUtils.readLines(new File(Set.CONFIG.BotSet.BanWordFilePath),"utf-8");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
