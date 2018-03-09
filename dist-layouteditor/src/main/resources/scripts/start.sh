#!/bin/bash
memory="-Xms256m -Xmx1024m"
mainJar="${project.artifactId}-${project.version}.jar"
java $memory -jar $mainJar
read -n1 -r -p "Press any key to continue..." key