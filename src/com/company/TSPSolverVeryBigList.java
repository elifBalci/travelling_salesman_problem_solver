package com.company;

import java.util.ArrayList;
import java.util.Random;

public class TSPSolverVeryBigList {
    private int[] visited;
    private ArrayList<Integer> route;
    private int totalTraveledDistance;
    private int size;
    private int startingNode = 0;
    private ArrayList<City> cityArrayList;

    public TSPSolverVeryBigList(){
        cityArrayList = City.getCityList();
        size = City.getCityList().size();
        route = new ArrayList<>();
        visited = new int[size];
        for (int i = 0; i < size ; i++) {
            visited[i] = 0;
        }
        this.totalTraveledDistance = 0;
        totalTraveledDistance = NN(startingNode);
        System.out.println("After nearest neighbor: " + totalTraveledDistance);
        size = route.size();
        //tryAllStartingPoints();
        System.out.println("After finding optimal starting point: " + totalTraveledDistance);
        //improveSolution();
        printRoute(route);
        twoOpt();
    }

    private int NN(int startingNode){
        int node = startingNode;
        int backup = -1;
        int travelledDistance = 0;
        while(!isAllVisited()){
            //if there is an unvisited node, add it to the route
            // and mark it as visited
            route.add(node);
            visited[node] = 1;
            int tmp = findClosestNeighbor(node);
            // findClosestNeighbor method will return -1 if there is no unvisited neighbor
            if(tmp != -1) {
                // add the distance to total distance
                travelledDistance += MatrixCreator.computeDistance(cityArrayList.get(node),cityArrayList.get(tmp));
            }
            backup = node;
            node = tmp;
        }
        // add starting node to the end of route
        route.add(startingNode);
        travelledDistance +=  MatrixCreator.computeDistance(cityArrayList.get(startingNode),cityArrayList.get(backup));
        computeRouteLength(route);
        //printRoute(route);
        return travelledDistance;
    }

    private boolean isAllVisited(){
        for (int i = 0; i < size; i++){
            if (visited[i] == 0)
                return false;
        }
        //printRoute(route);
        return true;
    }


    private int findClosestNeighbor(int nodeIndex){
        int closest = -1;
        int closestIndex = -1;

        for (int i = 0; i < size ; i++) {
            if(i != nodeIndex && visited[i] == 0){
                closestIndex = i;
                closest = MatrixCreator.computeDistance(cityArrayList.get(nodeIndex),cityArrayList.get(i));
                break;
            }
        }

        for (int i = 0; i < size ; i++) {
            if (i == nodeIndex)
                continue;     //closest can't be node itself
            if(closest >  MatrixCreator.computeDistance(cityArrayList.get(nodeIndex),cityArrayList.get(i)) && visited[i] == 0 ){;
                closestIndex = i;
                closest =MatrixCreator.computeDistance(cityArrayList.get(nodeIndex),cityArrayList.get(i));
            }
        }
        return closestIndex;
    }


    public void twoOpt() {
        ArrayList<Integer> newTour;
        int bestLength = totalTraveledDistance;
        int newLength;
        int swaps = 1;

        while (swaps != 0) { //until no improvements are being made.
            swaps = 0;
            for (int i = 1; i < route.size() - 2; i++) {
                for (int j = i + 1; j < route.size() - 1; j++) {
                    if ((MatrixCreator.computeDistance(cityArrayList.get(route.get(i)),cityArrayList.get(route.get(i-1))) +
                            MatrixCreator.computeDistance(cityArrayList.get(route.get(j+1)),cityArrayList.get(route.get(j))) >=
                            (MatrixCreator.computeDistance(cityArrayList.get(route.get(i)),cityArrayList.get(route.get(j+1))) +
                                    MatrixCreator.computeDistance(cityArrayList.get(route.get(i-1)),cityArrayList.get(route.get(j)))))){

                        newTour = swapCities(route, i, j);
                        newLength = computeRouteLength(newTour);
                        if (newLength < bestLength) {
                            route = newTour;
                            bestLength = newLength;
                            swaps++;
                        }
                    }
                }
            }
        }
        this.totalTraveledDistance = bestLength;
        printRoute(route);
        System.out.println(bestLength);
    }

    private ArrayList<Integer> swapCities(ArrayList<Integer> route, int i, int j) {
        ArrayList<Integer> newTour = new ArrayList<>();
        int size = this.size;
        for (int c = 0; c <= i - 1; c++) {
            newTour.add(route.get(c));
        }
        int count = 0;
        for (int c = i; c <= j; c++) {
            newTour.add(route.get(j - count));
            count++;
        }
        for (int c = j + 1; c < size; c++) {
            newTour.add(route.get(c));
        }
        return newTour;
    }

    private int computeRouteLength(ArrayList<Integer> temp){
        int dist = 0;
        for (int i = 0; i < temp.size()-1; i++) {
            dist = dist + MatrixCreator.computeDistance(cityArrayList.get(temp.get(i)),cityArrayList.get(temp.get(i+1)));
        }
        return dist;
    }

    public void printRoute(ArrayList<Integer> r){
        for (Integer i: r) {
            System.out.print(i+ " ");
        }
        System.out.println();
    }

    public ArrayList<Integer> getRoute() {
        return route;
    }

    public int getTotalTraveledDistance() {
        return totalTraveledDistance;
    }

}
