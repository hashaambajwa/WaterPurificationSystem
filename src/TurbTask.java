import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;

import java.io.IOException;
import java.util.TimerTask;

public class TurbTask extends TimerTask {
    private final Pin turbPin;
    private final SSD1306 myOled;
    private final Pin ledPin;
    private final Pin secondLed;
    private final IODevice myArduino;
    private final Pin myBuzzer;
    private final Pin pumpPin;
    private double currentVal;

    TurbTask (Pin turbPin, Pin ledPin, IODevice myArduino, SSD1306 myOled, Pin myBuzzer, Pin pumpPin, Pin secondLed){
        this.ledPin = ledPin;
        this.turbPin = turbPin;
        this.myArduino = myArduino;
        this.myOled = myOled;
        this.myBuzzer = myBuzzer;
        this.pumpPin = pumpPin;
        this.secondLed = secondLed;
    }
    @Override
    public void run() {
        //Schedule a turbidity listener
        TurbListener myListener = new TurbListener(pumpPin,ledPin);
        myArduino.addEventListener(myListener);
        try {
            currentVal = this.turbPin.getValue();
            System.out.println(currentVal);
            this.myOled.getCanvas().clear();
            this.myOled.getCanvas().drawString(0, 0, "Turb: " + String.valueOf(currentVal));
            this.myOled.display();
            if (currentVal <= 450) {
                    this.ledPin.setValue(1);
                    this.secondLed.setValue(0);

            } else if (currentVal > 500) {

                    this.ledPin.setValue(0);
                    this.secondLed.setValue(1);
            }
        }
        catch (Exception e){
            System.out.println("error");
        }
    }
}
