package ui.mailbox;

import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class EmailPage extends PageObject {

    @FindBy(xpath = "//body[a]")
    public WebElement emailBody;

    @FindBy(xpath = "//iframe[@id=\"msg_body\"]")
    public WebElementFacade MESSAGE_FRAME;
    }
