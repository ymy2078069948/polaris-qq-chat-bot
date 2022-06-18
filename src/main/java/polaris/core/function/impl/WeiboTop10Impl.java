package polaris.core.function.impl;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.stereotype.Service;
import polaris.core.Set;
import polaris.core.function.Function;
import polaris.core.pojo.OneMessageEvent;
import polaris.core.pojo.OriginMessage;

import java.io.File;
import java.io.IOException;

@Service("WeiboTop10")
public class WeiboTop10Impl implements Function {
    @Override
    public String getName() {
        return "WeiboTop10";
    }

    @Override
    public Boolean IsUsing() {
        return Set.CONFIG.FunctionSet.WeiboTop10.IsUsing;
    }

    @Override
    public Boolean getResponse(OneMessageEvent event) throws InterruptedException, IOException {
        for (OriginMessage originMessage : event.getOriginMessageList()) {
            if (originMessage.getType().equals("PlainText")){
                String msg = originMessage.getContent();
                for (String s : Set.CONFIG.FunctionSet.WeiboTop10.CommandSet) {
                    if (msg.equals(s)){
                        getTop10(event.getMessageEvent().getSubject());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Boolean setFunction(OneMessageEvent event) {
        return null;
    }

    private void getTop10(Contact contact){
        System.setProperty(Set.CONFIG.Bot.SeleniumDriverName,Set.CONFIG.Bot.SeleniumDriverPath);
        WebDriver driver = new EdgeDriver();
        try {
            driver.get("https://tophub.today/n/KqndgxeLl9");
            Thread.sleep(1000);
            WebElement webElement = driver.findElement(By.xpath("//*[@id=\"page\"]/div[2]/div[2]/div[1]/div[2]/div"));

            Long width = (Long) ((JavascriptExecutor)driver).executeScript("return document.documentElement.scrollWidth");
            Long height = (Long) ((JavascriptExecutor)driver).executeScript("return document.documentElement.scrollHeight");
            //设置浏览器弹窗页面的大小
            driver.manage().window().setSize(new Dimension(width.intValue(), height.intValue()));

            File screen = webElement.getScreenshotAs(OutputType.FILE);
            if (screen.exists()){
                Image image = ExternalResource.uploadAsImage(screen,contact);
                Thread.sleep(1000);
                contact.sendMessage(new MessageChainBuilder().append(new PlainText("实时榜单:")).append(image).build());

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            driver.close();
        }

    }
}
