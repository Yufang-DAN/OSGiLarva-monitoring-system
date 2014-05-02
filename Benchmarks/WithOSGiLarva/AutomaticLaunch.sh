echo "maxruns=1+4zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=10000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto1 

sleep 10

echo "delete output file"
rm -rf *.txt *.cur


echo "maxruns=1+5zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=100000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto2 

sleep 10

echo "delete output file"
rm -rf *.txt *.cur

echo "maxruns=1+6zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=1000000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto3 

sleep 20

echo "delete output file"
rm -rf *.txt *.cur


echo "maxruns=3+6zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=3000000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto4 

sleep 30

echo "delete output file"
rm -rf *.txt *.cur

echo "maxruns=5+6zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=5000000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto5 

sleep 40

echo "delete output file"
rm -rf *.txt *.cur


echo "maxruns=7+6zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=7000000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto6 

sleep 50

echo "delete output file"
rm -rf *.txt *.cur

echo "maxruns=9+6zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=9000000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto7 

sleep 50

echo "delete output file"
rm -rf *.txt *.cur

echo "maxruns=1+7zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=10000000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto8 

sleep 60

echo "delete output file"
rm -rf *.txt *.cur


echo "maxruns=3+7zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=30000000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto9 

sleep 70

echo "delete output file"
rm -rf *.txt *.cur


echo "maxruns=5+7zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=50000000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto10 

sleep 80

echo "delete output file"
rm -rf *.txt *.cur


echo "maxruns=7+7zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=70000000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto11 

sleep 90

echo "delete output file"
rm -rf *.txt *.cur


echo "maxruns=9+7zeros"
/etc/java-config-2/current-system-vm/bin/java -Dfelix.config.properties=file:felix/config.ini -Dlogosng.protected.consoledebug=true -Dlogosng.compress=true -Dfr.inria.amazones.logosng.interception.level=INFO -Dlogosng.logging.maxnumberofsavedfiles=40 -Dlogosng.logging.logfilesize=500000 -Dlogosng.logging.logfilename=logosng-recorded-on -Dlogosng.crypted=false -Dlogosng.aop.skip-services=org.apache.felix.gogo.shell.Converters,org.apache.felix.service.command.CommandProcessor,org.apache.felix.service.threadio.ThreadIO -Dfr.inria.amazones.logosng.storage.level=WARNING -Dlogosng.protected=false -Dfr.citi.amazones.fmuse.client.maxruns=90000000 -cp bundles/org.apache.felix.main_4.0.2.jar org.apache.felix.main.Main 2> toto12 

sleep 100

echo "delete output file"
rm -rf *.txt *.cur
