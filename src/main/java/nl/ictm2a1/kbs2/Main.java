package nl.ictm2a1.kbs2;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by Under_Koen on 26/02/2020.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        SerialPort[] ports = SerialPort.getCommPorts();
        List<SerialPort> usbs = Arrays.stream(ports)
                .filter(serialPort -> serialPort.getSystemPortName().contains("cu.usb"))
                .collect(Collectors.toList());

        for (SerialPort usb : usbs) {
            usb.setBaudRate(9600);
            if (usb.openPort()) {
                Thread.sleep(100);
                usb.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                InputStream in = usb.getInputStream();
                Scanner scannerInput = new Scanner(System.in);
                Scanner scannerArduino = new Scanner(usb.getInputStream());
                PrintWriter out = new PrintWriter(usb.getOutputStream());
                while(true) {
                    if (in.available() > 0) {
                        System.out.println(scannerArduino.next());
                    } else if (System.in.available() > 0) {
                        out.println(scannerInput.next());
                        out.flush();
                    }
                    Thread.sleep(100);
                }
            } else {
                System.out.println(usb.getSystemPortName() + " INCORRECT");
            }
        }
    }
}
