Feature: Resource

  Scenario: Client can create new resource
    Given client has user token "mandy" and encryption key "abcdef"
    When client posts a new resource request
    Then response should be same as request
    And ID in response should not be null