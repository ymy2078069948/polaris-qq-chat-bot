package polaris.core.handlers;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.NudgeEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polaris.core.Set;
import polaris.core.utils.MessageBuilder;

import java.util.Objects;

public class NudgeEventHandler extends SimpleListenerHost {
    Logger logger = LoggerFactory.getLogger("EventHandler");
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception){
        exception.printStackTrace();
        // 处理事件处理时抛出的异常
    }


    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull NudgeEvent event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理
        logger.info("[event]    "+event);
        if (event.getTarget().getId() == event.getBot().getId()){
            if (Set.CONFIG_VARIABLE.NudgeTimes.containsKey(event.getFrom().getId())){
                if (Set.CONFIG_VARIABLE.NudgeTimes.get(event.getFrom().getId())>Set.CONFIG.Bot.ToNudgeSay.size()){
                    Set.CONFIG_VARIABLE.NudgeTimes.remove(event.getFrom().getId());
                }else {
                    int times = Set.CONFIG_VARIABLE.NudgeTimes.get(event.getFrom().getId());
                    if (times<=Set.CONFIG.Bot.ToNudgeSay.size()){
                        event.getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(Set.CONFIG.Bot.ToNudgeSay.get(times - 1), event.getSubject())));
                        Set.CONFIG_VARIABLE.NudgeTimes.put(event.getFrom().getId(),times+1);
                    }else {
                        event.getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(Set.CONFIG.Bot.ToNudgeSay.get(Set.CONFIG.Bot.ToNudgeSay.size() - 1), event.getSubject())));
                    }
                }

            }else {
                Set.CONFIG_VARIABLE.NudgeTimes.put(event.getFrom().getId(),1);
            }

        }
        return ListeningStatus.LISTENING; // 表示继续监听事件
        // return ListeningStatus.STOPPED; // 表示停止监听事件
    }

}
