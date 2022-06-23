package polaris.core.service;

import net.mamoe.mirai.message.data.MessageChain;
import polaris.core.pojo.OriginMessage;

import java.util.Queue;

public interface FunctionService {
    Boolean isNeedAt();
    Boolean isUsing();
    MessageChain getResponse(Queue<OriginMessage> originMessageQueue);
}
