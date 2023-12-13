package lapr.project.controller;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SearchInformationControllerTest {

    @Test
    void printGraph() throws IOException {
        SearchInformationController SIC = new SearchInformationController();
        SIC.createGraph(2);
    }

    @Test
    void mapColour() throws IOException {
        SearchInformationController SIC = new SearchInformationController();
        SIC.createGraph(2);
        SIC.mapColour();
    }

    @Test
    void nClosestLocalsByContinent() throws IOException {
        SearchInformationController SIC = new SearchInformationController();
        SIC.createGraph(2);
        SIC.nClosestLocalsByContinent(2);
    }
}