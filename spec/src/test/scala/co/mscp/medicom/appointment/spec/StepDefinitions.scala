package co.mscp.medicom.appointment.spec

import co.mscp.logging.Logger
import cucumber.api.scala.{EN, ScalaDsl}

class StepDefinitions extends ScalaDsl with EN {

  var token: String = _
  var encryptionKey: String = _


  Before() { (_) =>
    DockerHelper.runAll()
  }

  Given("client has user token \"([^\"]*)\" and encryption key \"([^\"]*)\"")
  (token: String, key: String) => {
    this.token = token
    this.encryptionKey = key
  }

  When("^client calls \"([^\"]*)\" on \"([^\"]*)\"$")
  (path: String, arg1: String) => {

  }

  Then("^response should be same as the expected profile$") {
    Logger.of(this).info("response should be same as the expected profile")
  }

}
