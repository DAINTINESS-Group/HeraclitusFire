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
Then, you can massively work with multiple data sets, nested under the same 'mother' folder as follows.

For shell, cd to the 'mother' folder, edit the aforementioned variables in the following script and run it

``` Shell
#!/bin/sh
usage()  
{  
	echo "Usage: $0 projectFolder \n\t assuming you are at the mother folder of projectFolder "  
 	exit 1  
} 

libpath="/home/pvassil/bin/javafx-sdk-13/lib"
runnableJar="./HeraclitusFireRunable_v0-4-3.jar"
echo libpath is: $libpath
echo runnableJar is: $runnableJar
echo

Xvfb :92 -screen 0 1024x768x16 &> xvfb.log &
export DISPLAY=:92.0

for D in *; do
    if [ -d "${D}" ]; then
	echo "${D}/"   
        java --module-path "$libpath" --add-modules "javafx.controls"   --add-exports javafx.controls/com.sun.javafx.charts=ALL-UNNAMED  -jar "$runnableJar" $D
     fi
done
```

For DOS, cd to the mother folder, edit the aforementioned variables and paste into the cmd prompt line.

``` DOS
for /d %i in (.\*) 
do (
"C:\Program Files\Java\jdk-13\bin\java" --module-path "C:\\Program Files\\Java\\JavaFX\\javafx-sdk-12.0.1\\lib" --add-modules "javafx.controls"  --add-exports javafx.controls/com.sun.javafx.charts=ALL-UNNAMED -jar ./HeraclitusFireRunable_v0-4-3.jar %i
)
```

## License
See the [copyright](copyright.md) file

## ToDo ( / is for  partially, x for full, ! for important)
- [ ] ! Code and tests for schema size pattern test (logistic equation?)
- [ ] ! Code and tests for schema heartbeat pattern test (SUPER OPEN)
- [ ] Code and tests for comet table pattern test
- [ ] Code and tests for empty triangle table pattern test
- [/] wrap the code in interfaces (at least mainEngine)
- [ ] client should be able to parameterize what of all extracts is needed
- [x] Code and Tests for code for table figure extraction
- [x] Code and tests for schema life figure extraction
- [x] Code and tests for gamma table pattern test 
- [x] Code and tests for inv. gamma table pattern test
- [x] Code and tests for electrolysis table pattern test

## History
### v0.5 [2020 Oct - 2021 Jan]
* Georgios Kalampokis improved the monthly schema stats

### v0.2 - v0.4.5 [2019 Nov. - 2020 Jan]
* Alexandros Voulgaris started contributing to reporting, charting and pattern assessment
* P. Vassiliadis started contributing pattern testing

### v0.1 [2019 Summer - November]
* P. Vassiliadis. Code started being developed with data loading and extraction of figures for tables.

