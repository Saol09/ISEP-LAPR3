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
public class Ship_IMOTest {
    
    /**
     * Test of the constructors, of class Client.
     */
    @Test
    void ShipControllers() throws ParseException {
        Ship_IMO shipIMO= new Ship_IMO("TESTE", 99999, 266, 80, 9.5, "IMO5555555");
    }
    
    @Test
    void getCodigoTeste() throws ParseException {
        Ship_IMO s = new Ship_IMO("TESTE", 99999, 266, 80, 9.5, "IMO5555555");
        String expected = "IMO5555555";
        assertEquals(expected, s.getCode());
    }

    @Test
    void getCodigoTeste1() throws ParseException {
        Ship_IMO s = new Ship_IMO("TESTE1", 99999, 266, 80, 9.5, "IMO6666666");
        String expected = "IMO6666666";
        assertEquals(expected, s.getCode());
    }
    
    @Test
    void testCompareTo() {
    
    Ship_IMO s1 = new Ship_IMO("TESTE", 99999, 266, 80, 9.5, "IMO5555555");
    Ship_IMO s2 = new Ship_IMO("TESTE2", 99998, 276, 81, 9.6, "IMO5555556");
    
    Assertions.assertEquals(-1, s1.compareTo(s2), "Should s1 be smaller than s2");

    
    s1 = new Ship_IMO("TESTE", 99999, 266, 80, 9.5, "IMO5555555");
    s2 = new Ship_IMO("TESTE2", 99998, 276, 81, 9.6, "IMO5555555");
    // when: then:
    Assertions.assertEquals(0, s1.compareTo(s2), "Should s1 and s2 be equal");

    
    s1 = new Ship_IMO("TESTE", 99999, 266, 80, 9.5, "IMO5555556");
    s2 = new Ship_IMO("TESTE2", 99998, 276, 81, 9.6, "IMO5555555");
    
    Assertions.assertEquals(1, s1.compareTo(s2), "Should s1 be bigger than s2");
}
    
    
    
}
