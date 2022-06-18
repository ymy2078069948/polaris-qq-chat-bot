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
import java.util.List;

@Service("BaiduImageSearch")
public class BaiduImageSearchImpl implements Function {


    @Override
    public String getName() {
        return "BaiduImageSearch";
    }

    @Override
    public Boolean IsUsing() {
        return Set.CONFIG.FunctionSet.BaiduImageSearch.IsUsing;
    }

    @Override
    public Boolean getResponse(OneMessageEvent event) throws InterruptedException {
        String imageUrl = null;
        for (OriginMessage originMessage : event.getOriginMessageList()) {
            if (originMessage.getType().equals("Image")){
                imageUrl = originMessage.getImageId();
            }
            if (originMessage.getType().equals("PlainText")){
                String msg = originMessage.getContent();
                if (originMessage.getContent().startsWith(" ")){
                    msg = msg.substring(1);
                }
                if (Set.CONFIG.FunctionSet.BaiduImageSearch.CommandSet.contains(msg)){
                    Set.CONFIG.FunctionSet.BaiduImageSearch.sponsorContact = event.getMessageEvent().getSubject();
                    Set.CONFIG.FunctionSet.BaiduImageSearch.sponsor = event.getFromId();
                    event.getMessageEvent().getSubject().sendMessage("请发送要搜索的图片");
                }
            }
        }
        if (Set.CONFIG.FunctionSet.BaiduImageSearch.sponsor != null && Set.CONFIG.FunctionSet.BaiduImageSearch.sponsorContact != null){
            if (Set.CONFIG.FunctionSet.BaiduImageSearch.sponsor == event.getFromId() && Set.CONFIG.FunctionSet.BaiduImageSearch.sponsorContact.getId() == event.getMessageEvent().getSubject().getId() && imageUrl != null){
                buildAndSend(imageUrl,Set.CONFIG.FunctionSet.BaiduImageSearch.sponsorContact);
                Set.CONFIG.FunctionSet.BaiduImageSearch.sponsor = null;
                Set.CONFIG.FunctionSet.BaiduImageSearch.sponsorContact = null;
                imageUrl = null;
                return true;
            }
        }

        imageUrl = null;
        return false;
    }

    @Override
    public Boolean setFunction(OneMessageEvent event) {
        return null;
    }

    private void buildAndSend(String url, Contact contact) throws InterruptedException {
        System.out.println(url);
        System.setProperty(Set.CONFIG.Bot.SeleniumDriverName,Set.CONFIG.Bot.SeleniumDriverPath);

        WebDriver driver = new EdgeDriver();
        try {
            driver.manage().window().maximize();
            driver.get("https://graph.baidu.com/pcpage/index?tpl_from=pc");
            driver.findElement(By.xpath("//*[@id=\"app\"]/div/div[1]/div[7]/div/span[1]/input")).sendKeys(url);
            driver.findElement(By.xpath("//*[@id=\"app\"]/div/div[1]/div[7]/div/span[2]")).click();
            Thread.sleep(3000);


            WebElement titleElement = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div[2]"));
            if (titleElement.findElements(By.className("general-title")) == null){
                File screen = titleElement.getScreenshotAs(OutputType.FILE);
                Image image = ExternalResource.uploadAsImage(screen,contact);
                Thread.sleep(1000);
                contact.sendMessage(new MessageChainBuilder().append(new PlainText("百度百科:")).append(image).build());

                //图片来源
                List<WebElement> webElementList = driver.findElements(By.className("graph-same-list-item"));
                for (int i = 0; i < webElementList.size(); i++) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", webElementList.get(i));
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,200)");
                    File sc = webElementList.get(i).getScreenshotAs(OutputType.FILE);
                    String link = webElementList.get(i).findElement(By.tagName("a")).getAttribute("href");
                    image = ExternalResource.uploadAsImage(sc,contact);
                    Thread.sleep(1000);
                    contact.sendMessage(new MessageChainBuilder().append(new PlainText("图片来源"+(i+1)+":")).append(image).append(new PlainText("link:")).append(new PlainText(link)).build());

                }
            }else {
                contact.sendMessage("没有在百度百科找到相关信息");
                List<WebElement> webElementList = driver.findElements(By.className("graph-same-list-item"));
                for (int i = 0; i < webElementList.size(); i++) {
                    File sc = webElementList.get(i).getScreenshotAs(OutputType.FILE);
                    String link = webElementList.get(i).findElement(By.tagName("a")).getAttribute("href");
                    Image image = ExternalResource.uploadAsImage(sc,contact);
                    Thread.sleep(1000);
                    contact.sendMessage(new MessageChainBuilder().append(new PlainText("图片来源"+(i+1)+":")).append(image).append(new PlainText("link:")).append(new PlainText(link)).build());

                }
            }

            //相似图链接
            contact.sendMessage(new MessageChainBuilder().append(new PlainText("相似图链接:")).append(driver.findElement(By.className("general-waterfall")).findElement(By.tagName("a")).getAttribute("href")).build());

        }finally {
            driver.close();
        }

    }


}
