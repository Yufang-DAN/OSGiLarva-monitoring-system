echo "maxruns=5"
/usr/lib/jvm/java-6-openjdk/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dlogosng.protected=false -Dfr.inria.amazones.logosng.storage.level=WARNING -Dfr.citi.amazones.fmuse.client.maxruns=5 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto1 & sleep 10 
echo "delete output files"
rm -rf *.txt *.cur

echo "maxruns=10"
/usr/lib/jvm/java-6-openjdk/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dlogosng.protected=false -Dfr.inria.amazones.logosng.storage.level=WARNING -Dfr.citi.amazones.fmuse.client.maxruns=10 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto2 & sleep 5

echo "delete output files"
rm -rf *.txt *.cur



