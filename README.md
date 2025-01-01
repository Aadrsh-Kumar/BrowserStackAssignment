# BrowserStackAssignment
Project containing solutions  for BrowserStack Round 2 Assignment

Steps to Set Up and Run Tests:

1. Clone the Repository
Clone the repository to your local machine using Git:

2. Import the Maven Project into Your IDE
Open your favorite IDE (e.g., IntelliJ IDEA, Eclipse) and import the project as a Maven project.

3. Compile and Build the Project
Once the project is imported, Maven will automatically resolve the dependencies. If not, you can manually build the project using the following steps:

In IntelliJ IDEA: Build > Rebuild Project.
Or,
mvn clean install

4. Replace Config Values in automationConfig.properties File
Before running the tests, ensure that the following configurations in the config.properties file are properly set:

isLocal: Set to false to run tests on BrowserStack (or true for local execution).
GoogleCloudAPIKey: Replace <GoogleAPIKey> with your actual Google Cloud API Key (if required).
BROWSERSTACK_USERNAME and BROWSERSTACK_ACCESS_KEY: Set your BrowserStack credentials (username and access key).

5. Run the testRunner.xml File
After replacing the values in the config.properties file, the next step is to run the testRunner.xml file, which contains the TestNG configuration to execute the tests.

6. Execution Details
The execution will start in parallel on BrowserStack based on the configurations in testRunner.xml. The tests will be executed across different platforms and browsers, including Chrome, Firefox, Edge, and mobile devices.

During the test execution, The given assignment task will be perfomed.


