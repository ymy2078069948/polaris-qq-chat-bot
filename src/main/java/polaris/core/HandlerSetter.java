package polaris.core;

import net.mamoe.mirai.event.ListenerHost;
import java.lang.reflect.InvocationTargetException;


public class HandlerSetter {
    public void setHandler() {
        for (String s : Set.CONFIG.BotSet.ActivateHandler) {
            try {
                RunVariable.BOT.getEventChannel().registerListenerHost((ListenerHost) Class.forName("polaris.core.handlers."+s).getDeclaredConstructor().newInstance());

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
