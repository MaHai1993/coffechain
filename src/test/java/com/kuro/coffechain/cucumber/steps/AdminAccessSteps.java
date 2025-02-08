package com.kuro.coffechain.cucumber.steps;

import com.kuro.coffechain.cucumber.config.CucumberSpringConfiguration;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@AutoConfigureMockMvc
public class AdminAccessSteps extends CucumberSpringConfiguration {
    private ResponseEntity<String> response;
    private final RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    @Given("I am an admin user")
    public void i_am_an_admin_user() {
        // Simulating an authenticated admin user with basic auth
        headers.setBasicAuth("admin", "admin");
    }

    @When("I access the admin dashboard")
    public void i_access_the_admin_dashboard() {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = restTemplate.exchange("http://localhost:8080/admin/dashboard",
                HttpMethod.GET, entity, String.class);
    }

    @Then("I should receive success response")
    public void i_should_receive_success_response() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals("Admin Dashboard Accessed", response.getBody());
    }
}