# text-to-speech

## Simple test

    java -jar target/text-to-speech-1.0-SNAPSHOT-jar-with-dependencies.jar  example.properties

## Behind a proxy

    java -jar target/text-to-speech-1.0-SNAPSHOT-jar-with-dependencies.jar  example.properties \
      -Dhttp.proxyHost=myproxy -Dhttp.proxyPort=myport -Dhttp.proxyUser=myuser -Dhttp.proxyPassword=mypass
