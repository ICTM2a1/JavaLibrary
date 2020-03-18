package nl.ictm2a1.kbs2;

import org.ardulink.core.Link;
import org.ardulink.core.Pin;
import org.ardulink.core.convenience.Links;
import org.ardulink.util.URIs;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Under_Koen on 26/02/2020.
 */
public class Main {
    public static void main(String[] args){
        try {
            Link link = Links.getLink(URIs.newURI("ardulink://serial-jssc?port=COM3"));
            Pin.DigitalPin pin = Pin.digitalPin(13);
            for (boolean power = true; ; power = !power) {
                System.out.println("Send power: " + power);
                link.switchDigitalPin(pin, power);
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
