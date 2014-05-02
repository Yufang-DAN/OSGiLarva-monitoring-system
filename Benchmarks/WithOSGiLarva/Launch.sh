echo "compile the source-code of new Larva-system:"
echo "create a classes file"
mkdir ../newLarvaSystem/source\ code/compiler

echo "compile the source-code"
javac -d ../newLarvaSystem/source\ code/compiler ../newLarvaSystem/source\ code/*.java


echo "inject the monitor into java code:"
echo "Compiler the *.lrv file"
java -cp ../newLarvaSystem/source\ code/compiler compiler.Compiler BuyService.lrv -o BuyService/src/main/java/

echo "running the code with OSGiLarva"
mvn install pax:run 


