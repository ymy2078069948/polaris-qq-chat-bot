package polaris.core.utils;

import net.mamoe.mirai.contact.Contact;
import polaris.core.Set;
import polaris.core.pojo.OneMessageEvent;
import polaris.core.pojo.OriginMessage;

import java.util.Objects;
/**
 * 消息发送模块
 * 发送普通文本消息或者带[]之类的
 */
public class MessageSender {
    private static Contact contact = null;
    private static int recallIn = -1;


    /**
     *
     * @param event MessageEvent
     * @param msg 单条消息
     * @return 是否发送成功
     */
    public static boolean toSend(Contact event,String msg) {
        contact = event;

        if (msg != null) {
            if (msg.startsWith(" ")) {
                msg = msg.substring(1);
            }

            if (msg.contains("recallIn")){
                String[] m = msg.split("recallIn");
                msg = m[0];
                recallIn = Integer.parseInt(m[1]);
            }
        }
        if (Set.CONFIG_VARIABLE.SpecificReply.get(msg) != null){
            if (Set.CONFIG_VARIABLE.SpecificReply.get(msg).contains("[recall]")){
                recallIn = Set.CONFIG.BotSet.RecallIn;
            }

            if (recallIn == -1){
                event.sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(Set.CONFIG_VARIABLE.SpecificReply.get(msg), event)));
                return true;
            }else if (recallIn > 0){
                event.sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(Set.CONFIG_VARIABLE.SpecificReply.get(msg), event))).recallIn(recallIn * 1000L);
                recallIn = -1;
                return true;
            }
        }

        return false;
    }



    /**
     *
     * @param event OneMessageEvent 消息事件
     * @return 是否发送成功
     */
    public static boolean toSend(OneMessageEvent event) {
        contact = event.getMessageEvent().getSubject();
        for (OriginMessage originMessage : event.getOriginMessageList()) {
            String msg = originMessage.getContent();
            toSend(contact,msg);
        }
        return false;
    }



}
