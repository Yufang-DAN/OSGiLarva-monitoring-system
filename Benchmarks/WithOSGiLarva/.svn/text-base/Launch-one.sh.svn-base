echo "maxruns=1+4zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=100000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto2

echo "delete output file"
rm -rf *.txt *.cur

