echo ""

java -cp ../adapted-larva/Larva\ system compiler.Compiler BuyServiceJustForEachClient.lrv -o BuyService1/src/main/java/

mvn install pax:run -U
