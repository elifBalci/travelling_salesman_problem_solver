package com.company;

import java.util.ArrayList;
import java.util.Random;

public class TSPSolver {

    private int[][] matrix;
    private int[] visited;
    private ArrayList<Integer> route;
    private int totalTraveledDistance;
    private int length;
    private int size;
    private int startingNode = 0;

    public TSPSolver(int[][] matrix){
        route = new ArrayList<>();
        this.matrix = matrix;
        this.length = matrix.length;
        visited = new int[length];
        for (int i = 0; i < length ; i++) {
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
                travelledDistance += matrix[node][tmp];
            }
            backup = node;
            node = tmp;
        }
        // add starting node to the end of route
        route.add(startingNode);
        travelledDistance += matrix[startingNode][backup];
        computeRouteLength(route);
        //printRoute(route);
        return travelledDistance;
    }

    private void tryAllStartingPoints(){
        // This function tries NN with all possible starting points in order to find the best.
        int minDist = totalTraveledDistance;
        int bestStartingNode = this.startingNode;
        int localDist = 0;
        for (int i = 0; i < length ; i++) {
            //Route and visited lists should be clear before calling NN function
            flushVisited();
            route.clear();
            localDist = NN(i);
            if(localDist < minDist){
                minDist = localDist;
                bestStartingNode = i;
            }
        }
        flushVisited();
        route.clear();
        System.out.println("Best starting point is: "+ bestStartingNode + " min distance found is: " + minDist);
        this.totalTraveledDistance = NN(bestStartingNode);
        printRoute(route);
    }
    private boolean isAllVisited(){
        for (int i = 0; i < length; i++){
            if (visited[i] == 0)
                return false;
        }
        //printRoute(route);
        return true;
    }

    private void flushVisited(){
        for (int i = 0; i < length; i++){
            visited[i] = 0;
        }
    }

    private int findClosestNeighbor(int nodeIndex){
        int closest = -1;
        int closestIndex = -1;

        for (int i = 0; i < length ; i++) {
            if(i != nodeIndex && visited[i] == 0){
                closestIndex = i;
                closest = matrix[nodeIndex][i];
                break;
            }
        }

        for (int i = 0; i < length ; i++) {
            if (i == nodeIndex)
                continue;     //closest can't be node itself
            if(closest > matrix[nodeIndex][i] && visited[i] == 0 ){
                closestIndex = i;
                closest = matrix[nodeIndex][i];
            }
        }
        return closestIndex;
    }

    private void randomSwap(){
        // This function randomly swaps 2 cities to make any possible improvement.
        // But it is not effective.
        ArrayList<Integer> currentRoute = route;
        ArrayList<Integer> testRoute = route;
        int currentDistance = totalTraveledDistance;
        Random r = new Random();
        int low = 0;
        int high = size-1;
        int rand1 = 0;
        int rand2 = 0;
        int routeLength = 0;
        for (int i = 0; i < size*2 ; i++) {
            for (int j = 0; j < 75 ; j++) {
                rand1 = r.nextInt(high - low) + low;
                rand2 = r.nextInt(high - low) + low;
                route.remove(size-1);
                int backup = testRoute.get(rand1);
                int backup2 = testRoute.get(rand2);
                testRoute.set(rand1, testRoute.get(rand2));
                testRoute.set(rand2, backup);
                testRoute.add(testRoute.get(0));
                routeLength = computeRouteLength(testRoute);
                if (routeLength< currentDistance) {
                    System.out.println(routeLength);
                    printRoute(testRoute);
                    System.out.println();
                }
                if (routeLength< this.totalTraveledDistance) {
                    System.out.println(routeLength);
                    printRoute(testRoute);
                    System.out.println();
                }
                //If distance is increased in a certain rate, swap back.
                else if(routeLength-currentDistance > currentDistance/5){
                    System.out.println("swap back..."+ computeRouteLength(testRoute));
                    testRoute.set(rand1, backup);
                    testRoute.set(rand2, backup2);
                    System.out.println("swapped back..."+ computeRouteLength(testRoute));
                }
                else {
                    printRoute(testRoute);
                    System.out.println("didn't swap..."+ computeRouteLength(testRoute));
                }
            }
        }
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
                    if ((matrix[route.get(i)][route.get(i-1)] + matrix[route.get(j+1)][route.get(j)]) >=
                            (matrix[route.get(i)][route.get(j+1)] + matrix[route.get(i-1)][route.get(j)])) {
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
            dist = dist + matrix[temp.get(i)][temp.get(i+1)];
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
