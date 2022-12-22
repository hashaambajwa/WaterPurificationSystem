import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class myFrame {
    public static void main(String[] args) throws InterruptedException, IOException {
        File file = new File("TempData.txt");
        Scanner myScanner = new Scanner(file);
        int i = 0;
        double sum = 0;
        ArrayList<Double> tempData = new ArrayList<Double>();
        while (myScanner.hasNextLine()){
            String myLine = myScanner.nextLine();
            sum += Double.parseDouble(myLine);
            tempData.add(Double.parseDouble(myLine));
            i++;
        }
        double aveTemp = tempData.stream().collect(Collectors.summingDouble(Double::doubleValue))/i;
        System.out.println(aveTemp);

        //double aveTemp = sum/(double)i;

    }

}
