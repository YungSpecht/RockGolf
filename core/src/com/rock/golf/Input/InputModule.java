package com.rock.golf.Input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class InputModule {
    public static double[] get_input(){
        double[] result = new double[9];
        FileReader reader;
        try {
            reader = new FileReader("core/src/com/rock/golf/Input/Input.txt");
            Scanner in = new Scanner(reader);
            in.nextLine();
            for(int i = 0; i < 9; i++){
                String inputLine = in.nextLine();
                result[i] = Double.parseDouble(inputLine.substring(inputLine.indexOf(' ')));
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return result;
    }

    public static void set_new_position(double xPos, double yPos){
        try {
            File file = new File("core/src/com/rock/golf/Input/Input.txt");
            List<String> lines = Files.readAllLines(file.toPath());
            lines.set(6, "ball-x-position: " + xPos);
            Files.write(file.toPath(), lines);
            lines.set(7, "ball-y-position: " + yPos);
            Files.write(file.toPath(), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
