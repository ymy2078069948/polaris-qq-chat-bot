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

@Service("WeatherInfo")
public class GetWeatherImpl implements Function {
    @Override
    public String getName() {
        return "WeatherInfo";
    }

    @Override
    public Boolean IsUsing() {
        return Set.CONFIG.FunctionSet.WeatherInfo.IsUsing;
    }

    @Override
    public Boolean getResponse(OneMessageEvent event) throws InterruptedException, IOException {
        for (OriginMessage originMessage : event.getOriginMessageList()) {
            if (originMessage.getType().equals("PlainText")){
                String msg = originMessage.getContent();
                for (String s : Set.CONFIG.FunctionSet.WeatherInfo.CommandSet) {
                    if (msg.endsWith(s)){
                        getWeatherInfo(msg.split(s)[0],event.getMessageEvent().getSubject());
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

    private void getWeatherInfo(String city, Contact contact){
        System.setProperty(Set.CONFIG.Bot.SeleniumDriverName,Set.CONFIG.Bot.SeleniumDriverPath);
        WebDriver driver = new EdgeDriver();
        try {
            driver.get("https://weather.cma.cn/");
            Thread.sleep(1000);
            for (int i = 0; i < city.length(); i++) {
                driver.findElement(By.id("searchInput")).sendKeys(String.valueOf(city.charAt(i)));
                Thread.sleep(100);
            }
            driver.findElement(By.id("searchInput")).sendKeys(Keys.ENTER);
            Thread.sleep(1000);
            WebElement webElement = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div/div"));
            int width = webElement.getSize().width+100;
            Dimension dimension = new Dimension(width,driver.manage().window().getSize().height);
            driver.manage().window().setSize(dimension);
            File screen1 = webElement.getScreenshotAs(OutputType.FILE);
            if (screen1.exists()){
                Image image = ExternalResource.uploadAsImage(screen1,contact);
                Thread.sleep(1000);
                contact.sendMessage(new MessageChainBuilder().append(new PlainText("实时天气:")).append(image).build());

            }
            webElement = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[1]/div[1]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();",webElement);
            screen1 = webElement.getScreenshotAs(OutputType.FILE);
            if (screen1.exists()){
                Image image = ExternalResource.uploadAsImage(screen1,contact);
                Thread.sleep(1000);
                contact.sendMessage(new MessageChainBuilder().append(new PlainText("一周天气:")).append(image).build());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            driver.close();
        }

    }
}
