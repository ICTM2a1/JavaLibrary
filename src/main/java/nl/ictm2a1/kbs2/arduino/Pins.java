package nl.ictm2a1.kbs2.arduino;

import nl.ictm2a1.kbs2.arduino.Arduino;
import org.ardulink.core.Pin;
import org.ardulink.core.Pin.AnalogPin;
import org.ardulink.core.Pin.DigitalPin;

/**
 * Created by Under_Koen on 25/03/2020.
 */
public class Pins {
    public static final DigitalPin PIN_0 = Pin.digitalPin(0);
    public static final DigitalPin PIN_1 = Pin.digitalPin(1);
    public static final DigitalPin PIN_2 = Pin.digitalPin(2);
    public static final PwmPin PIN_3 = new PwmPin(3);
    public static final DigitalPin PIN_4 = Pin.digitalPin(4);
    public static final PwmPin PIN_5 = new PwmPin(5);
    public static final PwmPin PIN_6 = new PwmPin(6);
    public static final DigitalPin PIN_7 = Pin.digitalPin(7);
    public static final DigitalPin PIN_8 = Pin.digitalPin(8);
    public static final PwmPin PIN_9 = new PwmPin(9);
    public static final PwmPin PIN_10 = new PwmPin(10);
    public static final PwmPin PIN_11 = new PwmPin(11);
    public static final DigitalPin PIN_12 = Pin.digitalPin(12);
    public static final DigitalPin PIN_13 = Pin.digitalPin(13);

    public static final AnalogPin ANALOG_1 = Pin.analogPin(1);
    public static final AnalogPin ANALOG_2 = Pin.analogPin(2);
    public static final AnalogPin ANALOG_3 = Pin.analogPin(3);
    public static final AnalogPin ANALOG_4 = Pin.analogPin(4);
    public static final AnalogPin ANALOG_5 = Pin.analogPin(5);
}
