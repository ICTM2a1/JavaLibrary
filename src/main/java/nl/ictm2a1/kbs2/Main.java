package nl.ictm2a1.kbs2;

import nl.ictm2a1.kbs2.arduino.Arduino;

import static nl.ictm2a1.kbs2.arduino.Pins.*;

/**
 * Created by Under_Koen on 26/02/2020.
 */
public class Main {
    private static boolean state;

    public static void main(String[] args) throws InterruptedException {
        Arduino arduino = new Arduino();
        arduino.addPinListener(ANALOG_5, i -> {
            System.out.print(String.format("value: %s\r", i));
            boolean which = i > 512;
            if (state != (state = which)) {
                arduino.setPin(PIN_13, which);
                arduino.setPin(PIN_12, !which);
            }
        });

        Thread.sleep(Long.MAX_VALUE);
    }
}
