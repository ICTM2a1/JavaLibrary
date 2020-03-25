package nl.ictm2a1.kbs2.arduino;

import org.ardulink.core.Pin;

/**
 * Created by Under_Koen on 25/03/2020.
 */
public class PwmPin {
    private int pin;
    private Pin.AnalogPin analogPin;
    private Pin.DigitalPin digitalPin;

    public PwmPin(int pin) {
        this.pin = pin;
        analogPin = Pin.analogPin(pin);
        digitalPin = Pin.digitalPin(pin);
    }

    public int getPin() {
        return pin;
    }

    public Pin.AnalogPin getAnalogPin() {
        return analogPin;
    }

    public Pin.DigitalPin getDigitalPin() {
        return digitalPin;
    }
}
