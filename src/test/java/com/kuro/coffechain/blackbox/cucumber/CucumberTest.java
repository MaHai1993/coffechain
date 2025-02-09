package com.kuro.coffechain.blackbox.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


// Blackbox test so we MUST start the application before running this.
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.kuro.coffechain.blackbox.cucumber"},
        plugin = {"pretty", "json:target/cucumber-report.json"},
        monochrome = true
)
public class CucumberTest {
}
