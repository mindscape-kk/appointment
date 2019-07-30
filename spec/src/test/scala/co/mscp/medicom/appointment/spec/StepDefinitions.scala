package co.mscp.medicom.appointment.spec

import co.mscp.appointment.client.AppointmentClient
import co.mscp.appointment.entity.Resource
import cucumber.api.PendingException
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers._


class StepDefinitions extends ScalaDsl with EN {

  private val host = s"http://localhost:"+DockerHelper.BACKEND_PORT
  private var client: AppointmentClient = _
  private var token: String = _
  private var encryptionKey: String = _
  private var error: Exception = _

  private val inputResource = new Resource("testType", "testName",
    "Test Description",  None /* TODO more values */)

  private var outputResource: Resource = _


  Before() { (_) =>
    DockerHelper.runAll()
    client = new AppointmentClient(host, "testInstitute", "mandy")
  }

  Given("client has user token {string} and encryption key {string}"){ (token:String,key:String) =>
    client = new AppointmentClient(host, "testInstitute", token)
  }

  When("client posts a new resource request"){ () =>
    try {
      outputResource = client.createResource(inputResource)
    } catch {
      case e:Exception =>
        error = e
        println(e.getMessage)
    }
  }

  Then("response should be same as request"){ () =>
    outputResource should have (
      'type ("testType"),
      'name ("testName"),
      'description (Some("Test Description")),
      'userDefinedProperties (None)
    )
  }

  Then("ID in response should not be null"){ () =>
    outputResource.id should not be null
  }

  Given("""client holds valid token for own institute"""){ () =>
    token = "mandy"
    client = new AppointmentClient(host, "my", token)
  }


  Given("""client holds invalid token for own institute"""){ () =>
    token = "invalidToken"
    client = new AppointmentClient(host, "my", token)
  }

  Then("""response should be error"""){ () =>
    assert(error != null)
  }

  Then("""error type should be BAD_AUTHORIZATION"""){ () =>

  }
}
