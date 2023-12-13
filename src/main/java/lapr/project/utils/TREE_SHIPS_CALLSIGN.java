/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import lapr.project.model.*;

/**
 * The type Tree ships callsign.
 *
 * @author andre
 */
public class TREE_SHIPS_CALLSIGN extends SUPERTREE<Ship_CallSign>{


    /**
     * Instantiates a new Tree ships callsign.
     *
     * @param ship_callSign the ship call sign
     */
    public TREE_SHIPS_CALLSIGN(Ship_CallSign ship_callSign) {
        super(ship_callSign);
    }

    /**
     * Instantiates a new Tree ships callsign.
     *
     * @param ship_callSign the ship call sign
     * @param path          the path
     */
    public TREE_SHIPS_CALLSIGN(Ship_CallSign ship_callSign, String path){
        super(ship_callSign, path);
    }
}
