package nl.ictm2a1.kbs2;

import java.util.List;
import java.util.TooManyListenersException;

/**
 * Created by Under_Koen on 26/02/2020.
 */
public class Main {
    private static List<Arduino> arduinos;
    private static boolean state;

    public static void main(String[] args) throws InterruptedException, TooManyListenersException {
        arduinos = Arduino.findUsbArduinos();
        for (Arduino arduino : arduinos) {
            if (arduino.open()) {
                Thread.sleep(1500);
                arduino.addReadHandler(s -> {
                    if (s.isEmpty()) return;
                    System.out.print(String.format("value: %s\r"));
                    int i = Integer.parseInt(s);
                    if (state != (state = i > 500)) {
                        if (state) {
                            arduino.sendEvent('n', 1);
                            arduino.sendEvent('f', 2);
                        } else {
                            arduino.sendEvent('n', 2);
                            arduino.sendEvent('f', 1);
                        }
                    }
                });
            }
        }
    }
}
