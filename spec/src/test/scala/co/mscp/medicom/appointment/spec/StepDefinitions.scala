package co.mscp.medicom.appointment.spec

import co.mscp.logging.Logger
import cucumber.api.scala.{EN, ScalaDsl}

class StepDefinitions extends ScalaDsl with EN {

  Before() { (_) =>
    DockerHelper.runAll()
  }

  Given("client has user token and encryption key") {
    Logger.of(this).info("client has user token and encryption key")
  }

  When("^client calls \"([^\"]*)\" on \"([^\"]*)\"$") { (arg0: String, arg1: String) =>
    Logger.of(this).info("client calls %s on %s", arg0, arg1)
  }

  Then("^response should be same as the expected profile$") {
    Logger.of(this).info("response should be same as the expected profile")
  }

}
