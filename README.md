# Simple Meeting
This is an example used
in a lecture for the Hogeschool
Utrecht regarding basic automated testing.

Students are invited to take a look at:
* the [pom.xml](pom.xml) 
for Maven plug-ins, dependencies and goals
* the [maven.yml](.github/workflows/maven.yml) 
for configuration of the steps in the GitHub Actions build pipeline
* the [test directory](src/test) 
for examples of automated testing
* the [Postman collection](Simple%20Meeting.postman_collection.json)
for empowering local development

## Badges
![Java CI with Maven](https://github.com/arothuis-hu/simple-meeting-example/workflows/Java%20CI%20with%20Maven/badge.svg)
[![codecov](https://codecov.io/gh/arothuis-hu/simple-meeting-example/branch/master/graph/badge.svg)](https://codecov.io/gh/arothuis-hu/simple-meeting-example)

Badges can display the status and the metrics for your
projects. 

In this example, 
GitHub Actions is used as a build pipeline. The badge
shows whether the recent build was successful.
Coverage is generated via JaCoCo and Maven (see `pom.xml`).
Afterwards, the coverage is published to codecov.
Codecov gives an insight in the coverage of the project
over time. 

Right now, coverage could be better!

## To do
This example project, although useful, is incomplete.

* Improve overall coverage
* Add tests for controllers
* Show more examples of test doubles using Mockito
* Add Spring system tests
* Add more internal integration tests
* Add end-to-end tests using cucumber and Selenium
    * Requires a live application
* Add complexity analysis tools and badges like SonarCloud
