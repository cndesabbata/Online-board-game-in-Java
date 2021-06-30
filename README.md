# ing_sw_2021_Radi_Sanchini_De_Sabbata
Progetto Ingegneria Del Software 2021

## Project specification
The project consists of a Java implementation of the board game Master
of Renaissance, made by Cranio Creations.

## Implemented functionalities
- Basic rules
- Complete rules
- CLI
- GUI
- Socket
- 2 AF (advanced functionalities):
    - Multiple games
    - Resilience to disconnections

## Test cases
Class coverage for model and controller packages is 100%.

**Coverage criteria: code lines.**

| Package |Tested Class | Coverage |
|:-----------------------|:------------------|:------------------------------------:|
| Model | GlobalPackage | 493/629 (78%)
| Leaders | GlobalPackage | 36/44 (81%)
| Controller | GlobalPackage | 181/365 (49%)
| Actions | GlobalPackage | 203/270 (75%)

## Documentation

### Javadoc
The JavaDoc for this project contains the description of all classes and methods developed.
You can read it here: [Javadoc]

### UML
The following UML diagrams represent a high level description of the project, and a detailed one that contains all
classes with their attributes and methods.


## Jar and Execution
The project jar can be downloaded here: [Jar]. To execute it, you will need at least Java 14.

To start the jar, execute this command in your terminal:
```
java -jar master-of-renaissance.jar
```
Once the programs starts, you will be able to choose to launch the server, the CLI or the GUI.

[Javadoc]: https://gianlucaradi.github.io/ing_sw_2021_Radi_Sanchini_De_Sabbata
[Jar]: https://github.com/GianlucaRadi/ing_sw_2021_Radi_Sanchini_De_Sabbata
