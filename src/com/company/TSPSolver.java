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
        totalTraveledDistance = greedy(startingNode);
        size = route.size();
        tryAllStartingPoints();
        //improveSolution();
    }

    private int greedy(int startingNode){
        int node = startingNode;
        int backup = -1;
        int travelledDistance = 0;
        while(!isAllVisited()){
            route.add(node);
            visited[node] = 1;
            int tmp = findClosestNeighbor(node);
            if(tmp != -1) {
                travelledDistance += matrix[node][tmp];
            }
            backup = node;
            node = tmp;
        }
        route.add(startingNode);
        travelledDistance += matrix[startingNode][backup];
        computeRouteLength(route);
        //printRoute(route);
        return travelledDistance;
    }
    private void tryAllStartingPoints(){
        int minDist = totalTraveledDistance;
        int bestStartingNode = this.startingNode;
        int localDist = 0;
        for (int i = 0; i < length ; i++) {
            flushVisited();
            route.clear();
            localDist = greedy(i);
            if(localDist < minDist){
                minDist = localDist;
                bestStartingNode = i;
            }
        }
        flushVisited();
        route.clear();
        System.out.println("Best starting point is: "+ bestStartingNode + " min distance found is: " + minDist);
        this.totalTraveledDistance = greedy(bestStartingNode);
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
//    private void improveSolution(){
//        ArrayList<Integer> temp = route;
//        int index = 0;
//        //int limit = 500;
//        int offset = 1;
//        for (int i = 0; i < Math.pow(length, 1) ; i++) {
//            if(index+offset > length-1 || index+1 > length-1)
//                index=0;
//            int backup = temp.get(index);
//            temp.set(index, temp.get(index + offset)) ;
//            temp.set(index+1, backup);
//            temp.set(length, temp.get(0));
//            if (computeRouteLength(route) > computeRouteLength(temp)){
//                route = temp;
//                printRoute(route);
//                System.out.println("Route changed to distance: "+ computeRouteLength(route));
//                printRoute(temp);
//                System.out.println();
//            }
//            if (index+offset < route.size())
//                index++;
//            else {
//                index = 0;
//                offset++;
//            }
//        }
//
//    }
    private void improveSolution(){
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
            for (int j = 0; j < 1000 ; j++) {
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
                else if(routeLength-currentDistance > currentDistance/5){
                    System.out.println("swap back..."+ computeRouteLength(testRoute));
                    testRoute.set(rand1, backup);
                    testRoute.set(rand2, backup2);
                    System.out.println("swapped back..."+ computeRouteLength(testRoute));
                }
                else {
                    printRoute(testRoute);
                    System.out.println("didn't swap..."+ computeRouteLength(testRoute));
                }//System.out.println(routeLength);;
            }
        }
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
