package polaris.core.message.responder.chat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.OnlineAudio;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import polaris.core.RunVariable;
import polaris.core.message.MessageQueue;
import polaris.core.message.collector.chat.*;
import polaris.core.pojo.OriginMessage;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class GroupMessageResponder {
    public static GroupMessageEvent processingGroupMessageEvent = null;

    @PostConstruct
    public void loadCollector(){
        RunVariable.BOT.getEventChannel().registerListenerHost(new GroupMessageCollector());
    }


    @Scheduled(fixedRate = 1000)
    public void responder(){
        if (!MessageQueue.GroupMessageQueue.isEmpty()){
            for (int i = 0; i < MessageQueue.GroupMessageQueue.size(); i++) {
                processingGroupMessageEvent = MessageQueue.GroupMessageQueue.poll();
                JSONArray messageArray = JSONArray.parseArray(MessageChain.serializeToJsonString(processingGroupMessageEvent.getMessage()));
                Queue<OriginMessage> originMessageQueue = new LinkedList<>();
                for (int j = 1; j < messageArray.size();j++ ) {
                    JSONObject jsonObject = messageArray.getJSONObject(i);
                    if (jsonObject.getString("type").equals("Image")) {
                        Image image = Image.fromId(jsonObject.getString("imageId"));
                        jsonObject.put("imageId",Image.queryUrl(image));
                    }
                    originMessageQueue.offer(jsonObject.toJavaObject(OriginMessage.class));
                    functionRunner(originMessageQueue);
                }
            }
        }

    }

    private void functionRunner(Queue<OriginMessage> originMessageQueue){

    }

}
