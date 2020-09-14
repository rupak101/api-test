# API Automation Test Suite

<br/>This automated API test suite covers all the test cases mentioned in Tech challenge file.

# Tools, Framework,Programming Language used: 
   * IntelliJ IDE, maven, TestNG, Java 8+, Hamcrest, RestAssured
   
# Development environment : 
   * All development and execution done on Mac OS.It should work on other OS(e.g windows) as well. 
      
# Libraries Used
* TestNG:
    * To perform parallel execution of test.
    
* Client architecture
    -RestAssured client consist all type of request (e.g GET,POST etc).
    
* Schema validation:
  *  Validate the response by json schema validator.
    
# Features:
* Generation human readable allure report
    - HTML Reports are available in the "/target/allure-report" directory having details of each test case execution.

* Configurator(via testng.xml file):
  * run tests in parallel mode;
    - Test cases executed in parallel with maximum thread count of 5.

* Allure report: 
  *Integrate to defect tracking system by using @link
  *Test order by severity by using @Severity annotation.
  *Tests groups with @Epic, @Feature, and @Stories annotations.
  
# Prerequisite to run the tests:
  * App can start with `python3 app.py` and will run on a local machine before executing the test
  
# Steps to execute the project:
* Method to run in Terminal or command line :
    * Go to project folder in the terminal or command line
    
    Run the below command
    ```
    mvn clean test
    ``` 
    Run the command to generate allure report and open in a browser: 
    ```bash
    mvn allure:serve
    ```
    