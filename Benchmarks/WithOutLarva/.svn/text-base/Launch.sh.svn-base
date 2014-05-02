mkdir -p classes

echo "Compiling service"
javac -d classes BuyService.java

echo "Compiling client"
javac -d classes -cp classes BuyClient.java

echo -n "TESTING CODE : "

java -cp classes BuyClient

