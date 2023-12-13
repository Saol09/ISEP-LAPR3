/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.TreeMap;

import lapr.project.model.Ship;
import lapr.project.model.ShipDynamicData;
import lapr.project.model.Ship_IMO;

/**
 * The type Tree ships imo.
 *
 * @author andre
 */
public class TREE_SHIPS_IMO extends SUPERTREE<Ship_IMO> {


    /**
     * Instantiates a new Tree ships imo.
     *
     * @param ship_imo the ship imo
     */
    public TREE_SHIPS_IMO(Ship_IMO ship_imo) {
        super(ship_imo);
    }

    /**
     * Instantiates a new Tree ships imo.
     *
     * @param ship_imo the ship imo
     * @param path     the path
     */
    public TREE_SHIPS_IMO(Ship_IMO ship_imo,String path) {
        super(ship_imo, path);
    }


}
