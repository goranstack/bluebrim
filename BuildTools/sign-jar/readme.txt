# To generate a new certificate, use the following in a command window (in this folder)
C:\Program\Java\jdk1.6.0_05\bin\keytool -genkey -alias bluebrimcert -keypass BlueBrim -dname "CN=BlueBrim, O=BlueBrim, L=Stockholm, C=SE" -validity 500 -keystore .keystore

# To delete the old certificate
C:\Program\Java\jdk1.6.0_05\bin\keytool -delete -alias bluebrimcert -keystore .keystore