package lapr.project.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CountryTest {

    final Country country1 = new Country("Europe", "MT", "MLT", "Malta",
            0.44, "Valletta",140.52, -14.2);

    @Test
    // testing default const
    public void testCountry() {
        Country c1 = new Country();
        assertTrue(true);
    }

    @Test
    public void getPopulation(){
        assertEquals("0.44", country1.getPopulation().toString());
    }

    @Test
    public void getContinent() {
        assertEquals("Europe", country1.getContinent());
    }

    @Test
    public void getAlpha2() {
        assertEquals("MT", country1.getAlpha2());
    }

    @Test
    public void getAlpha3() {
        assertEquals("MLT", country1.getAlpha3());
    }

    @Test
    public void getCountry() {
        assertEquals("Malta", country1.getCountry());
    }

    /*
    @Test
    public void getCapital() {
        assertEquals("Valletta", country1.getCapital());
    }

     */

    @Test
    public void testToString()
    {
        Country portugal = new Country();
        String expected = "Country{continent='null', alpha2='null', alpha3='null', country='null', population=null, " +
                "capital='null'}";
        Assert.assertEquals(expected, portugal.toString());
    }

    @Test
    public void testHashCode() {
        Country c = new Country("europe","CY","CYP","Cyprus",0.85,
                "Nicosia",35.16666667,33.366667);
        assertEquals(2033349077,c.hashCode());
    }

}
