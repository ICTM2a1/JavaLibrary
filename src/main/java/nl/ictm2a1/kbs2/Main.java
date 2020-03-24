package nl.ictm2a1.kbs2;

import gnu.io.NRSerialPort;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * Created by Under_Koen on 26/02/2020.
 */
public class Main {
    public static void main(String[] args) {
        List<Arduino> arduinos = Arduino.findUsbArduinos();
        for (Arduino arduino : arduinos) {
            try {
                if (arduino.open()) {
                    Thread.sleep(1500);
                    arduino.addReadHandler(s -> {
                        if (s.isEmpty()) return;
                        System.out.println(s);
                        int i = Integer.parseInt(s);
                        if (i > 512) {
                            arduino.sendEvent('n', 1);
                            arduino.sendEvent('f', 2);
                        } else {
                            arduino.sendEvent('n', 2);
                            arduino.sendEvent('f', 1);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
