package com.rock.golf.Input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import org.mariuszgromada.math.mxparser.Function;

public class InputModule {
    public static double[] get_input() {
        double[] result = new double[9];
        FileReader reader;
        try {
            reader = new FileReader("core/src/main/com/rock/golf/Input/Input.txt");
            Scanner in = new Scanner(reader);
            in.nextLine();
            for (int i = 0; i < 9; i++) {
                String inputLine = in.nextLine();
                result[i] = Double.parseDouble(inputLine.substring(inputLine.indexOf(' ')));
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void set_new_position(double xPos, double yPos) {
        try {
            File file = new File("core/src/main/com/rock/golf/Input/Input.txt");
            List<String> lines = Files.readAllLines(file.toPath());
            lines.set(6, "ball-x-position: " + xPos);
            Files.write(file.toPath(), lines);
            lines.set(7, "ball-y-position: " + yPos);
            Files.write(file.toPath(), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Function get_profile() {
        FileReader reader;
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current absolute path is: " + s);
        try {
            reader = new FileReader("C:/Users/liann/OneDrive/Documenten/GitHub/RockGolf/core/src/main/com/rock/golf/Input/Input.txt");
            // core/src/main/com/rock/golf/Input/Input.txt
            
            Scanner in = new Scanner(reader);
            String inputLine = in.nextLine();
            in.close();
            return new Function(inputLine.substring(inputLine.indexOf(' ')));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new Function(" ");
    }
}