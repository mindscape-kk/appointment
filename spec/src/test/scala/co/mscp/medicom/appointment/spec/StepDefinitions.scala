package co.mscp.medicom.appointment.spec

import co.mscp.appointment.client.AppointmentClient
import co.mscp.appointment.entity.Resource
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers._


class StepDefinitions extends ScalaDsl with EN {

  private val host = s"http://localhost:6452"
  private var client: AppointmentClient = _
  private var token: String = _
  private var encryptionKey: String = _

  private val inputResource = Resource("1323", "testType", "testName",
    Some("Test Description"),  None /* TODO more values */)

  private var outputResource: Resource = _


  Before() { (_) =>
    DockerHelper.runAll()
  }

  Given("client has user token {string} and encryption key {string}"){ (token:String,key:String) =>
    client = new AppointmentClient(host, "testInstitute", token)
  }

  When("client posts a new resource request"){ () =>
    outputResource = client.createResource(inputResource)
  }

  Then("response should be same as request"){ () =>
    /* TODO check all fields other than ID are equal */
  }

  Then("ID in response should not be null"){ () =>
    outputResource.id should not be null
  }
}
