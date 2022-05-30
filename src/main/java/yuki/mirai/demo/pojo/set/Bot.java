package yuki.mirai.demo.pojo.set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {
    // 账号
    public Long QQ;
    // 密码
    public String Password;
    // 登录协议
    public String Protocol;
    // bot name
    public String Name;
    // bot的备注
    public String Remark;
    // bot的介绍
    public String Introduction;
    // 工作路径
    public String Workspace;
    // 缓存路径
    public String CacheDir;
    // 聊天功能的websocket端口
    public int WebSocketPort;
    // 可以通过那个qq进行管理
    public Long Admin;
    // 允许监听那些消息 [DefaultMessageHandler|FriendInputStatusChangedEventHandler|MemberUnmuteEventHandler|MemberJoinEventHandler...]
    public List<String> ActivateHandler;
    // 对那些群生效
    public Set<Long> ApplyGroups;
    // 被解除禁言时
    public String UnmuteSay;
    // 群聊成员加入说
    public String MemberJoinSay;
    // 如何处理违规
    public int SensitiveWordsBanned;

    public String BanWordFilePath;
    // 对被ban的人说
    public String ToBannedNumberSay;
    public String ToUnableBannedNumberSay;
    // 戳一戳
    public List<String> ToNudgeSay;
    public int RecallIn;
    public String SeleniumDriverPath;
    public String SeleniumDriverName;
    public Long FrequencyOfSpeeches;
    public String WhenCalled;
    public String MemberLeaveSay;
}