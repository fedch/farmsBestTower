# FarmsBestTower
This project is a part of the Comms Intern Tech Test.
It is designed to find the best communication tower for a given farm. The tower is considered the "best" if the average RSSI (Received Signal Strength Indicator) is the highest amongst the towers for the given farm ID.

## Overview
The program starts by fetching a list of file links from a provided AWS endpoint. Each link corresponds to a CSV file containing records for various farm-tower combinations and their RSSI values. The program processes each CSV file, calculates the average RSSI for each tower corresponding to the provided farm ID, and finally identifies the tower with the highest average RSSI.

Please note:

* While some links providing file paths are accessible, others aren't, which is assumed to be a part of the test scenario. This is accounted for in the code: it will continue calculating the average RSSI values in the accessible files, but will log the inaccessible ones with the 403 responce.
* As per the test requirements, the primary functionality is encapsulated into a single function called `fetchBestTower`.

## Prerequisites
* Java JDK (Recommended version: 18 or above)
* Apache Maven

## Running the Code
1. Clone the repository:
```console
git clone https://github.com/fedch/farmsBestTower.git
```
2. Navigate to the project directory:
```console
cd FarmsBestTower
```
3. If using maven, compile and run the project as follows:
```console
mvn clean compile
mvn exec:java -Dexec.mainClass="com.mycompany.farmsbesttower.FarmsBestTower"
```
Alternatively, you can use any IDE that supports Java, open the project, and run the `FarmsBestTower` main class.

4. To run tests (maven):
```console
mvn test
```
Alternatively, you can use any IDE that supports Java, open the project, and run the `FarmsBestTowerTest` class.
