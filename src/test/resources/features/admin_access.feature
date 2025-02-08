Feature: Admin Dashboard Access

  Scenario: Admin accesses the dashboard successfully
    Given I am an admin user
    When I access the admin dashboard
    Then I should receive success response
