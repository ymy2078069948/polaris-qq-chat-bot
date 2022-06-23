package polaris.core.function;

import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polaris.core.Set;
import polaris.core.WebSocketServer;
import polaris.core.pojo.OneMessageEvent;
import polaris.core.pojo.plugin.PluginMessage;
import polaris.core.utils.MessageBuilder;

import java.io.IOException;
import java.util.Objects;

public class FunctionManager {
    static Logger logger = LoggerFactory.getLogger("FunctionManager");

    public static void onMessage(MessageEvent event) throws InterruptedException, IOException {
        OneMessageEvent oneMessageEvent = new OneMessageEvent(event);
        if (oneMessageEvent.getKind().equals("GROUP")){
            if (Set.CONFIG.BotSet.ApplyGroups.contains(oneMessageEvent.getMessageEvent().getSubject().getId())){
                toApply(oneMessageEvent);
            }
        }else {
            toApply(oneMessageEvent);
        }

    }

    private static void toApply(OneMessageEvent oneMessageEvent) throws InterruptedException, IOException {
        if (!banMessage(oneMessageEvent)){
            if ((oneMessageEvent.getOriginMessageList().toString().contains("C&") || oneMessageEvent.getOriginMessageList().toString().contains("c&")) && oneMessageEvent.getFromId() == Set.CONFIG.BotSet.Admin) {
                logger.info("Message is Command, time ["+oneMessageEvent.getMessageEvent().getTime()+"]");
                if (!isCommand(oneMessageEvent)){
                    oneMessageEvent.getMessageEvent().getSubject().sendMessage("宁输入的之类有误呢,"+ Set.CONFIG.BotSet.Name+"听不懂呢");
                }
            }else {
                if (isReply(oneMessageEvent)){
                    if (Set.CONFIG_VARIABLE.LastSendTime!=null){
                        long temp =  System.currentTimeMillis() - Set.CONFIG_VARIABLE.LastSendTime;
                        if (temp < Set.CONFIG.BotSet.FrequencyOfSpeeches){
                            if (temp>0){
                                Thread.sleep(temp);
                            }else {
                                Thread.sleep(Set.CONFIG.BotSet.FrequencyOfSpeeches);
                            }
                        }
                    }else {
                        Set.CONFIG_VARIABLE.LastSendTime = System.currentTimeMillis();
                    }
                }else {
                    WebSocketServer.sendInfoToAll(new PluginMessage(oneMessageEvent), oneMessageEvent.getMessageEvent().getSubject());
                }
            }
        }
    }

    private static boolean isReply(OneMessageEvent event) throws InterruptedException, IOException {
        for (String s : Set.CONFIG.FunctionSet.getPriority()) {
            Function function = null;
            function = FunctionLoader.getService(s);
            if ((function != null) && function.IsUsing()){
                if (function.IsUsing()) {
                    if (function.getResponse(event)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private static boolean isCommand(OneMessageEvent event){
        for (String s : Set.CONFIG.FunctionSet.getPriority()) {
            Function function = null;
            function = FunctionLoader.getService(s);
            if ((function != null) && function.IsUsing()){
                if (function.setFunction(event)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean banMessage(OneMessageEvent oneMessageEvent){
        String msg = oneMessageEvent.getMessageEvent().getMessage().serializeToMiraiCode();
        if (oneMessageEvent.getKind().equals("GROUP")){
            String botPermission = Objects.requireNonNull(oneMessageEvent.getMessageEvent().getBot().getGroup(oneMessageEvent.getMessageEvent().getSubject().getId())).getBotPermission().name();
            if (botPermission.equals("OWNER")){
                if (isBan(msg)){
                    banSb(oneMessageEvent);
                    return true;
                }
            }else if (botPermission.equals("ADMINISTRATOR")){
                if (Objects.requireNonNull(Objects.requireNonNull(oneMessageEvent.getMessageEvent().getBot().getGroup(oneMessageEvent.getMessageEvent().getSubject().getId())).getMembers().get(oneMessageEvent.getFromId())).getPermission().name().equals("MEMBER") && isBan(msg)){
                    banSb(oneMessageEvent);
                    return true;
                }else if (!Objects.requireNonNull(Objects.requireNonNull(oneMessageEvent.getMessageEvent().getBot().getGroup(oneMessageEvent.getMessageEvent().getSubject().getId())).getMembers().get(oneMessageEvent.getFromId())).getPermission().name().equals("MEMBER") && isBan(msg)){
                    oneMessageEvent.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(Set.CONFIG.BotSet.ToUnableBannedNumberSay, oneMessageEvent.getMessageEvent().getSubject())));
                    return true;
                }
            }

        }

        return false;
    }
    private static boolean isBan(String msg){
        for (String banWord : Set.CONFIG_VARIABLE.BanWords) {
            if (msg.contains(banWord)){
                return true;
            }
        }
        return false;
    }
    private static void banSb(OneMessageEvent oneMessageEvent) {
        if (Set.CONFIG.BotSet.SensitiveWordsBanned == 0){
            MessageSource.recall(oneMessageEvent.getMessageEvent().getMessage());
            oneMessageEvent.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(Set.CONFIG.BotSet.ToBannedNumberSay, oneMessageEvent.getMessageEvent().getSubject())));
        }else if (Set.CONFIG.BotSet.SensitiveWordsBanned > 0){
            MessageSource.recall(oneMessageEvent.getMessageEvent().getMessage());
            Objects.requireNonNull(Objects.requireNonNull(oneMessageEvent.getMessageEvent().getBot().getGroup(oneMessageEvent.getMessageEvent().getSubject().getId())).getMembers().get(oneMessageEvent.getFromId())).mute(Set.CONFIG.BotSet.SensitiveWordsBanned);
                    oneMessageEvent.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(Set.CONFIG.BotSet.ToBannedNumberSay, oneMessageEvent.getMessageEvent().getSubject())));
        }

    }

}
