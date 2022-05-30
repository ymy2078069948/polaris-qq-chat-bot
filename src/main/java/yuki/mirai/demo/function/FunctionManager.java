package yuki.mirai.demo.function;

import com.alibaba.fastjson.JSON;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yuki.mirai.demo.Set;
import yuki.mirai.demo.WebSocketServer;
import yuki.mirai.demo.pojo.OneMessageEvent;
import yuki.mirai.demo.pojo.plugin.PluginMessage;
import yuki.mirai.demo.utils.MessageBuilder;

import java.io.IOException;
import java.util.Objects;

public class FunctionManager {
    static Logger logger = LoggerFactory.getLogger("FunctionManager");

    public static void onMessage(MessageEvent event) throws InterruptedException, IOException {
        OneMessageEvent oneMessageEvent = new OneMessageEvent(event);
        if (oneMessageEvent.getKind().equals("GROUP")){
            if (Set.CONFIG.Bot.ApplyGroups.contains(oneMessageEvent.getMessageEvent().getSubject().getId())){
                toApply(oneMessageEvent);
            }
        }else {
            toApply(oneMessageEvent);
        }

    }

    private static void toApply(OneMessageEvent oneMessageEvent) throws InterruptedException, IOException {
        if (!banMessage(oneMessageEvent)){
            if ((oneMessageEvent.getOriginMessageList().toString().contains("C&") || oneMessageEvent.getOriginMessageList().toString().contains("c&")) && oneMessageEvent.getFromId() == Set.CONFIG.Bot.Admin) {
                logger.info("Message is Command, time ["+oneMessageEvent.getMessageEvent().getTime()+"]");
                if (!isCommand(oneMessageEvent)){
                    oneMessageEvent.getMessageEvent().getSubject().sendMessage("宁输入的之类有误呢,"+ Set.CONFIG.Bot.Name+"听不懂呢");
                }
            }else {
                if (isReply(oneMessageEvent)){
                    if (Set.CONFIG_VARIABLE.LastSendTime!=null){
                        long temp =  System.currentTimeMillis() - Set.CONFIG_VARIABLE.LastSendTime;
                        if (temp < Set.CONFIG.Bot.FrequencyOfSpeeches){
                            if (temp>0){
                                Thread.sleep(temp);
                            }else {
                                Thread.sleep(Set.CONFIG.Bot.FrequencyOfSpeeches);
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
                    oneMessageEvent.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(Set.CONFIG.Bot.ToUnableBannedNumberSay, oneMessageEvent.getMessageEvent().getSubject())));
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
        if (Set.CONFIG.Bot.SensitiveWordsBanned == 0){
            MessageSource.recall(oneMessageEvent.getMessageEvent().getMessage());
            oneMessageEvent.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(Set.CONFIG.Bot.ToBannedNumberSay, oneMessageEvent.getMessageEvent().getSubject())));
        }else if (Set.CONFIG.Bot.SensitiveWordsBanned > 0){
            MessageSource.recall(oneMessageEvent.getMessageEvent().getMessage());
            Objects.requireNonNull(Objects.requireNonNull(oneMessageEvent.getMessageEvent().getBot().getGroup(oneMessageEvent.getMessageEvent().getSubject().getId())).getMembers().get(oneMessageEvent.getFromId())).mute(Set.CONFIG.Bot.SensitiveWordsBanned);
                    oneMessageEvent.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(Set.CONFIG.Bot.ToBannedNumberSay, oneMessageEvent.getMessageEvent().getSubject())));
        }

    }

}
