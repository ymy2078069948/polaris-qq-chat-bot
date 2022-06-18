package polaris.core.function;

import polaris.core.pojo.OneMessageEvent;

import java.io.IOException;

public interface Function {
     String getName();
     Boolean IsUsing();
     Boolean getResponse(OneMessageEvent event) throws InterruptedException, IOException;
     Boolean setFunction(OneMessageEvent event);
}
