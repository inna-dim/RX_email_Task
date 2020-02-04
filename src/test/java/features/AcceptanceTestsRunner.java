package features;


import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@CucumberOptions(features = "src/test/resources/features")
@RunWith(CucumberWithSerenity.class)
public class AcceptanceTestsRunner {
}
