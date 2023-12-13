package lapr.project.model;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PortTest {

    final Port port1 = new Port("europa", "portugal", "123",
            "Porto", 140.52, -14.2);

    @Test
    // testing default const
    public void testPort() {
        Port c1 = new Port();
        assertTrue(true);
    }

    @Test
    public void getContinent() {
        assertEquals("europa", port1.getContinent());
    }

    @Test
    public void getCountry() {
        assertEquals("portugal", port1.getCountry());
    }

    @Test
    public void getCode() {
        assertEquals("123", port1.getCode());
    }

    @Test
    public void getPort() {
        assertEquals("Porto", port1.getPort());
    }

    @Test
    public void testCompareTo() {
        // given:
        Port leixoes = new Port("Europe", "Portugal", "12450", "Leixões", 40.25,20.15);
        Port liverpool = new Port("Europe", "United Kingdom", "12550", "Liverpool", 15.10,52.10);
        // when: then:
        Assertions.assertEquals(-1, leixoes.compareTo(liverpool), "Leixoes is different liverpool");

        // given:
        leixoes = new Port("Europe", "Portugal", "12450", "Leixões", 40.25,20.15);
        liverpool = new Port("Europe", "United Kingdom", "12450", "Liverpool", 15.10,52.10);
        // when: then:
        Assertions.assertEquals(0, leixoes.compareTo(liverpool), "leixoes is the same as liverpool");

        // given:
        leixoes = new Port("Europe", "Portugal", "12450", "Leixões", 40.25,20.15);
        liverpool = new Port("Europe", "United Kingdom", "12400", "Liverpool", 15.10,52.10);
        // when: then:
        Assertions.assertEquals(5, leixoes.compareTo(liverpool), "leixoes is bigger than liverpool");
    }
}