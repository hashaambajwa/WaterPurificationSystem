import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;
import org.w3c.dom.html.HTMLImageElement;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class TurbListener implements IODeviceEventListener {
    private final Pin pumpPin;
    private final Pin ledPin;

    TurbListener (Pin pumpPin, Pin ledPin){
        this.pumpPin = pumpPin;
        this.ledPin = ledPin;

    }
    @Override
    public void onStart(IOEvent ioEvent) {

    }

    @Override
    public void onStop(IOEvent ioEvent) {

    }

    @Override
    public void onPinChange(IOEvent ioEvent) {
        if (ioEvent.getPin().getIndex() != this.ledPin.getIndex()){
            return;
        }
        //Check the LED condition either 1 or 0
        try {
            long currentVal = this.ledPin.getValue();
            if (currentVal == 1){
                //Create a GUI to see how long pump
                JFrame myFrame = new JFrame();
                myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                int length = Integer.parseInt( JOptionPane.showInputDialog(null, "Enter your size"));
                Thread.sleep(7000);
                System.out.println(length);
                long t = 0;

                //if conditions for the different size of the cup of water

                if (length == 1){
                    this.pumpPin.setValue(1);
                    Thread.sleep(3000);
                }
                else if (length == 2){
                    this.pumpPin.setValue(1);
                    Thread.sleep(5000);
                }

                else if (length == 3){
                    this.pumpPin.setValue(1);
                    Thread.sleep(9000);
                }

                this.pumpPin.setValue(0);
            }
            else if (currentVal == 0){
                this.pumpPin.setValue(0);
            }

        }
        catch (Exception e){
            System.out.println("Not working properly");
        }

    }
    @Override
    public void onMessageReceive(IOEvent ioEvent, String s) {

    }
}
