package yuki.mirai.demo.function.impl;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.stereotype.Service;
import yuki.mirai.demo.Set;
import yuki.mirai.demo.function.Function;
import yuki.mirai.demo.pojo.OneMessageEvent;
import yuki.mirai.demo.pojo.OriginMessage;

import java.io.File;
import java.io.IOException;

@Service("BrowserCrawling")
public class BrowserCrawlingImpl implements Function {
    @Override
    public String getName() {
        return "BrowserCrawling";
    }

    @Override
    public Boolean IsUsing() {
        return Set.CONFIG.FunctionSet.BrowserCrawling.IsUsing;
    }

    @Override
    public Boolean getResponse(OneMessageEvent event) throws InterruptedException, IOException {
        for (OriginMessage originMessage : event.getOriginMessageList()) {
            if (originMessage.getType().equals("PlainText")){
                String msg = originMessage.getContent();
                int index = Set.CONFIG.FunctionSet.BrowserCrawling.TriggerWordList.indexOf(msg);
                if (index == -1){
                    return false;
                }else {
                    getWebPage(event.getMessageEvent().getSubject(),index);
                }

            }
        }
        return false;
    }

    @Override
    public Boolean setFunction(OneMessageEvent event) {
        return null;
    }

    private void getWebPage(Contact contact,int index){
        System.setProperty(Set.CONFIG.Bot.SeleniumDriverName,Set.CONFIG.Bot.SeleniumDriverPath);
        WebDriver driver = new EdgeDriver();
        try {
            driver.get(Set.CONFIG.FunctionSet.BrowserCrawling.UrlList.get(index));
            Thread.sleep(1000);
            System.out.println(Set.CONFIG.FunctionSet.BrowserCrawling.XpathList.get(index));
            WebElement webElement = driver.findElement(By.xpath(Set.CONFIG.FunctionSet.BrowserCrawling.XpathList.get(index)));

            Long width = (Long) ((JavascriptExecutor)driver).executeScript("return document.documentElement.scrollWidth");
            Long height = (Long) ((JavascriptExecutor)driver).executeScript("return document.documentElement.scrollHeight");
            //设置浏览器弹窗页面的大小
            driver.manage().window().setSize(new Dimension(width.intValue(), height.intValue()));

            if (webElement != null){
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", webElement);
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,200)");
                File screen = webElement.getScreenshotAs(OutputType.FILE);
                if (screen.exists()){
                    Image image = ExternalResource.uploadAsImage(screen,contact);
                    Thread.sleep(1000);
                    contact.sendMessage(new MessageChainBuilder().append(image).build());
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            driver.close();
        }

    }
}
