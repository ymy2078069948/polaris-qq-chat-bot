package yuki.mirai.demo.pojo.plugin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.message.data.MessageChain;
import yuki.mirai.demo.pojo.OneMessageEvent;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginMessage {
    private long time;

    // client to server is [command|code] server to client is msg
    private String messageType;

    private String message;

    public PluginMessage(OneMessageEvent oneMessageEvent){
        time = oneMessageEvent.getTime();
        messageType = "msg";
        message = MessageChain.serializeToJsonString(oneMessageEvent.getMessageEvent().getMessage());
    }
}
