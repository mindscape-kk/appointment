Feature: User Profile

  Scenario: User can see own profile
    Given client has user token and encryption key
    When client calls "GET" on "/user/self/profile"
    Then response should be same as the expected profile