package nl.ictm2a1.kbs2;

import gnu.io.NRSerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.function.Consumer;

/**
 * Created by Under_Koen on 18/03/2020.
 */
public class Arduino implements Closeable {
    public static List<Arduino> findUsbArduinos() {
        List<Arduino> arduinos = new ArrayList<>();
        for (String s : NRSerialPort.getAvailableSerialPorts()) {
            NRSerialPort port = new NRSerialPort(s, 9600);
            System.out.println(s);
            arduinos.add(new Arduino(port));
        }

        return arduinos;
    }

    private NRSerialPort port;
    private int baudRate;
    private List<Consumer<String>> readHandlers;
    private SerialReader serialReader;
    private PrintWriter printWriter;

    public Arduino(NRSerialPort port) {
        this.port = port;
        this.readHandlers = new ArrayList<>();
        setBaudRate(9600);
    }

    public void setBaudRate(int baudRate) {
        port.setBaud(baudRate);
        this.baudRate = baudRate;
    }

    /**
     * @return the baudrate of the port.<br><b>Default:</b> 9600
     */
    public int getBaudRate() {
        return baudRate;
    }

    public void sendEvent(char t, int v) {
        write(t);
        write((char) v);
        flush();
    }

    public void write(String s) {
        if (printWriter == null) printWriter = new PrintWriter(port.getOutputStream());
        printWriter.write(s);
    }

    public void write(char c) {
        if (printWriter == null) printWriter = new PrintWriter(port.getOutputStream());
        printWriter.write(c);
    }

    public void writeln(String s) {
        write(s + "\n");
        printWriter.flush();
    }

    public void flush() {
        printWriter.flush();
    }

    public boolean open() {
        return port.connect();
    }

    @Override
    public void close() {
        port.disconnect();
    }

    public void addReadHandler(Consumer<String> readHandler) throws TooManyListenersException {
        readHandlers.add(readHandler);
        if (serialReader == null) {
            serialReader = new SerialReader(port.getInputStream(), (s) -> {
                String s2 = s.replaceAll("[\r\n\b\f]", "");
                readHandlers.forEach(c -> c.accept(s2));
            });
            port.addEventListener(serialReader);
        }
    }

    private static class SerialReader implements SerialPortEventListener {
        private InputStream in;
        private Consumer<String> onLine;
        private StringBuilder builder;

        public SerialReader(InputStream in, Consumer<String> onLine) {
            this.in = in;
            this.onLine = onLine;
            this.builder = new StringBuilder();
        }

        public void serialEvent(SerialPortEvent event) {
            try {
                int b;
                while ((b = in.read()) != -1) {
                    if (b == '\n') {
                        onLine.accept(builder.toString());
                        builder.setLength(0);
                    } else {
                        builder.append((char) b);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

    }
}
