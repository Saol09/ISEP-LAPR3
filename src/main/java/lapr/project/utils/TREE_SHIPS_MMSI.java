/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import lapr.project.model.Ship;
import lapr.project.model.ShipDynamicData;
import lapr.project.model.Ship_CallSign;
import lapr.project.model.Ship_MMSI;

/**
 * The type Tree ships mmsi.
 *
 * @author andre
 */
public class TREE_SHIPS_MMSI extends SUPERTREE<Ship_MMSI> {


    /**
     * Instantiates a new Tree ships mmsi.
     *
     * @param ship_mmsi the ship mmsi
     */
    public TREE_SHIPS_MMSI(Ship_MMSI ship_mmsi) {
        super(ship_mmsi);
    }

    /**
     * Instantiates a new Tree ships mmsi.
     *
     * @param ship_mmsi the ship mmsi
     * @param path      the path
     */
    public TREE_SHIPS_MMSI(Ship_MMSI ship_mmsi, String path){
        super(ship_mmsi, path);
    }



}

