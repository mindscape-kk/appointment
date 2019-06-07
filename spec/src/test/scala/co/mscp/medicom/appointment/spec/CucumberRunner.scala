package co.mscp.medicom.appointment.spec

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(
  plugin = Array("pretty"),
  glue = Array("co.mscp.medicom.appointment.spec"),
  features = Array("src/test/feature"))
class CucumberRunner