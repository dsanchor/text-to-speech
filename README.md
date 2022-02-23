# text-to-speech

## Generate jar

    mvn clean install

## Simple test

    java -jar target/text-to-speech-1.0-SNAPSHOT-jar-with-dependencies.jar  example.properties

## Test behind a proxy

    java -jar target/text-to-speech-1.0-SNAPSHOT-jar-with-dependencies.jar  example.properties \
      -Dhttp.proxyHost=myproxy -Dhttp.proxyPort=myport -Dhttp.proxyUser=myuser -Dhttp.proxyPassword=mypass
