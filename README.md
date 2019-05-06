# Getting Started

Welcome to Test Automation Project. The structure of the project is as follows.


File / Folder | Purpose
---------|----------
`src/` | contains java source files.
`drivers/` | driver files for firefox and chrome browsers. chrome-75 folder contains driver for chrome version 75. The provided driver works for chrome version 74. 
`libs\` | contains selenium dependencies required for this project.
`screenshots` | screenshots goes to this folder.
`test-output` | test ng reports goes here.
`readme.md` | this readme.
`.project` | project file for eclipse.
`testng-configuration.properties` | configuration file for test ng.
`TestSuite.txt` | contains the test cases


The following sections are a quick walkthrough of essential tasks:

<!-- TestNG installation in Eclipse -->

- Open Eclipse and click on Help -> Eclipse Marketplace.
- Search for TestNG for Eclipse and click on Install. 
- Proceed through the default options and confirm the installation. 
- After TestNG installation, add it to the build path of the project. To add to build path, right click on project -> properties -> Java Build Path -> Libraries -> Add Library -> add TestNG and finish. 

<!-- Driver configurations -->

- The chrome and firefox drivers are placed in drivers folder.
- The provided chrome driver supports chrome version 74. Use chrome-75/chromedriver.exe driver for chrome version 75.

<!-- TestNG configuration -->

- This project reads configuration for selenium from testng-configuration.properties file.
- The property "selenium.browser" decides which browser to use for running automation test. possible values are firefox & chrome.
- The property "firefox.driver" stores the location of firefox driver. The default value given is "drivers/geckodriver.exe".
- The property "chrome.driver" stores the location of chrome driver. The default value given is "drivers/chromedriver.exe".

<!-- How to add selenium libraries to build path -->

- To add selenium dependencies, right click on project -> properties -> Java Build Path -> Libraries -> Add External JARs -> select libraries provided in libs/selenium folder.

<!-- How to Run Test cases -->

- To run test cases, right click on LoginAutomationTest.java -> run as -> select TestNG Test. 
- After execution of test suite, the report will be saved to test-output folder. 

<!-- How to check the report --> 

- After execution of test suite, open test-output/index.html in browser to view the test results.
- TestNG also provides test results in a xml format. open test-output/testng-results.xml to view the report. 