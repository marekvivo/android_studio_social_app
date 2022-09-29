package com.example.findr;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class GenreTest {

    @Test
    public void indexOfSubGenre() {
        Genre g = new Genre("default");
        ArrayList<String> strTest = new ArrayList<>();

        strTest.add("entertainment");
        strTest.add("video games");
        strTest.add("shooter");

        //assertEquals(g.getSubGenre(0));

        //assertEquals("[0, 0, 3]",g.indexOfSubGenre(strTest).toString());
        //assertEquals("[0, 0, 3]",g.indexOfSubGenre(strTest).toString());
    }

    @Test
    public void combineSubGenres() {
        Genre g = new Genre("default");

        ArrayList<String> strTest = new ArrayList<>();

        strTest.add("entertainment");
        strTest.add("video games");
        strTest.add("shooter");

        ArrayList<String> strTest1 = new ArrayList<>();

        strTest1.add("entertainment");
        strTest1.add("video games");
        strTest1.add("Action");


        //System.out.println(strTest.toString());
        ArrayList<Integer> testIntArr =   g.combineSubGenres(g.indexOfSubGenre(strTest),g.indexOfSubGenre(strTest1));

        //System.out.println(strTest.toString());
        //assertEquals("[entertainment, video games, shooter, ]",g.printParseGenre(g.indexOfSubGenre(strTest)));

        assertEquals("",g.printParseGenre(testIntArr));
        assertEquals("",g.printParseGenre(testIntArr));
    }


} 