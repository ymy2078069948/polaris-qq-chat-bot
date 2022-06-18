package polaris.core.function.impl;

import net.mamoe.mirai.contact.NormalMember;
import org.springframework.stereotype.Service;
import polaris.core.Set;
import polaris.core.function.Function;
import polaris.core.pojo.OneMessageEvent;
import polaris.core.utils.MessageBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service("SignIn")
public class SignInImpl implements Function {
    @Override
    public String getName() {
        return "SignIn";
    }

    @Override
    public Boolean IsUsing() {
        return Set.CONFIG.FunctionSet.SignIn.IsUsing;
    }

    @Override
    public Boolean getResponse(OneMessageEvent event) throws InterruptedException, IOException {
        if (event.getKind().equals("GROUP")){
            if (event.getOriginMessageList().get(0).getType().equals("PlainText")){
                String msg = event.getOriginMessageList().get(0).getContent();
                if (msg.equals(Set.CONFIG.FunctionSet.SignIn.SignIn)){
                    Set.CONFIG.FunctionSet.SignIn.LastSignInTime.put(event.getTargetId(),new Date());
                    List<NormalMember> memberList;
                    if ( Set.CONFIG.FunctionSet.SignIn.memberListMap.containsKey(event.getTargetId())){
                        memberList = Set.CONFIG.FunctionSet.SignIn.memberListMap.get(event.getTargetId());
                        for (NormalMember normalMember : memberList) {
                            if (normalMember.getId() == event.getFromId()){
                                event.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain("今天你已经签到了(☆▽☆)", event.getMessageEvent().getSubject())));
                                return true;
                            }
                        }
                        memberList.add(Objects.requireNonNull(event.getMessageEvent().getBot().getGroup(event.getTargetId())).getMembers().get(event.getFromId()));
                        Set.CONFIG.FunctionSet.SignIn.memberListMap.put(event.getTargetId(),memberList);
                        event.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain("签到成功,你是第"+memberList.size()+"名&&" + Set.CONFIG.FunctionSet.SignIn.ToSignInSay, event.getMessageEvent().getSubject())));
                    }else {
                        memberList = new ArrayList<>();
                        memberList.add(Objects.requireNonNull(event.getMessageEvent().getBot().getGroup(event.getTargetId())).getMembers().get(event.getFromId()));
                        Set.CONFIG.FunctionSet.SignIn.memberListMap.put(event.getTargetId(),memberList);
                        event.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain("哇,你是今天第一个签到的&&" + Set.CONFIG.FunctionSet.SignIn.ToSignInSay, event.getMessageEvent().getSubject())));
                    }
                    return true;
                }else if (msg.equals(Set.CONFIG.FunctionSet.SignIn.DidISignIn)){
                    if ( Set.CONFIG.FunctionSet.SignIn.memberListMap.containsKey(event.getTargetId())){
                        List<NormalMember> memberList = Set.CONFIG.FunctionSet.SignIn.memberListMap.get(event.getTargetId());
                        for (NormalMember normalMember : memberList) {
                            if (normalMember.getId() == event.getFromId()){
                                event.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain("今天你已经签到了O(∩_∩)O", event.getMessageEvent().getSubject())));
                                return true;
                            }
                        }
                    }
                    event.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain("今天你还未签到(＾Ｕ＾)ノ~ＹＯ", event.getMessageEvent().getSubject())));
                    return true;
                }else if (msg.contains(Set.CONFIG.FunctionSet.SignIn.DidHeSignIn)){
                    String key = msg.split(Set.CONFIG.FunctionSet.SignIn.DidHeSignIn)[0];
                    if ( Set.CONFIG.FunctionSet.SignIn.memberListMap.containsKey(event.getTargetId())){
                        List<NormalMember> memberList = Set.CONFIG.FunctionSet.SignIn.memberListMap.get(event.getTargetId());
                        for (NormalMember normalMember : memberList) {
                            if (normalMember.getNameCard().equals(key) || normalMember.getSpecialTitle().equals(key)){
                                event.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain("今天"+normalMember.getNameCard()+"已经签到了b(￣▽￣)d", event.getMessageEvent().getSubject())));
                                return true;
                            }
                        }
                    }
                    event.getMessageEvent().getSubject().sendMessage(Objects.requireNonNull(MessageBuilder.buildMessageChain("今天"+key+"还没签到~(￣▽￣)~*", event.getMessageEvent().getSubject())));
                }
            }
        }

        return false;
    }

    @Override
    public Boolean setFunction(OneMessageEvent event) {
        return null;
    }
}
