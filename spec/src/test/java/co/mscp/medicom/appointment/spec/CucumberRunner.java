package co.mscp.medicom.appointment.spec;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty"},
    glue = "co.mscp.medicom.appointment.spec",
    features = "src/test/feature")
public class CucumberRunner {

}