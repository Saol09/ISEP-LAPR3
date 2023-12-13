/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.utils;


import java.time.Duration;

import javafx.util.Pair;
import lapr.project.model.Ship;
import lapr.project.model.ShipDynamicData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * The type Tree dynamic data.
 *
 * @author andre
 */
public class TREE_DynamicData extends AVL<ShipDynamicData> {

    /**
     * Gets inicio partida date.
     *
     * @return the inicio partida date
     */
    public LocalDateTime getInicioPartidaDate() {
        return getInicioPartidaDate(root);
    }

    private LocalDateTime getInicioPartidaDate(Node<ShipDynamicData> node) {
        if (root == null) return null;

        if (node.getLeft() == null) return node.getElement().getBaseDateTime();

        return getInicioPartidaDate(node.getLeft());
    }

    /**
     * Gets fim partida.
     *
     * @return the fim partida
     */
    public LocalDateTime getFimPartida() {
        return getFimPartida(root);
    }

    private LocalDateTime getFimPartida(Node<ShipDynamicData> node) {
        if (root == null) return null;

        if (node.getRight() == null) return node.getElement().getBaseDateTime();

        return getFimPartida(node.getRight());
    }

    /**
     * Gets numero total movimentos.
     *
     * @return the numero total movimentos
     */
    public Integer getNumeroTotalMovimentos() {
        return getNumeroTotalMovimentos(root);
    }

    private Integer getNumeroTotalMovimentos(Node<ShipDynamicData> node) { // numero total de movimentos vai ser igual ao numero de nos que esta arvore vai ter

        if (node == null) return 0;

        return 1 + getNumeroTotalMovimentos(node.getLeft()) + getNumeroTotalMovimentos(node.getRight());
    }

    /**
     * Gets max sog.
     *
     * @return the max sog
     */
    public double getMaxSog() {
        if (root == null) return 0;
        double max = 0;
        for (ShipDynamicData shipDynamicData : inOrder()) {
            if (shipDynamicData.getSog() > max) {
                max = shipDynamicData.getSog();
            }
        }
        return max;
    }

    /**
     * Gets mean sog.
     *
     * @return the mean sog
     */
    public double getMeanSog() {
        if (root == null) return 0;
        double sum = 0;
        int cont = 0;
        for (ShipDynamicData shipDynamicData : inOrder()) {
            sum += shipDynamicData.getSog();
            cont++;
        }
        return sum / cont;
    }

    /**
     * Gets mean sog between dates.
     *
     * @param dataInicial the data inicial
     * @param dataFinal   the data final
     * @return the mean sog between dates
     */
//Ex4
    public double getMeanSogBetweenDates(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        if (root == null) return 0;
        double sum = 0;
        int cont = 0;
        for (ShipDynamicData shipDynamicData : inOrder()) {
            if (shipDynamicData.getBaseDateTime().compareTo(dataInicial) >= 0 &&
                    shipDynamicData.getBaseDateTime().compareTo(dataFinal) <= 0) {
                sum += shipDynamicData.getSog();
                cont++;
            }
        }
        return sum / cont;
    }

    /**
     * Gets max cog.
     *
     * @return the max cog
     */
    public double getMaxCog() {
        if (root == null) return 0;
        double max = root.getElement().getCog();
        for (ShipDynamicData shipDynamicData : inOrder()) {
            if (shipDynamicData.getCog() > max) {
                max = shipDynamicData.getCog();
            }
        }
        return max;
    }

    /**
     * Gets mean cog.
     *
     * @return the mean cog
     */
    public double getMeanCog() {
        if (root == null) return 0;
        double sum = 0;
        int contador = 0;
        for (ShipDynamicData shipDynamicData : inOrder()) {
            sum += shipDynamicData.getCog();
            contador++;
        }
        return sum / contador;
    }


    /**
     * Gets departure latitude.
     *
     * @return the departure latitude
     */
    public double getDepartureLatitude() {
        return getDepartureLatitude(root);
    }

    private double getDepartureLatitude(Node<ShipDynamicData> node) {
        if (root == null) return 0;

        if (node.getLeft() == null) return node.getElement().getLat();

        return getDepartureLatitude(node.getLeft());

    }


    /**
     * Gets departure longitude.
     *
     * @return the departure longitude
     */
    public double getDepartureLongitude() {
        return getDepartureLongitude(root);
    }

    private double getDepartureLongitude(Node<ShipDynamicData> node) {
        if (root == null) return -1;

        if (node.getLeft() == null) return node.getElement().getLon();

        return getDepartureLongitude(node.getLeft());

    }

    /**
     * Gets arrival latitude.
     *
     * @return the arrival latitude
     */
    public double getArrivalLatitude() {
        return getArrivalLatitude(root);
    }

    private double getArrivalLatitude(Node<ShipDynamicData> node) {
        if (node == null) return -1;

        if (node.getRight() == null) return node.getElement().getLat();

        return getArrivalLatitude(node.getRight());

    }

    /**
     * Gets arrival longitude.
     *
     * @return the arrival longitude
     */
    public double getArrivalLongitude() {
        return getArrivalLongitude(root);
    }

    private double getArrivalLongitude(Node<ShipDynamicData> node) {
        if (root == null) return -1;

        if (node.getRight() == null) return node.getElement().getLon();

        return getArrivalLongitude(node.getRight());

    }

    /**
     * Gets ship coordinates in a certain data
     *
     * @param data data inserida
     * @return coordenadas no navio nessa data
     */
    public Pair<Double, Double> getCoordsByData(LocalDateTime data) {

        if (root == null) return null;
        List<Node<ShipDynamicData>> beforeNodes = new ArrayList<>();
        return getCoordsByData(root, data, beforeNodes);
    }

    private Pair<Double, Double> getCoordsByData(Node<ShipDynamicData> node, LocalDateTime data, List<Node<ShipDynamicData>> beforeNodes) {
        if (root == null) return null;


        if (node == null) {
            if (beforeNodes.size() == 0)
                return null;
            return new Pair<>(beforeNodes.get(beforeNodes.size() - 1).getElement().getLat(), beforeNodes.get(beforeNodes.size() - 1).getElement().getLon());
        }

        if (node.getElement().getBaseDateTime().isBefore(data))
            beforeNodes.add(node);


        if (data.compareTo(node.getElement().getBaseDateTime()) > 0) {
            return getCoordsByData(node.getRight(), data, beforeNodes);
        }

        if (data.compareTo(node.getElement().getBaseDateTime()) < 0)
            return getCoordsByData(node.getLeft(), data, beforeNodes);

        return new Pair<>(node.getElement().getLat(), node.getElement().getLon());

    }


    /**
     * Gets travelled distance.
     *
     * @return the travelled distance
     */
    public double getTravelledDistance() {
        if (root == null) return -1;
        double sum = 0;
        Iterator<ShipDynamicData> iterator1 = inOrder().iterator();
        Iterator<ShipDynamicData> iterator2 = inOrder().iterator();
        iterator2.next();
        while (iterator2.hasNext()) {
            ShipDynamicData s1 = iterator1.next();
            ShipDynamicData s2 = iterator2.next();
            sum += DistancesCalculus.distance(s1.getLat(), s1.getLon(), s2.getLat(), s2.getLon(), "K");
        }
        return sum;
    }

    /**
     * Gets travelled distance between dates.
     *
     * @param dataInicial the data inicial
     * @param dataFinal   the data final
     * @return the travelled distance between dates
     */
//Ex4
    public double getTravelledDistanceBetweenDates(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        if (root == null) return -1;
        double sum = 0;
        Iterator<ShipDynamicData> iterator1 = inOrder().iterator();
        Iterator<ShipDynamicData> iterator2 = inOrder().iterator();
        iterator2.next();
        while (iterator2.hasNext()) {
            ShipDynamicData s1 = iterator1.next();
            ShipDynamicData s2 = iterator2.next();
            if (s1.getBaseDateTime().compareTo(dataInicial) >= 0 && s2.getBaseDateTime().compareTo(dataFinal) <= 0) {
                sum += DistancesCalculus.distance(s1.getLat(), s1.getLon(), s2.getLat(), s2.getLon(), "K");
            }
        }
        return sum;
    }

    /**
     * Gets delta distance.
     *
     * @return the delta distance
     */
    public double getDeltaDistance() {
        if (root == null) return -1;
        Iterator<ShipDynamicData> iterator = inOrder().iterator();
        ShipDynamicData sInicial = iterator.next();
        ShipDynamicData sFinal = sInicial;       //some ships only have 1 dynamic data
        while (iterator.hasNext()) {
            sFinal = iterator.next();
        }
        assert sFinal != null;
        return DistancesCalculus.distance(sInicial.getLat(), sInicial.getLon(), sFinal.getLat(), sFinal.getLon(), "K");
    }

    /**
     * Has two movements between dates boolean.
     *
     * @param dataInicial the data inicial
     * @param dataFinal   the data final
     * @return the boolean
     */
//EX4
    public boolean hasTwoMovementsBetweenDates(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        int contador = 0;

        for (ShipDynamicData dynamicData : inOrder()) {
            if (dynamicData.getBaseDateTime().compareTo(dataInicial) >= 0 &&
                    dynamicData.getBaseDateTime().compareTo(dataFinal) <= 0) {
                contador++;
            }
        }

        if (contador > 1) {
            return true;
        }
        return false;
    }

}
