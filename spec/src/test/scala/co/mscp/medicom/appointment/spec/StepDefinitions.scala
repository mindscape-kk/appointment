package co.mscp.medicom.appointment.spec

import co.mscp.appointment.client.AppointmentClient
import co.mscp.appointment.entity.Resource
import co.mscp.mmk2.net.ServiceError
import cucumber.api.PendingException
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers._


class StepDefinitions extends ScalaDsl with EN {

  private val host = s"http://localhost:"+DockerHelper.BACKEND_PORT
  private var client: AppointmentClient = _
  private var token: String = _
  private var encryptionKey: String = _
  private var error: Exception = _

  private val R1 = new Resource("testType", "testName",
    "Test Description",  None /* TODO more values */)

  private var inputResource: Resource = _
  private var outputResource: Resource = _


  Before() { (_) =>
    DockerHelper.runAll()
  }

  Given("client has user token {string} and encryption key {string}"){ (token:String,key:String) =>
    client = new AppointmentClient(host, "testInstitute", token)
  }

  When("client posts a new resource request"){ () =>
    try {
      inputResource = R1
      outputResource = client.createResource(inputResource)
    } catch {
      case e:Exception =>
        error = e
        println(e.getMessage)
    }
  }

  Then("response should be same as request"){ () =>
    outputResource === inputResource
  }

  Then("ID in response should not be null"){ () =>
    outputResource.id should not be null
  }

  Given("""client holds valid token for own institute"""){ () =>
    token = "andy"
    client = new AppointmentClient(host, "my", token)
  }


  Given("""client holds invalid token for own institute"""){ () =>
    token = "invalidToken"
    client = new AppointmentClient(host, "my", token)
  }


  Given("""client holds valid token for different institute"""){ () =>
    token = "andy"
    client = new AppointmentClient(host, "invalidInstitute", token)
  }

  When("client posts a new resource request for other institute"){ () =>
    try {
      inputResource = R1
      outputResource = client.createResource(inputResource)
    } catch {
      case e:Exception =>
        error = e
        println(e.getMessage)
    }
  }

  When("""client put modified resource request for other institute"""){ () =>
    try {
      inputResource = R1.copy(description = Some("updated description"))
      outputResource = client.updateResource(inputResource)
    } catch {
      case e:Exception =>
        error = e
        println(e.getMessage)
    }
  }

  Then("""response should be error"""){ () =>
    assert(error != null)
  }

  Then("""error type should be BAD_AUTHORIZATION"""){ () =>
    assert(error != null)
    assert(error.asInstanceOf[ServiceError].getData.code  == ServiceError.Code.BAD_AUTHORIZATION)
  }



  When("""client put modified resource request for own institute"""){ () =>
    try {
      inputResource = R1
      val create : Resource = client.createResource(inputResource)
      inputResource = create.copy(description = Some("updated description"))
      outputResource = client.updateResource(inputResource)
    } catch {
      case e:Exception =>
        error = e
        println(e.getMessage)
    }
  }


  When("""client put modified resource request for own institute with bad id"""){ () =>
    try {
      inputResource = R1.copy(id=Some("BADID"))

      outputResource = client.updateResource(inputResource)
    } catch {
      case e:Exception =>
        error = e
        println(e.getMessage)
    }
  }
  When("""client put modified resource request for own institute with empty id"""){ () =>
    try {
      inputResource = R1.copy(id=Some(null))

      outputResource = client.updateResource(inputResource)
    } catch {
      case e:Exception =>
        error = e
        println(e.getMessage)
    }
  }

  Then("""error type should be BAD_ID"""){ () =>
    assert(error != null)
    assert(error.asInstanceOf[ServiceError].getData.code  == ServiceError.Code.BAD_ID)
  }
}
