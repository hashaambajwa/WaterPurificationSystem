import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.stream.Collectors;

import org.firmata4j.*;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException{
        //1. Test the turbidity of water             TimerTask
        //If water is good sound buzzer
        //If water is bad light up LED            Set value to 1 in TimerTask
        //Show a jchart turbidity graph
        //2. If water is turbid, call an event listener, pump the water through a filter system to filter out the water
        //On pin change, LED Pin change
        //3. When water is in good condition, check the temperature of water and display the condition of water on the LED screen
        //4. If water is too warm, turn on the fan
        //Use a Java button to change volt level

        //Initialize the arduino board
        IODevice myArduino = new FirmataDevice("COM4");

        myArduino.start();
        myArduino.ensureInitializationIsDone();

        //Set up pins
        Pin turbPin = myArduino.getPin(PinDefinitions.turbPin);
        Pin buzzerPin = myArduino.getPin(8);
        buzzerPin.setMode(Pin.Mode.OUTPUT);
        buzzerPin.setValue(1);
        Pin pumpPin = myArduino.getPin(PinDefinitions.pumpPin);

        Pin ledPin = myArduino.getPin(PinDefinitions.ledPin);
        ledPin.setMode(Pin.Mode.OUTPUT);

        Pin secondLed = myArduino.getPin(8);
        secondLed.setMode(Pin.Mode.OUTPUT);

        Pin buttonPin = myArduino.getPin(PinDefinitions.buttonPin);
        buttonPin.setMode(Pin.Mode.INPUT);

        I2CDevice myI2C = myArduino.getI2CDevice((byte) 0x3c);
        SSD1306 myOled = new SSD1306(myI2C, SSD1306.Size.SSD1306_128_64);
        myOled.init();

        //Parse data from the temperature file
        File file = new File("TempData.txt");
        Scanner myScanner = new Scanner(file);
        int i = 0;
        ArrayList<Double> tempData = new ArrayList<Double>();
        while (myScanner.hasNextLine()){
            String myLine = myScanner.nextLine();
            tempData.add(Double.parseDouble(myLine));
            i++;
        }

        //Java Streams implementation
        double aveTemp = tempData.stream().collect(Collectors.summingDouble(Double::doubleValue))/i;

        //Display temp on OLED
        myOled.getCanvas().setTextsize(2);
        myOled.getCanvas().drawString(0, 0, String.valueOf(aveTemp));
        myOled.display();
        Thread.sleep(5000);

        //Either prompt user to fan or proceed

        if (aveTemp > 27) {
            //Prompt the user to place the drink near the fan
            //Print this on the OLED Display
            myOled.clear();
            myOled.getCanvas().setTextsize(2);
            myOled.getCanvas().drawString(0, 0, "USE FAN");
            myOled.display();
            Thread.sleep(10000);
        }

        else {
            //Print on the oled that water is good to go
            myOled.clear();
            myOled.getCanvas().drawString(0,0,"GOOD");
            myOled.getCanvas().drawString(20,20,"TO GO");
            myOled.display();
        }

        //Now schedule a task to check the turbidity of the water and have it display on the OLED
        Timer myTimer = new Timer();
        TurbTask myTask = new TurbTask(turbPin, ledPin, myArduino, myOled, buzzerPin, pumpPin, secondLed);
        myTimer.scheduleAtFixedRate(myTask,0,2000);

    }
}
