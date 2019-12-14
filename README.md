# Heraclitus Fire

## Description
This project retrieves histories of schemata that have been processed on how the schema and the individual tables evolve and automatically produces visualizations and statistical tests for patterns of schema evolution  

## Installation
1. Install Java (jdk-13 is used)
The folder /lib comes with all the jar needed for the software to work.
A point of possible pain is the handling of JavaFX: all javaFx libs are included.
Although not thoroughly tested, Java 8 should probably work, too. 

## Usage
Must configure correctly the paths for
- java executable
- javafx lib
- location of the target HeraclitusFireRunable jar

For Shell

``` Shell
#!/bin/sh
usage()  
{  
	echo "Usage: $0 projectFolder \n\t assuming you are at the mother folder of projectFolder "  
 	exit 1  
} 

libpath="/home/pvassil/bin/javafx-sdk-13/lib"
echo $libpath

Xvfb :92 -screen 0 1024x768x16 &> xvfb.log &
export DISPLAY=:92.0

for D in *; do
    if [ -d "${D}" ]; then
	echo "${D}/"   
        java --module-path="$libpath" --add-modules="javafx.controls" --add-exports="javafx.controls/com.sun.javafx.charts=ALL-UNNAMED" -jar ./HeraclitusFireRunable_v0-4-3.jar $D
     fi
done
```

For DOS

``` DOS
for /d %i in (.\*) 
do (
"C:\Program Files\Java\jdk-13\bin\java" --module-path="C:\\Program Files\\Java\\JavaFX\\javafx-sdk-12.0.1\\lib" --add-modules="javafx.controls" --add-exports="javafx.controls/com.sun.javafx.charts=ALL-UNNAMED" -jar ./HeraclitusFireRunable_v0-4-3.jar %i
)
```

## License
See the [copyright](copyright.md) file

## ToDo ( / is for  partially, x for full, ! for important)
- [x] Code and Tests for code for table figure extraction
- [x] Code and tests for schema life figure extraction
- [ ] ! Code and tests for schema size pattern test (logistic equation?)
- [ ] ! Code and tests for schema heartbeat pattern test (SUPER OPEN)
- [x] Code and tests for gamma table pattern test 
- [x] Code and tests for inv. gamma table pattern test
- [ ] Code and tests for comet table pattern test
- [ ] Code and tests for empty triangle table pattern test
- [ ] !Code and tests for electrolysis table pattern test
- [ ] wrap the code in interfaces (at least mainEngine)
- [ ] client should be able to parameterize what of all extracts is needed


## History
### v0.2 [2019 Nov. - ]
* Alexandros Voulgaris started contributing to charting
* P. Vassiliadis started contributing pattern testing

### v0.1 [2019 Summer - November]
* P. Vassiliadis. Code started being developed with data loading and extraction of figures for tables.

