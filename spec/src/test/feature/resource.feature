Feature: Resource

  Scenario: Client can create new resource
    Given client holds valid token for own institute
    When client posts a new resource request
    Then response should be same as request
    And ID in response should not be null

  Scenario: Unauthorized client cannot create new resource
    Given client holds invalid token for own institute
    When client posts a new resource request
    Then response should be error
    And error type should be BAD_AUTHORIZATION

  Scenario: Client belonging to different institute cannot create resource
    Given client holds valid token for different institute
    When client posts a new resource request for other institute
    Then response should be error
    And error type should be BAD_AUTHORIZATION


# Update resource

#  Scenario: Owner can modify information for each resource
#    Given client holds valid token
#    When client put modified resource request for own institute
#    Then response should be same as request
#
#  Scenario: Owner can modify information for each resource
#    Given client holds valid token
#    And desired resource does not exist
#    When client put modified resource request for own institute
#    Then response should be error
#    And error type should be BAD_ID
#
#  Scenario: Client belonging to different institute cannot modify resource
#    Given client holds valid token for different institute
#    When client put modified resource request for own institute
#    Then response should be error
#    And error type should be BAD_AUTHORIZATION


# Delete resource

#  Scenario: Owner can delete given resource
#    Given client holds valid token
#    When client request delete of resource with given ID for own institute
#    Then response should contain the deleted resource
#
#  Scenario: Owner can delete given resource
#    Given client holds valid token
#    And desired resource does not exist
#    When client request delete of resource with given ID for own institute
#    Then response should be error
#    And error type should be BAD_ID
#
#  Scenario: Client belonging to different institute cannot delete resource
#    Given client holds valid token for different institute
#    When client request delete of resource with given ID for own institute
#    Then response should be error
#    And error type should be BAD_AUTHORIZATION
