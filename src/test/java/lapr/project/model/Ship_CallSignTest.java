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
public class Ship_CallSignTest {
    
    /**
     * Test of the constructors, of class Client.
     */
    @Test
    void ShipControllers() throws ParseException {
        Ship_CallSign shipCallSign= new Ship_CallSign("TESTE", 99999, 266, 80, 9.5, "C4SQ2");
    }
    
    @Test
    void getCodigoTeste() throws ParseException {
        Ship_CallSign s = new Ship_CallSign("TESTE", 99999, 266, 80, 9.5, "C4SQ2");
        String expected = "C4SQ2";
        assertEquals(expected, s.getCode());
    }
    
    @Test
    void testCompareTo() {
    
    Ship_CallSign s1 = new Ship_CallSign("TESTE", 99999, 266, 80, 9.5, "C4SQ2");
    Ship_CallSign s2 = new Ship_CallSign("TESTE2", 99998, 276, 81, 9.6, "C4SQ3");
    
    Assertions.assertEquals(-1, s1.compareTo(s2), "Should s1 be smaller than s2");

    
    s1 = new Ship_CallSign("TESTE", 99999, 266, 80, 9.5, "C4SQ2");
    s2 = new Ship_CallSign("TESTE2", 99998, 276, 81, 9.6, "C4SQ2");
    // when: then:
    Assertions.assertEquals(0, s1.compareTo(s2), "Should s1 and s2 be equal");

    
    s1 = new Ship_CallSign("TESTE", 99999, 266, 80, 9.5, "C4SQ3");
    s2 = new Ship_CallSign("TESTE2", 99998, 276, 81, 9.6, "C4SQ2");
    
    Assertions.assertEquals(1, s1.compareTo(s2), "Should s1 be bigger than s2");
}
    
    
    
}
