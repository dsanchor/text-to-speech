package com.microsoft.csu.tts;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.Scanner;

/**
 * https://docs.microsoft.com/en-us/java/api/overview/azure/cognitiveservices/client/speechservice?view=azure-java-stable
 *
 */
public class App 
{

    private static final String TEXT_TEMPLATE="<speak version=\"{0}\" xmlns=\"http://www.w3.org/2001/10/synthesis\" xml:lang=\"{1}\"><voice name=\"{2}\">{3}</voice></speak>";

    // TODO if wanted, add these constants as properties
    
    private static final String SSML_VERSION="1.0";

    private static final String LANGUAGE="en-us";

    private static final String VOICE="en-us-guyneural";

    
    private enum Mode {AZURE, AKS, NONE};

    private static Mode mode;

    private static String subscriptionKey;

    private static String location;

    private static String aksTtsUri;

    private static final String EXIT_STRING="quit";

    public static void main( String[] args ) {

        initProperties(args[0]);
        // Load SpeechConfig based on app.mode 
        SpeechConfig sc = null;
        if (getMode().equals(Mode.NONE)) {
            System.err.println("Please, set app.mode to either AZURE or AKS");
            System.exit(-1);
        } else if (getMode().equals(Mode.AZURE)) {
            sc = SpeechConfig.fromSubscription(getSubscriptionKey(), getLocation());
        } else if (getMode().equals(Mode.AKS)) {
            try {
                sc = SpeechConfig.fromHost(new URI(getAksTtsUri()));
            } catch (URISyntaxException e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        }

        SpeechSynthesizer ss = null;
        Scanner scanner = null;
        try {
            ss = new SpeechSynthesizer(sc);
            String text = "";
            while (!text.equals(EXIT_STRING)) {
                scanner = new Scanner(System.in);
                System.out.println("Write a message: ");
                text = scanner.nextLine();
            
                String message = MessageFormat.format(TEXT_TEMPLATE, SSML_VERSION, LANGUAGE, VOICE, text);
                System.out.println( message );
    
                SpeechSynthesisResult  ssr = ss.SpeakSsml(message);    
    
                System.out.println("Result: " + ssr.getReason()); 
            }

        } catch (Throwable t) {
            System.err.println("Error getting audio: "  + t.getMessage());
        } finally {
            if (scanner!=null) {
                scanner.close();
            }
            if (ss!=null) {
                ss.close();
            }
        }

        if (sc!=null) {
            sc.close();
        }
    }


    private static void initProperties(String propertiesPath) {

        System.out.println("Loading properties from " + propertiesPath);
        Properties props = new Properties();
        FileInputStream fis = null;
        setMode(Mode.NONE);
        try {
            fis = new FileInputStream(propertiesPath);
            props.load(fis);
        
            String mode = props.getProperty("app.mode");
            if (mode!=null && !"".equals(mode)) {
                setMode(Mode.valueOf(mode.toUpperCase()));
            } 
            if (getMode().equals(Mode.AZURE)) {
                setSubscriptionKey(props.getProperty("subs.key"));
                setLocation(props.getProperty("location"));
            } else if (getMode().equals(Mode.AKS)) {
                setAksTtsUri(props.getProperty("aks.tts.uri"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis!=null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // GETTERS/SETTERS

    public static Mode getMode() {
        return mode;
    }


    public static void setMode(Mode mode) {
        App.mode = mode;
    }


    public static String getSubscriptionKey() {
        return subscriptionKey;
    }


    public static void setSubscriptionKey(String subscriptionKey) {
        App.subscriptionKey = subscriptionKey;
    }


    public static String getLocation() {
        return location;
    }


    public static void setLocation(String location) {
        App.location = location;
    }


    public static String getAksTtsUri() {
        return aksTtsUri;
    }


    public static void setAksTtsUri(String aksTtsUri) {
        App.aksTtsUri = aksTtsUri;
    }


}
