# WebDriverIOAppTest

## Useful commands

### To run all cases from the TestCases class:
```commandline
mvn clean test -Dsurefire.suiteXmlFiles=all-tests.xml
```
### To run cases by groups:
- the groups not to be executed can be commented out from the group-tests.xml
```commandline
mvn clean test -Dsurefire.suiteXmlFiles=group-tests.xml
```
