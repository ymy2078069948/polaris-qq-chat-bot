package polaris.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import polaris.core.pojo.RemindMe;
import polaris.core.service.RemindMeService;
import polaris.core.utils.MessageBuilder;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduleEventHandle {
    @Autowired
    private RemindMeService remindMeService;

    @Scheduled(fixedRate = 1000*60*60*4)
    private void getRemindMeList(){
        QueryWrapper<RemindMe> remindMeQueryWrapper = new QueryWrapper<>();
        remindMeQueryWrapper.ge("remind_time", System.currentTimeMillis());
        RunVariable.remindMeList = remindMeService.list(remindMeQueryWrapper);
    }

    @Scheduled(fixedRate = 1000*60)
    private void DoRemindMe() {
        if (RunVariable.remindMeList == null){
            RunVariable.remindMeList = new ArrayList<>();
        }
        RunVariable.remindMeList.forEach(event -> {
            if (Math.abs(event.getRemindTime() - System.currentTimeMillis())<1000*30){
                if (event.getGroupID() != null){
                    At at = new At(event.getSubjectID());
                    Contact contact = RunVariable.BOT.getGroup(event.getGroupID());
                    Objects.requireNonNull(contact).sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(event.getContent(), contact)).plus(at));
                }else {
                    Contact contact = RunVariable.BOT.getFriend(event.getSubjectID());
                    Objects.requireNonNull(contact).sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(event.getContent(), contact)));
                }
                remindMeService.removeById(event);
            }
        });
    }
}
