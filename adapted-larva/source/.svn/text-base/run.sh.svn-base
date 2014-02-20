#!/bin/sh
echo "Running Larva Compiler"
java -cp ../Larva\ system compiler.Compiler BuyService.lrv -o .
cp ../Larva\ system/source\ code/addedbysfr/RootMonitor.java larva/
cp ../Larva\ system/source\ code/addedbysfr/_callable.java larva/
echo "Injecting Larva aspect"
ajc -1.5 -cp /opt/aspectj1.6/lib/aspectjrt.jar -d . -sourceroots .
echo "Running injected server"
aj5 -cp . nesting.Server
