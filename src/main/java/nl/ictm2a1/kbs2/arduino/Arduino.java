package nl.ictm2a1.kbs2.arduino;

import org.ardulink.core.Link;
import org.ardulink.core.Pin;
import org.ardulink.core.Pin.AnalogPin;
import org.ardulink.core.Pin.DigitalPin;
import org.ardulink.core.Tone;
import org.ardulink.core.convenience.Links;
import org.ardulink.core.events.AnalogPinValueChangedEvent;
import org.ardulink.core.events.DigitalPinValueChangedEvent;
import org.ardulink.core.events.EventListener;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Under_Koen on 25/03/2020.
 */
public class Arduino implements EventListener {
    private Link link;
    private List<PinEvent<AnalogPin, Integer>> analogListeners = new ArrayList<>();
    private List<PinEvent<DigitalPin, Boolean>> digitalListeners = new ArrayList<>();

    public Arduino() {
        link = Links.getDefault();
        init();
    }

    public Arduino(URI uri) {
        link = Links.getLink(uri);
        init();
    }

    private void init() {
        try {
            link.addListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPin(DigitalPin pin, boolean value) {
        try {
            link.switchDigitalPin(pin, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPin(PwmPin pin, boolean value) {
        setPin(pin.getDigitalPin(), value);
    }

    public void setPin(PwmPin pin, int value) {
        try {
            link.switchAnalogPin(pin.getAnalogPin(), value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tone(PwmPin pin, int hertz, int duration, TimeUnit timeUnit) {
        try {
            link.sendTone(Tone.forPin(pin.getAnalogPin()).withHertz(hertz).withDuration(duration, timeUnit));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tone(PwmPin pin, int hertz) {
        try {
            link.sendTone(Tone.forPin(pin.getAnalogPin()).withHertz(hertz).endless());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void noTone(PwmPin pin) {
        try {
            link.sendNoTone(pin.getAnalogPin());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPinListener(AnalogPin pin, EventHandler<Integer> eventHandler) {
        try {
            link.startListening(pin);
            analogListeners.add(new PinEvent<>(pin, eventHandler));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPinListener(DigitalPin pin, EventHandler<Boolean> eventHandler) {
        try {
            link.startListening(pin);
            digitalListeners.add(new PinEvent<>(pin, eventHandler));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPinListener(PwmPin pin, EventHandler<Boolean> eventHandler) {
        addPinListener(pin.getDigitalPin(), eventHandler);
    }

    @Override
    public void stateChanged(AnalogPinValueChangedEvent event) {
        if (event == null) return;
        AnalogPin pin = event.getPin();
        int value = event.getValue();

        for (PinEvent<AnalogPin, Integer> analogListener : analogListeners) {
            if (pin.equals(analogListener.pin)) {
                try {
                    analogListener.eventHandler.handle(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void stateChanged(DigitalPinValueChangedEvent event) {
        if (event == null) return;
        DigitalPin pin = event.getPin();
        boolean value = event.getValue();

        for (PinEvent<DigitalPin, Boolean> digitalListener : digitalListeners) {
            if (pin.equals(digitalListener.pin)) {
                try {
                    digitalListener.eventHandler.handle(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class PinEvent<P extends Pin, V> {
        private P pin;
        private EventHandler<V> eventHandler;

        public PinEvent(P pin, EventHandler<V> eventHandler) {
            this.pin = pin;
            this.eventHandler = eventHandler;
        }
    }

    public interface EventHandler<T> {
        void handle(T t) throws Exception;
    }
}
