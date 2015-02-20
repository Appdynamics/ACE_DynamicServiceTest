#!/bin/sh

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
mkdir -p $DIR/logs

# Change this if needed
#


export AGENTDIR=$DIR/agent/

appName="ServiceTest"


export JAVA_OPTS="-javaagent:$AGENTDIR/javaagent.jar
-Dappdynamics.agent.applicationName=$appName
-Dappdynamics.controller.hostName=${controllerHost-localhost}
-Dappdynamics.controller.port=${controllerPort-8090}
-Dappdynamics.agent.tierName=Frontend
-Dappdynamics.agent.nodeName=Client1
-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=9201
-Dcom.sun.management.jmxremote.local.only=false
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false"


$DIR/TestApp/bin/TestApp $*