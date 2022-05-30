package yuki.mirai.demo.function;

import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import yuki.mirai.demo.pojo.OneMessageEvent;

import java.io.IOException;

public interface Function {
     String getName();
     Boolean IsUsing();
     Boolean getResponse(OneMessageEvent event) throws InterruptedException, IOException;
     Boolean setFunction(OneMessageEvent event);
}
