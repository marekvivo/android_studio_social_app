package com.example.findr;

import java.util.ArrayList;
import java.util.Random;

public class Interest {
    //genreIdList -> [1,1,123] -> Entertainment, VideoGames, [Action,Adventure,Fantasy]
    private ArrayList<Integer> genreIdList = new ArrayList<>();
    //Name of interest
    private String interest;

    public Interest(){
        //Default constructor for firebase
    }

    public Interest(String random){
        Random r = new Random();
        int i = r.nextInt(999);
        genreIdList.add(i);
        i = r.nextInt(999);
        genreIdList.add(i);
        i = r.nextInt(999);
        genreIdList.add(i);

        this.interest = "RANDOM";
    }

    public Interest(ArrayList<Integer> genreIdList, String interest){
        this.genreIdList = genreIdList;
        this.interest = interest.toLowerCase();
    }

    public ArrayList<Integer> getGenreIdList() {
        return genreIdList;
    }

    public void setGenreIdList(ArrayList<Integer> genreIdList) {
        this.genreIdList = genreIdList;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest.toLowerCase();
    }

    public int toDatabaseGenreId(){
        String rawGenreIdList = genreIdList.toString().replaceAll(",\\s", "0");
        rawGenreIdList = rawGenreIdList.replace("[","");
        rawGenreIdList = rawGenreIdList.replace("]","");
        System.out.println(rawGenreIdList);

        return Integer.parseInt(rawGenreIdList);
    }

    public boolean equals (Interest i){
        if(!i.getGenreIdList().equals(this.genreIdList)){
            return false;
        }
        if(!i.getInterest().equalsIgnoreCase(this.interest)){
            return false;
        }
        return true;
    }


}
