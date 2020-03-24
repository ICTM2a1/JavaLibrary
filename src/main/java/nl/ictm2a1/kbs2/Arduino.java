package nl.ictm2a1.kbs2;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;

import java.io.Closeable;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Under_Koen on 18/03/2020.
 */
public class Arduino implements Closeable {
    public static List<Arduino> findUsbArduinos() {
        List<Arduino> arduinos = new ArrayList<>();

        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            String name = port.getSystemPortName();
            if (!name.toLowerCase().contains("usb")) continue;
            arduinos.add(new Arduino(port));
        }

        return arduinos;
    }

    private SerialPort port;
    private int baudRate;
    private List<Consumer<String>> readHandlers;
    private DataListener dataListener;
    private PrintWriter printWriter;

    public Arduino(SerialPort port) {
        this.port = port;
        this.readHandlers = new ArrayList<>();
        setBaudRate(9600);
    }

    public void setBaudRate(int baudRate) {
        port.setBaudRate(baudRate);
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
        return port.openPort();
    }

    @Override
    public void close() {
        port.closePort();
    }

    public void addReadHandler(Consumer<String> readHandler) {
        readHandlers.add(readHandler);
        if (dataListener == null) {
            dataListener = new DataListener((s) -> {
                String s2 = s.replaceAll("[\r\n\b\f]", "");
                readHandlers.forEach(c -> c.accept(s2));
            });
            port.addDataListener(dataListener);
        }
    }

    private static class DataListener implements SerialPortPacketListener {
        private Consumer<String> onLine;
        private StringBuilder builder;

        public DataListener(Consumer<String> onLine) {
            this.onLine = onLine;
            this.builder = new StringBuilder();
        }

        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            byte b = event.getReceivedData()[0];
            if (b == '\n') {
                onLine.accept(builder.toString());
                builder.setLength(0);
            } else {
                builder.append((char) b);
            }
        }

        @Override
        public int getPacketSize() {
            return 1;
        }
    }
}
