package nl.ictm2a1.kbs2;

import gnu.io.NRSerialPort;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Under_Koen on 26/02/2020.
 */
public class Main {
    public static void main(String[] args) {
        for(String s: NRSerialPort.getAvailableSerialPorts()){
            System.out.println("Availible port: "+s);
        }
        String port = "COM3";
        int baudRate = 9600;
        NRSerialPort serial = new NRSerialPort(port, baudRate);
        serial.connect();

        DataInputStream ins = new DataInputStream(serial.getInputStream());
        DataOutputStream outs = new DataOutputStream(serial.getOutputStream());

        try {
            int b = ins.read();
            System.out.println(b);
            outs.write(b);

        } catch (IOException e) {
            e.printStackTrace();
        }

        serial.disconnect();
    }
}
