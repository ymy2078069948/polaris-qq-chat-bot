package yuki.mirai.demo.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * explain：获取应用上下文并获取相应的接口实现类
 *
 */
@Component
public class FunctionLoader implements ApplicationContextAware {
    static Logger logger = LoggerFactory.getLogger("FunctionManager");
    /**
     * 用于保存接口实现类名及对应的类
     */
    private static Map<String, Function> map;

    /**
     * 获取应用上下文并获取相应的接口实现类
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //根据接口类型返回相应的所有bean
        map = applicationContext.getBeansOfType(Function.class);
    }

    /**
     * 获取所有实现集合
     * @return
     */
    public static Map<String, Function> getMap() {
        return map;
    }

    /**
     * 获取对应服务
     * @param key
     * @return
     */
    public static Function getService(String key) {
        return map.get(key);
    }
}
