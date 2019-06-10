Feature: Resource

  Scenario: Client can create new resource
    Given client has user token "mandy" and encryption key "abcdef"
    When client posts a new resource request
    """
    {
      "type":"room",
      "name":"K2",
      "description":"waiting room",
      "userDefinedProperties":["test","test-val-K"]
    }
    """
    Then response should be
    """
    {
      "type":"room",
      "name":"K2",
      "description":"waiting room",
      "userDefinedProperties":["test","test-val-K"]
    }
    """