package polaris.core.function.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polaris.core.RunVariable;
import polaris.core.Set;
import polaris.core.function.Function;
import polaris.core.pojo.OneMessageEvent;
import polaris.core.pojo.OriginMessage;
import polaris.core.pojo.RemindMe;
import polaris.core.service.RemindMeService;
import polaris.core.utils.MessageBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Service("RemindMe")
public class RemindMeImpl implements Function {
    @Autowired
    private RemindMeService remindMeService;

    @Override
    public String getName() {
        return "RemindMe";
    }

    @Override
    public Boolean IsUsing() {
        return Set.CONFIG.FunctionSet.RemindMe.IsUsing;
    }

    @Override
    public Boolean getResponse(OneMessageEvent event) throws InterruptedException, IOException {
        if (Set.CONFIG.FunctionSet.RemindMe.IfNeedAt){
            if (event.toString().contains("@")){
                return addRemind(event);
            }
        }else {
            return addRemind(event);
        }

        return false;
    }

    @Override
    public Boolean setFunction(OneMessageEvent event) {
        return null;
    }

    private Boolean addRemind(OneMessageEvent event){
        for (OriginMessage originMessage : event.getOriginMessageList()) {
            if (originMessage.getType().equals("PlainText")){
                String msg = originMessage.getContent();
                for (String s : Set.CONFIG.FunctionSet.RemindMe.CommandSet) {
                    if (msg.contains(s)){
                        RemindMe remindMe;
                        Long time = null;
                        String [] msgPart = msg.split(s);
                        try {
                            time = timeFormat(msgPart[0]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (time != null){
                            remindMe = new RemindMe();
                            remindMe.setRemindTime(time);
                            remindMe.setSubjectID(event.getFromId());
                            if (event.getTargetId() != event.getBotId()){
                                remindMe.setGroupID(event.getTargetId());
                            }
                            if (msgPart.length == 2){
                                remindMe.setContent(msgPart[1]);
                            }
                            if (RunVariable.remindMeList == null){
                                RunVariable.remindMeList = new ArrayList<>();
                            }
                            remindMeService.save(remindMe);
                            RunVariable.remindMeList.add(remindMe);
                            event.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain(Set.CONFIG.FunctionSet.RemindMe.OkSay, event.getMessageEvent().getSubject())));
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }

    private Long timeFormat(String time) throws ParseException {
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String nowTime = dateFormat.format(date);
        String dayString = nowTime.substring(0,10);
        String hourString = nowTime.substring(11, 13);
        String minString = nowTime.substring(14, 16);
        System.out.println(dayString +"@"+hourString);
        if (time.contains("-")){
            String[] timePart = time.split("-");
            String timeF = getFormatTimeString(timePart[1],hourString);
            if (timeF != null){
                timeF = nowTime.substring(0,8)+ timePart[0] +" "+timeF;
                return dateFormat.parse(timeF).getTime();
            }
        }else {
            String timeF = getFormatTimeString(time,hourString);
            if (timeF != null){
                timeF = nowTime.substring(0,10) +" "+timeF;
                return dateFormat.parse(timeF).getTime();
            }
        }
        return null;
    }
    private String getFormatTimeString(String time,String hourString){
        if (time.contains("点")){
            String [] timePart = time.split("点");
            if (timePart.length == 1){
                return timePart[0]+":00:00";
            }else if (timePart.length == 2){
                if (timePart[1].contains("分")){
                    timePart[1] = timePart[1].split("分")[0];
                }
                return timePart[0]+":"+timePart[1]+":00";
            }
        }
        if (time.contains(":")){
            String [] timePart = time.split(":");
            if (timePart.length == 1){
                return timePart[0]+":00:00";
            }else if (timePart.length == 2){
                return timePart[0]+":"+timePart[1]+":00";
            }
        }
        if (time.contains(".")){
            String [] timePart = time.split("\\.");
            if (timePart.length == 1){
                return timePart[0]+":00:00";
            }else if (timePart.length == 2){
                return timePart[0]+":"+timePart[1]+":00";
            }
        }
        if (time.contains(" ")){
            String [] timePart = time.split(" ");
            if (timePart.length == 1){
                return timePart[0]+":00:00";
            }else if (timePart.length == 2){
                return timePart[0]+":"+timePart[1]+":00";
            }
        }
        return null;
    }
}
