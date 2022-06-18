package polaris.core.function.impl;

import org.springframework.stereotype.Service;
import polaris.core.Set;
import polaris.core.function.Function;
import polaris.core.pojo.OneMessageEvent;
import polaris.core.pojo.OriginMessage;
import polaris.core.utils.MessageSender;

@Service("SpecificReply")
public class SpecificReplyImpl implements Function {
    @Override
    public String getName() {
        return "SpecificReply";
    }

    @Override
    public Boolean IsUsing() {
        return Set.CONFIG.FunctionSet.SpecificReply.IsUsing;
    }

    @Override
    public Boolean getResponse(OneMessageEvent event) {
        // 群消息
        if (event.getKind().equals("GROUP") && Set.CONFIG.Bot.ApplyGroups.contains(event.getTargetId())) {
            if (Set.CONFIG.FunctionSet.SpecificReply.IfNeedAt){
                if (event.getOriginMessageList().toString().contains("At")) {
                    return MessageSender.toSend(event);
                }
            }else {
                return MessageSender.toSend(event);
            }
        }else {
            return MessageSender.toSend(event);
        }

        return false;
    }



    @Override
    public Boolean setFunction(OneMessageEvent event) {
        for (OriginMessage originMessage : event.getOriginMessageList()) {
            if (originMessage.getType().equals("PlainText")) {
                String[] message = originMessage.getContent().split(":");
                if (Set.CONFIG.FunctionSet.SpecificReply.CommandSet.indexOf(message[0]) == 0){
                    Set.CONFIG.FunctionSet.SpecificReply.IsUsing = !Set.CONFIG.FunctionSet.SpecificReply.IsUsing;
                    return true;
                }else if (Set.CONFIG.FunctionSet.SpecificReply.CommandSet.indexOf(message[0]) == 1) {
                    Set.CONFIG.FunctionSet.SpecificReply.IfNeedAt = !Set.CONFIG.FunctionSet.SpecificReply.IfNeedAt;
                    return true;
                }
            }
        }
        return false;
    }



}
