package com.example.findr;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class ScoreTest {

    @Test
    public void addScoresTest() {
        Score user = new Score();
        ArrayList<Integer> testGenreIdList = new ArrayList<>();
        testGenreIdList.add(1);
        testGenreIdList.add(1);
        testGenreIdList.add(123);

        ArrayList<Integer> testGenreIdList1 = new ArrayList<>();
        testGenreIdList1.add(2);
        testGenreIdList1.add(2);
        testGenreIdList1.add(135);

        user.addScores(testGenreIdList);
        user.addScores(testGenreIdList1);

        user.printIdMap();

        assertEquals(user.getSubScore().get(0).getGenreId(),1);
        assertEquals(user.getSubScore().get(1).getGenreId(),2);

        Score user1 = user.getSubScore().get(0);

        assertEquals(1,user1.getGenreId());
        user1.printIdMap();

        user1 = user.getSubScore().get(1);
        assertEquals(2,user1.getGenreId());
        user1.printIdMap();

        Score userSub1 = user.getSubScore().get(0).getSubScore().get(0);

        userSub1.printIdMap();

        assertEquals(userSub1.getSubScore().get(0).getGenreId(),1);
        assertEquals(userSub1.getSubScore().get(1).getGenreId(),2);
        assertEquals(userSub1.getSubScore().get(2).getGenreId(),3);

        Score userSub2 = user.getSubScore().get(1).getSubScore().get(0);
        userSub2.printIdMap();

        assertEquals(userSub2.getSubScore().get(0).getGenreId(),1);
        assertEquals(userSub2.getSubScore().get(1).getGenreId(),3);
        assertEquals(userSub2.getSubScore().get(2).getGenreId(),5);


        assertEquals(24,user.calculateTotalScore(user));

        Score user2 = new Score();
        user2.addScores(testGenreIdList);

        //assertEquals(50,user.calculateScore(user,user2));

        assertEquals((double)50,user.percentageMatched(user2),0.0001);




    }
}