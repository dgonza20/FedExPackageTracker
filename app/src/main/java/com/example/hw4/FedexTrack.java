package com.example.hw4;
import android.database.sqlite.SQLiteDatabase;

import java.util.*;

public class FedexTrack {

    private Integer track_id;
    public Integer getTrack_id(){ return track_id;}

    // Return string names of cities for int codes
    public String getNames(Integer cityInt) {
        String returnString = "Wrong Value";

        switch (cityInt) {
            case 0:
                returnString = "Northborough, MA";
                break;

            case 1:
                returnString = "Edison, NJ";
                break;

            case 2:
                returnString = "Pittsburgh, PA";
                break;

            case 3:
                returnString = "Allentown, PA";
                break;

            case 4:
                returnString = "Martinsburg, WV";
                break;

            case 5:
                returnString = "Charlotte, NC";
                break;

            case 6:
                returnString = "Atlanta, GA";
                break;

            case 7:
                returnString = "Orlando, FL";
                break;

            case 8:
                returnString = "Memphis, TN";
                break;

            case 9:
                returnString = "Grove City, OH";
                break;

            case 10:
                returnString = "Indianapolis, IN";
                break;

            case 11:
                returnString = "Detroit, MI";
                break;

            case 12:
                returnString = "New Berlin, WI";
                break;

            case 13:
                returnString = "Minneapolis, MN";
                break;

            case 14:
                returnString = "St. Louis, MO";
                break;

            case 15:
                returnString = "Kansas, KS";
                break;

            case 16:
                returnString = "Dallas, TX";
                break;

            case 17:
                returnString = "Houston, TX";
                break;

            case 18:
                returnString = "Denver, CO";
                break;

            case 19:
                returnString = "Salt Lake City, UT";
                break;

            case 20:
                returnString = "Phoenix, AZ";
                break;

            case 21:
                returnString = "Los Angeles, CA";
                break;

            case 22:
                returnString = "Chino, CA";
                break;

            case 23:
                returnString = "Sacramento, CA";
                break;

            case 24:
                returnString = "Seattle, WA";
                break;

            default:
                break;

        }

        return returnString;
    }

    // Adjacency matrix of the cities
    public static int[][] graph = new int[][]{
            //1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5

            {0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//0

            {1,0,1,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//1

            {1,1,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},//2

            {1,1,0,0,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//3

            {0,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//4

            {0,0,0,1,1,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//5

            {0,0,0,0,0,1,0,1,1,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0},//6

            {0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//7

            {0,0,0,0,0,0,1,0,0,1,1,0,0,0,1,0,0,1,0,0,0,0,0,0,0},//8

            {0,1,0,0,1,1,0,0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},//9

            {0,0,0,0,0,0,1,0,1,1,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0},//10

            {0,0,1,0,0,0,0,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0},//11

            {0,0,0,0,0,0,0,0,0,0,1,1,0,1,1,1,0,0,0,0,0,0,0,0,0},//12

            {0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,1,0,0,0,0,0,0},//13

            {0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,1,1,0,0,0,0,0,0,0,0},//14

            {0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,1,0,1,0,0,0,0,0,0},//15

            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1,1,0,0,0,0,0,0},//16

            {0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0},//17

            {0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1,0,0,1,1,1,0,0,0},//18

            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,1,1},//19

            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1,0,0,0},//20

            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0,0},//21

            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,1,0},//22

            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1,0,1},//23

            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0}};//24



    // This function uses BFS algorithm to find the cities from the adjacency list
    public Vector<Integer> cityList(int Source, int Dest) {

        boolean[] visited = new boolean[25];
        Vector<Integer> returnVect = new Vector<Integer>();
        Integer[] cities = new Integer[25];
        Arrays.fill(cities, -1);
        Queue<Integer> theQueue = new LinkedList<>();

        theQueue.add(Source);
        visited[Source] = true;

        // while list not empty
        while(!theQueue.isEmpty()) {

            // Pop front of the queue
            int theFront = theQueue.poll();

            // Check for adjacencies in that node
            for(int i = 0; i < 25; i++) {

                if(graph[theFront][i] == 1) {

                    if(visited[i] == false) {
                        visited[i] = true;
                        cities[i] = theFront;

                        if(i == Dest) break;

                        theQueue.add(i);

                    }
                }
            }
        }


        int currentLoc = Dest;
        returnVect.add(Dest);
        while(currentLoc != Source) {
            //System.out.print(cities[currentLoc] + " ");
            currentLoc = cities[currentLoc];
            returnVect.add(currentLoc);
        }
        System.out.println();

        Collections.reverse(returnVect);
        return returnVect;
    }

    // Insert the remaining package tracking into the DB
    public boolean InsertPackageRemaining(DatabaseHelper myDB, Integer tracking, Integer SourceInt, Integer DestInt){

        if(myDB == null) return false;
        if(tracking == null) return false;
        if(SourceInt == null) return false;
        if(DestInt == null) return false;
        Vector<Integer> LocationList = this.cityList(SourceInt, DestInt);
        boolean Inserted = false;
        track_id = tracking;

        // Start at the location after source. That is why i start at 1
        for(int i = 1; i < LocationList.size(); i++){
            if(i == LocationList.size()-1)
                Inserted = myDB.insertData(track_id, SourceInt, DestInt, LocationList.elementAt(i), 1);
            else
                Inserted = myDB.insertData(track_id, SourceInt, DestInt, LocationList.elementAt(i), 0);
        }

        return Inserted;
    }

    // Insert the starting location row into the DB
    public boolean insertPackageStart(DatabaseHelper myDB, Integer SourceInt, Integer DestInt){

        Random r = new Random();
        track_id = r.nextInt(9999 - 1000) + 1000;

        Vector<Integer> LocationList = this.cityList(SourceInt, DestInt);
        if(SourceInt == DestInt)
            return myDB.insertData(track_id, SourceInt, DestInt, LocationList.elementAt(0), 1);
        return myDB.insertData(track_id, SourceInt, DestInt, LocationList.elementAt(0), 0);
    }

}
