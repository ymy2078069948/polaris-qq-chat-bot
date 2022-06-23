package polaris.core;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import polaris.core.pojo.set.BotSet;
import polaris.core.pojo.set.Config;
import polaris.core.pojo.set.ConfigVariable;
import polaris.core.pojo.set.FunctionSet;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

//@Component
public class Set {
    public static Config CONFIG;
    public static ConfigVariable CONFIG_VARIABLE;
    public static BotSet BotSet;
    public static FunctionSet FunctionSet;

//    @PostConstruct
//    public void loadSet() throws IOException {
//        Yaml yml = new Yaml(new Constructor(BotSet.class));
//        Set.BotSet = yml.load(FileUtils.openInputStream(new File("BotSet.yml")));
//        yml = new Yaml(new Constructor(FunctionSet.class));
//        Set.FunctionSet = yml.load(FileUtils.openInputStream(new File("FunctionSet.yml")));
//    }
}
