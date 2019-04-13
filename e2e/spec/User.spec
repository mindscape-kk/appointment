Feature: User Profile

Scenario: User can see own profile
  Given app has valid token
  When app access profile endpoint
  Then app shuold receive profile data
