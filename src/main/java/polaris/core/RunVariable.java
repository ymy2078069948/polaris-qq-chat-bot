package polaris.core;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import org.springframework.stereotype.Component;
import polaris.core.pojo.RemindMe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RunVariable {
    public static Bot BOT;
    public static Map<Long, Contact> WSContacts = new HashMap<>();
    public static List<RemindMe> remindMeList;

}
