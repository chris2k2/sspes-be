package de.cweyermann.ssps.backend.logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class CalcRankingTest {


    @Test
    public void testRegex() {
        String result = "\"spoCk\"";
        String s = result.toLowerCase().replaceAll("[^a-z]", "");

        assertEquals(s, "spock");
    }
}