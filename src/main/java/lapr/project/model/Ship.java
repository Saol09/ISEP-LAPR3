/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.time.LocalDateTime;

import lapr.project.utils.TREE_DynamicData;

/**
 * The type Ship.
 *
 * @author andre
 */
public abstract class Ship implements Comparable<Ship> {


    //dados estáticos
    private String name;
    private String code;
    private TREE_DynamicData dynamicDataTree;       //arvore com dados dinamicos
    private int vesselTypeCode; //pode ser necessario alterar para uma classe
    private double lenght;
    private double width;
    private double draft;


    /**
     * Instantiates a new Ship.
     *
     * @param name           the name
     * @param vesselTypeCode the vessel type code
     * @param length         the length
     * @param width          the width
     * @param draft          the draft
     */
    public Ship(String name, int vesselTypeCode, double length, double width, double draft) {
        this.name = name;
        this.vesselTypeCode = vesselTypeCode;
        this.lenght = length;
        this.width = width;
        this.draft = draft;
        this.dynamicDataTree = new TREE_DynamicData();
    }

    /**
     * Empty Ship constructor
     */
    public Ship() {

    }

    @Override
    public abstract int compareTo(Ship o);


    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets inicio partida date.
     *
     * @return the inicio partida date
     */
    public LocalDateTime getInicioPartidaDate() {
        return dynamicDataTree.getInicioPartidaDate();
    }

    /**
     * Gets fim partida date.
     *
     * @return the fim partida date
     */
    public LocalDateTime getFimPartidaDate() {
        return dynamicDataTree.getFimPartida();
    }

    /**
     * Gets numero total movimentos.
     *
     * @return the numero total movimentos
     */
    public Integer getNumeroTotalMovimentos() {
        return dynamicDataTree.getNumeroTotalMovimentos();
    }

//    public Set<ShipDynamicData> getDynamicDataList() {
//        return dynamicDataList;
//    }

    /**
     * Gets dynamic data tree.
     *
     * @return the dynamic data tree
     */
    public TREE_DynamicData getDynamicDataTree() {
        return dynamicDataTree;
    }


    /**
     * Add to dynamic data tree.
     *
     * @param shipDynamicData the ship dynamic data
     */
    public void addToDynamicDataTree(ShipDynamicData shipDynamicData) {
        this.dynamicDataTree.insert(shipDynamicData);
    }

    /**
     * Gets travelled distance.
     *
     * @return the travelled distance
     */
    public double getTravelledDistance() {
        return this.dynamicDataTree.getTravelledDistance();
    }

    /**
     * Gets delta distance.
     *
     * @return the delta distance
     */
    public double getDeltaDistance() {
        return this.dynamicDataTree.getDeltaDistance();
    }

    /**
     * Gets vessel type code.
     *
     * @return the vessel type code
     */
    public int getVesselTypeCode() {
        return vesselTypeCode;
    }

    /**
     * Gets mean sog between dates.
     *
     * @param dataInicial the data inicial
     * @param dataFinal   the data final
     * @return the mean sog between dates
     */
    public double getMeanSogBetweenDates(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return this.dynamicDataTree.getMeanSogBetweenDates(dataInicial, dataFinal);
    }

    /**
     * Gets max cog.
     *
     * @return the max cog
     */
    public double getMaxCog() {
        return this.dynamicDataTree.getMaxCog();
    }

    /**
     * Gets max sog.
     *
     * @return the max sog
     */
    public double getMaxSog() {
        return this.dynamicDataTree.getMaxSog();
    }

    /**
     * Gets mean cog.
     *
     * @return the mean cog
     */
    public double getMeanCog() {
        return this.dynamicDataTree.getMeanCog();
    }

    /**
     * Gets mean sog.
     *
     * @return the mean sog
     */
    public double getMeanSog() {
        return this.dynamicDataTree.getMeanSog();
    }

    /**
     * Gets departure latitude.
     *
     * @return the departure latitude
     */
    public double getDepartureLatitude() {
        return this.dynamicDataTree.getDepartureLatitude();
    }

    /**
     * Gets arrival latitude.
     *
     * @return the arrival latitude
     */
    public double getArrivalLatitude() {
        return this.dynamicDataTree.getArrivalLatitude();
    }

    /**
     * Gets departure longitude.
     *
     * @return the departure longitude
     */
    public double getDepartureLongitude() {
        return this.dynamicDataTree.getDepartureLongitude();
    }

    /**
     * Gets arrival longitude.
     *
     * @return the arrival longitude
     */
    public double getArrivalLongitude() {
        return this.dynamicDataTree.getArrivalLongitude();
    }

    /**
     * Get travelled distance between dates double.
     *
     * @param dataInicial the data inicial
     * @param dataFinal   the data final
     * @return the double
     */
    public double getTravelledDistanceBetweenDates(LocalDateTime dataInicial, LocalDateTime dataFinal){ return  this.dynamicDataTree.getTravelledDistanceBetweenDates(dataInicial, dataFinal);}

    /**
     * Has two movements between dates boolean.
     *
     * @param dataInicial the data inicial
     * @param dataFinal   the data final
     * @return the boolean
     */
    public boolean hasTwoMovementsBetweenDates(LocalDateTime dataInicial, LocalDateTime dataFinal){ return this.getDynamicDataTree().hasTwoMovementsBetweenDates(dataInicial, dataFinal);}

    @Override
    public String toString() {
        return ", \n\tname='" + name + '\'' +
                //", \n\tdynamicDataList=" + dynamicDataTree +
                ", \n\tvesselTypeCode=" + vesselTypeCode +
                ", \n\tlenght=" + lenght +
                ", \n\twidth=" + width +
                ", \n\tdraft=" + draft;
    }
}

    
    

