package yuki.mirai.demo;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.code.MiraiCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import yuki.mirai.demo.pojo.plugin.PluginInfo;
import yuki.mirai.demo.pojo.plugin.PluginMessage;
import yuki.mirai.demo.utils.MessageBuilder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/WebSocketServer/{PluginName}/{PluginID}")
@Component
public class WebSocketServer {
    Logger logger = LoggerFactory.getLogger("WebSocketChatServer");

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的CumWebSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    private PluginInfo pluginInfo= null;

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("PluginName") String PluginName,@PathParam("PluginID") String PluginID) {
        this.session = session;
        this.pluginInfo = new PluginInfo(PluginName,PluginID,true,session.getId());
        //加入set中
        webSocketSet.add(this);

        //添加在线人数
        addOnlineCount();
        logger.info("[onOpen]   [Plugin Name]   "+PluginName);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {

        logger.info("[onClose]   [Plugin Name]   "+this.pluginInfo.getPluginName());
        //从set中删除
        webSocketSet.remove(this);
        //在线数减1
        subOnlineCount();

    }

    /**
     * 收到客户端消息后调用
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.info("[onMessage]    "+message);
        PluginMessage pluginMessage = JSON.parseObject(message,PluginMessage.class);
        Contact contact = RunVariable.WSContacts.get(pluginMessage.getTime());
        if (pluginMessage.getMessageType().equals("command")){
           contact.sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(pluginMessage.getMessage(), contact)));
        }else if (pluginMessage.getMessageType().equals("code")){
            contact.sendMessage(MiraiCode.deserializeMiraiCode(pluginMessage.getMessage()));
        }else {
            logger.error("[onMessage]    消息类型错误");
        }
        RunVariable.WSContacts.remove(pluginMessage.getTime());
    }

    /**
     * 暴露给外部的群发
     *
     * @param message
     * @throws IOException
     */
    public static void sendInfoToAll(String message) throws IOException {
        sendAll(message);
    }
    public static void sendInfoToAll(PluginMessage message, Contact contact) throws IOException {
        RunVariable.WSContacts.put(message.getTime(),contact);
        sendAll(JSON.toJSONString(message));
    }

    /**
     * 暴漏给外部的发送到特定插件
     *
     * @param message
     * @param pluginID
     * @throws IOException
     */
    public static void sendInfoToPlugin(String message,String pluginID) throws IOException {
        for (WebSocketServer webSocketServer : webSocketSet) {
            if (webSocketServer.pluginInfo.getPluginID().equals(pluginID)){
                webSocketServer.sendMessage(message);
            }
        }
    }

    /**
     * 群发
     *
     * @param message
     */
    private static void sendAll(String message) {
        Arrays.asList(webSocketSet.toArray()).forEach(item -> {
            WebSocketServer customWebSocket = (WebSocketServer) item;
            //群发
            try {
                customWebSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 减少在线人数
     */
    private void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    /**
     * 添加在线人数
     */
    private void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    /**
     * 当前在线人数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 发送信息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        //获取session远程基本连接发送文本消息
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

}
