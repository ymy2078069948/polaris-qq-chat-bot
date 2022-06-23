package polaris.core.handlers;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polaris.core.Set;
import polaris.core.pojo.OneMessageEvent;
import polaris.core.utils.MessageBuilder;

import java.util.Objects;

public class WhenCalledEventHandler  extends SimpleListenerHost {
    Logger logger = LoggerFactory.getLogger("EventHandler");
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception){
        exception.printStackTrace();
        // 处理事件处理时抛出的异常
    }


    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull MessageEvent event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理
        OneMessageEvent oneMessageEvent = new OneMessageEvent(event);
        String call = oneMessageEvent.getOriginMessageList().get(0).getContent();
        if (call != null && call.equals(Set.CONFIG.BotSet.Name)){
            event.getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(Set.CONFIG.BotSet.WhenCalled, event.getSubject())));
        }
        return ListeningStatus.LISTENING; // 表示继续监听事件
        // return ListeningStatus.STOPPED; // 表示停止监听事件
    }

}