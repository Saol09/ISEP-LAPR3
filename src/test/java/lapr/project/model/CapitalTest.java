package lapr.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class CapitalTest {

    @Test
    void getName() {
        Capital c = new Capital("Lisbon",38.71666667,-9.133333,"Europe");
        assertEquals("Lisbon", c.getName());
    }

    @Test
    void getLat() {
        Capital c = new Capital("Lisbon",38.71666667,-9.133333,"Europe");
        assertEquals("38.71666667", c.getLat().toString());
    }

    @Test
    void getLon() {
        Capital c = new Capital("Lisbon",38.71666667,-9.133333,"Europe");
        assertEquals("-9.133333", c.getLon().toString());
    }

    @Test
    void getContinent() {
        Capital c = new Capital("Lisbon",38.71666667,-9.133333,"Europe");
        assertEquals("Europe", c.getContinent());
    }

    @Test
    void testEqualsIguais() {
        Capital c = new Capital("Kosovo",42.66666667,21.166667,"Europe");
        Capital c1 = new Capital("Kosovo",42.66666667,21.166667,"Europe");
        assertEquals(true,c.equals(c1));
    }

    @Test
    void testEqualsDiferentes() {
        Capital c = new Capital("Lisbon",38.71666667,-9.133333,"Europe");
        Capital c1 = new Capital("Kosovo",42.66666667,21.166667,"Europe");
        assertEquals(false,c.equals(c1));
    }

    @Test
    void testHashCode() {
        Capital c = new Capital("Kosovo",42.66666667,21.166667,"Europe");
        assertEquals(-1478463190,c.hashCode());
    }

}