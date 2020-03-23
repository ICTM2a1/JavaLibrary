package nl.ictm2a1.kbs2;

import org.ardulink.core.Link;
import org.ardulink.core.Pin;
import org.ardulink.core.convenience.Links;
import org.ardulink.core.events.AnalogPinValueChangedEvent;
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
            Pin.DigitalPin led1 = Pin.digitalPin(12);
            Pin.DigitalPin led2 = Pin.digitalPin(13);
            Pin.AnalogPin potMeter = Pin.analogPin(5);
            EventListener listener = new EventListener(potMeter);
            link.addListener(listener);
            link.startListening(potMeter);
            for (boolean power = true; ; power = !power) {
                System.out.println("Send power: " + power);
                link.switchDigitalPin(led1, power);
                link.switchDigitalPin(led2, !power);
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Potmeter waarde is " + listener.getPotMeterValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
