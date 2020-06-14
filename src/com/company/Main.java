package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String args[]) {


        String fileName;
        //fileName = args[0];
        fileName = "example-input-1.txt";
        InputHandler inputHandler = new InputHandler(fileName);
        for (int i = 0; i < inputHandler.getRowList().size(); i++) {
            new City(inputHandler.getRowList().get(i));
        }
        MatrixCreator matrixCreator = new MatrixCreator();
        TSPSolver tspSolver = new TSPSolver(MatrixCreator.getM());
        ArrayList<Integer> route = tspSolver.getRoute();

        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter("out.txt");
            myWriter.write(String.valueOf(tspSolver.getTraveledDistance()));
            for (Integer i: route ) {
                myWriter.write('\n');
                myWriter.write(String.valueOf(i));
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
