package polaris.core.message.collector;

import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;

import java.util.LinkedList;
import java.util.Queue;

public class ChatMessageCollector {
    public static Queue<MessageEvent> chatMessageQueue = new LinkedList<>();
}
