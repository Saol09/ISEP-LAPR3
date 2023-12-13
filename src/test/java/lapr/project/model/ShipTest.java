/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andre
 */
public class ShipTest {

    final Ship ship1 = new Ship("barco", 12345, 12.25, 20.00, 2.00)
    {
        @Override
        public int compareTo(Ship o) {
            return 0;
        }
    };


    @Test
    public void getNameShip(){
        assertEquals("barco", ship1.getName());
    }

    @Test
    public void getCodeShip(){
        assertEquals(12345, ship1.getVesselTypeCode());
    }




    }


