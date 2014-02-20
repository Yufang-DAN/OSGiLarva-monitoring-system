echo ""

java -cp ../adapted-larva/Larva\ system compiler.Compiler BuyServiceForEach.lrv -o BuyService1/src/main/java/

java -cp ../adapted-larva/Larva\ system compiler.Compiler BuyServiceForEach.lrv -o BuyService2/src/main/java/

mvn install pax:run -U
