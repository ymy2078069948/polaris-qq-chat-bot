package yuki.mirai.demo;

import net.mamoe.mirai.event.ListenerHost;

import java.lang.reflect.InvocationTargetException;

public class HandlerSetter {
    public static void setHandler() {
        for (String s : Set.CONFIG.Bot.ActivateHandler) {
            try {
                RunVariable.BOT.getEventChannel().registerListenerHost((ListenerHost) Class.forName("yuki.mirai.demo.handlers."+s).getDeclaredConstructor().newInstance());

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
