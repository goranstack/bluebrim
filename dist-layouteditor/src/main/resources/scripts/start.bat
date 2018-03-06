set memory=-Xms256m -Xmx1024m
set mainJar=${project.artifactId}-${project.version}.jar
java %memory% -jar %mainJar%
echo  ------------------------------ 
echo    Script will exit now, bye!  
echo  ------------------------------ 

pause
