package yuki.mirai.demo.pojo.set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yuki.mirai.demo.pojo.set.function.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionSet {
    public List<String> Priority;
    public BaiduTranslate BaiduTranslate;
    public SpecificReply SpecificReply;
    public MusicShare MusicShare;
    public BaiduImageSearch BaiduImageSearch;
    public WeatherInfo WeatherInfo;
    public WeiboTop10 WeiboTop10;
    public SignIn SignIn;
    public BrowserCrawling BrowserCrawling;
}