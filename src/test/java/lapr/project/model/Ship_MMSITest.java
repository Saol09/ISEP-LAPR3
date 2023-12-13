/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.text.ParseException;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author andre
 */
public class Ship_MMSITest {
    
     /**
     * Test of the constructors, of class Client.
     */
    @Test
    void ShipControllers() throws ParseException {
        Ship_MMSI shipMMSI= new Ship_MMSI("TESTE", 99999, 266, 80, 9.5, "211111111");
    }
    
    @Test
    void getCodigoTeste() throws ParseException {
        Ship_MMSI s = new Ship_MMSI("TESTE", 99999, 266, 80, 9.5, "211111111");
        String expected = "211111111";
        assertEquals(expected, s.getCode());
    }
    
    @Test
    void testCompareTo() {
    
    Ship_MMSI s1 = new Ship_MMSI("TESTE", 99999, 266, 80, 9.5, "211111111");
    Ship_MMSI s2 = new Ship_MMSI("TESTE2", 99998, 276, 81, 9.6, "211111112");
    
    Assertions.assertEquals(-1, s1.compareTo(s2), "Should s1 be smaller than s2");

    
    s1 = new Ship_MMSI("TESTE", 99999, 266, 80, 9.5, "211111111");
    s2 = new Ship_MMSI("TESTE2", 99998, 276, 81, 9.6, "211111111");
    // when: then:
    Assertions.assertEquals(0, s1.compareTo(s2), "Should s1 and s2 be equal");

    
    s1 = new Ship_MMSI("TESTE", 99999, 266, 80, 9.5, "211111112");
    s2 = new Ship_MMSI("TESTE2", 99998, 276, 81, 9.6, "211111111");
    
    Assertions.assertEquals(1, s1.compareTo(s2), "Should s1 be bigger than s2");
}
    
    
    
}
