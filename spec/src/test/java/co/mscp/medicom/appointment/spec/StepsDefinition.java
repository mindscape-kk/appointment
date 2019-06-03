package co.mscp.medicom.appointment.spec;

import co.mscp.Logger;
import cucumber.api.java8.En;

public class StepsDefinition implements En {

    public StepsDefinition() {

        Before(() -> DockerHelper.runAll());

        Given("client has user token and encryption key", () -> {
            Logger.of(this).info("client has user token and encryption key");
        });

        When("^client calls \"([^\"]*)\" on \"([^\"]*)\"$", (String arg0, String arg1) -> {
            Logger.of(this).info("client calls %s on %s", arg0, arg1);
        });

        Then("^response should be same as the expected profile$", () -> {
            Logger.of(this).info("response should be same as the expected profile");
        });

    }

}
