@echo off 
echo ******************************************************************************
echo *  This will automatically weave and run your application                    *
echo *  make sure that your PATH directory gives direct access to the commands:   *
echo *  javac, ajc, aj5                                                           *
echo *  also ensure that the source code of your application is available         *
echo ******************************************************************************

cd .\

SET F=
SET /P F=Enter script file: 

SET O=
SET /P O=Enter application root directory: 

SET M=
SET /P M=Enter main java file (relative to the root directory): 

SET A=
SET /P A=Enter application command line: 


echo generating files...
java -cp . compiler.Compiler %F% -o %O%

REM echo compiling files...
REM cd %O%
REM dir *.java /B/S/X > _files.src
REM javac -cp %O% @_files.src
REM works for paths with no spaces

echo compiling files...
javac -cp %O% %O%/%M%

echo compiling files...
javac -cp %O% %O%/larva/*.java

echo compiling aspects...
call ajc -1.5 -cp aspectjrt.jar;%O% -outxmlfile %O%\META-INF\aop.xml %O%\aspects\*.aj

echo running application...
call aj5 -cp %O% %A%

pause