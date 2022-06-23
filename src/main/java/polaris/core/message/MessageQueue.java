package polaris.core.message;

import net.mamoe.mirai.event.events.*;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {
    public static Queue<GroupMessageEvent> GroupMessageQueue = new LinkedList<>();
    public static Queue<FriendMessageEvent> FriendMessageQueue = new LinkedList<>();
    public static Queue<GroupTempMessageEvent> GroupTempMessageQueue = new LinkedList<>();
    public static Queue<StrangerMessageEvent> StrangerMessageQueue = new LinkedList<>();
    public static Queue<OtherClientMessageEvent> OtherClientMessageQueue = new LinkedList<>();
}
