package nl.ictm2a1.kbs2;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Under_Koen on 26/02/2020.
 */
public class Main {
    private static List<Arduino> arduinos;
    private static long last;

    public static void main(String[] args) throws InterruptedException {
        arduinos = Arduino.findUsbArduinos();
        for (Arduino arduino : arduinos) {
            if (arduino.open()) {
                Thread.sleep(1500);
                arduino.addReadHandler(s -> {
                    if (s.isEmpty()) return;
                    System.out.println(s);
                    try {
                        int i = Integer.parseInt(s);
                        if (i > 512) {
                            arduino.sendEvent('n', 1);
                            arduino.sendEvent('f', 2);
                        } else {
                            arduino.sendEvent('n', 2);
                            arduino.sendEvent('f', 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
