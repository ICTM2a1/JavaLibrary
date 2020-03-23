package nl.ictm2a1.kbs2;

import org.ardulink.core.Pin;
import org.ardulink.core.events.AnalogPinValueChangedEvent;
import org.ardulink.core.events.EventListenerAdapter;

public class EventListener extends EventListenerAdapter {
    private final Pin potMeter;
    private int potMeterValue;

    public EventListener(Pin potMeter) {
        this.potMeter = potMeter;
    }

    @Override
    public void stateChanged(AnalogPinValueChangedEvent event) {
        assert event != null;
        if(event.getPin().equals(potMeter)) {
            potMeterValue = event.getValue();
            //System.out.println(potMeterValue);
        }
    }

    public int getPotMeterValue() {
        return potMeterValue;
    }
}
