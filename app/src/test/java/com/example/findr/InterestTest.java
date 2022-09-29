package com.example.findr;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class InterestTest {

    @Test
    public void getGenre() {
    }

    @Test
    public void setGenre() {
    }

    @Test
    public void getGenreId() {
    }

    @Test
    public void setGenreId() {
    }

    @Test
    public void getSubGenreArrayList() {
    }

    @Test
    public void setSubGenreArrayList() {
    }

    @Test
    public void getSubInterestsArrayList() {
    }

    @Test
    public void setSubInterestsArrayList() {
    }

    @Test
    public void addSubInterest() {

    }

    @Test
    public void containsSubInterestString() {
    }

    /*@Test
    public void indexOfSubGenre() {
        Genre i = new Genre();
        Interest entertainment = new Interest("Entertainment",1);
        i.getSubGenreArrayList().add(entertainment);

        entertainment.addNewSubGenre("Video Games");
        entertainment.addNewSubGenre("Movies");
        entertainment.addNewSubGenre("Sports");

        Interest videoGame = entertainment.getSubGenreArrayList().get(0);
        videoGame.addNewSubGenre("Action");
        videoGame.addNewSubGenre("Adventure");
        videoGame.addNewSubGenre("Fantasy");

        Interest gameFantasy = videoGame.getSubGenreArrayList().get(2);
        //gameFantasy.addNewSubInterest("Zelda Breath of the Wild");

        String testStr = "";
        for(Interest tInt: entertainment.getSubGenreArrayList()){
            testStr+= tInt.getGenreId();
        }

        //String[] strList = {"MAIN","Video Games","Adventure"};
        ArrayList<String> strArr = new ArrayList<>();
        strArr.add("entertainment");
        strArr.add("video games");
        strArr.add("adventure");

        ArrayList<Integer>  callback = i.indexOfSubGenre(strArr);

        assertEquals(videoGame.getSubGenreArrayList().get(1),i.getTargetSubgenre(callback));

        //assertEquals("00",callback.toString());

        assertEquals(3,i.getSubGenreArrayList().get(0).getSubGenreArrayList().size());
        assertEquals("fantasy", i.getSubGenreArrayList().get(0).getSubGenreArrayList().get(0).getSubGenreArrayList().get(2).getGenre());
        assertEquals(1,gameFantasy.getSubInterestsArrayList().size());
        assertEquals("zelda breath of the wild",i.getSubGenreArrayList().get(0).getSubGenreArrayList().get(0).getSubGenreArrayList().get(2).getSubInterestsArrayList().get(0).getSubInterest());

    }


    @Test
    public void getNextInterest() {
        Interest i = new Interest();
        Interest entertainment = new Interest("Entertainment",1);
        i.getSubGenreArrayList().add(entertainment);

        entertainment.addNewSubGenre("Video Games");
        entertainment.addNewSubGenre("Movies");
        entertainment.addNewSubGenre("Sports");

        Interest videoGame = entertainment.getSubGenreArrayList().get(0);
        videoGame.addNewSubGenre("Action");
        videoGame.addNewSubGenre("Adventure");
        videoGame.addNewSubGenre("Fantasy");

        assertEquals(videoGame,i.getNextSubGenre(entertainment.getSubGenreArrayList(),"video games"));
    }*/

    @Test
    public void toDbId() {
        ArrayList<Integer> inputInt = new ArrayList<>();
        inputInt.add(1);
        inputInt.add(1);
        inputInt.add(123);

        Interest interest = new Interest(inputInt,"Test");
        //assertEquals(101012300, interest.toDatabaseGenreId(inputInt));

    }
}