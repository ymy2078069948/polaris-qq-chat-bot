package yuki.mirai.demo.function.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import yuki.mirai.demo.Set;
import yuki.mirai.demo.function.Function;
import yuki.mirai.demo.pojo.OneMessageEvent;
import yuki.mirai.demo.pojo.OriginMessage;
import yuki.mirai.demo.utils.CharacterUtil;
import yuki.mirai.demo.utils.TransApi;



@Service("BaiduTranslate")
public class BaiduTranslateImpl implements Function {

    @Override
    public String getName() {
        return "BaiduTranslate";
    }

    @Override
    public Boolean IsUsing() {
        return Set.CONFIG.FunctionSet.BaiduTranslate.IsUsing;
    }

    @Override
    public Boolean getResponse(OneMessageEvent event) {
        for (OriginMessage originMessage : event.getOriginMessageList()) {
            if (originMessage.getType().equals("PlainText")) {
                if (originMessage.getContent().startsWith("T&") || originMessage.getContent().startsWith("t&")) {
                    String res = TransApi.getTransResult(originMessage.getContent().substring(2),"auto","zh");
                    JSONObject jsonObject = JSONObject.parseObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("trans_result");
                    jsonObject = jsonArray.getJSONObject(0);
                    res = CharacterUtil.unicodeDecode(jsonObject.getString("dst"));
                    event.getMessageEvent().getSubject().sendMessage(res);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean setFunction(OneMessageEvent event) {
        for (OriginMessage originMessage : event.getOriginMessageList()) {
            if (originMessage.getType().equals("PlainText")) {
                String[] message = originMessage.getContent().split(":");
                if (Set.CONFIG.FunctionSet.BaiduTranslate.CommandSet.indexOf(message[0]) == 0){
                    Set.CONFIG.FunctionSet.BaiduTranslate.IsUsing = !Set.CONFIG.FunctionSet.BaiduTranslate.IsUsing;
                    return true;
                }
            }
        }
        return false;
    }
}
