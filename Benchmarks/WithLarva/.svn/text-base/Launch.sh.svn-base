echo "compile the source-code of old Larva-system:"
echo "create a classes file"
mkdir ../oldLarvaSystem/source\ code/compiler

echo "compile the source-code"
javac -d ../oldLarvaSystem/source\ code/compiler ../oldLarvaSystem/source\ code/*.java


echo "inject the monitor into java code:"
echo "Compiler the *.lrv file"
java -cp ../oldLarvaSystem/source\ code/compiler compiler.Compiler BuyService.lrv -o .

echo  "call aspectj for injecting monitor"
ajc -1.5 -cp aspectjrt.jar -sourceroots .

echo "call new BuyClient"
aj5 -cp . BuyClient
