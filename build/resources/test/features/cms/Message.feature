Feature: User should be able to send a message from gmail

  Scenario: Send a message  to an enother user
#    Given I send a message to 'testing1234@mailinator.com' from my gmail using API
    When I open 'https://mailinator.com/v3/' site
    And I login as 'testing1234@mailinator.com'
    And I open last message
    Then I am able to extract a link from the email body

