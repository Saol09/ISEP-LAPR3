--# tabela Client #--
INSERT INTO Client (id, clientName) VALUES (1,'Jonh');
INSERT INTO Client (id, clientName) VALUES (2,'Michael');
INSERT INTO Client (id, clientName) VALUES (3,'Dave');
INSERT INTO Client (id, clientName) VALUES (4,'Obie');
INSERT INTO Client (id, clientName) VALUES (5,'Peter');
INSERT INTO Client (id, clientName) VALUES (6,'Joseph');


-- ## tabela CONTAINER ##
INSERT INTO Container (containerID, payload, tare, gross, temperature, ISOCode, checkDigit, maxVolume, container_Type) 
    VALUES (1, 123, 100, 100, 32, '21PG', 100, 100, 1);
INSERT INTO Container (containerID, payload, tare, gross, temperature, ISOCode, checkDigit, maxVolume, container_Type) 
    VALUES (2, 123, 100, 100, 32, '22PG', 100, 100, 1);
INSERT INTO Container (containerID, payload, tare, gross, temperature, ISOCode, checkDigit, maxVolume, container_Type) 
    VALUES (3, 123, 100, 100, 32, '23PG', 100, 100, 1);
INSERT INTO Container (containerID, payload, tare, gross, temperature, ISOCode, checkDigit, maxVolume, container_Type)
    VALUES (4, 123, 100, 100, 32, '24PG', 100, 100, 1);
INSERT INTO Container (containerID, payload, tare, gross, temperature, ISOCode, checkDigit, maxVolume, container_Type)
    VALUES (5, 123, 100, 100, 32, '25PG', 100, 100, 1);
INSERT INTO Container (containerID, payload, tare, gross, temperature, ISOCode, checkDigit, maxVolume, container_Type)
    VALUES (6, 123, 100, 100, 32, '26PG', 100, 100, 1);
INSERT INTO Container (containerID, payload, tare, gross, temperature, ISOCode, checkDigit, maxVolume, container_Type)
    VALUES (7, 123, 100, 100, 32, '27PG', 100, 100, 1);
INSERT INTO Container (containerID, payload, tare, gross, temperature, ISOCode, checkDigit, maxVolume, container_Type)
    VALUES (8, 123, 100, 100, 32, '28PG', 100, 100, 1);
INSERT INTO Container (containerID, payload, tare, gross, temperature, ISOCode, checkDigit, maxVolume, container_Type)
    VALUES (9, 123, 100, 100, 32, '29PG', 100, 100, 1);


--# tabela Order #--
--order 1 for client 1 --
INSERT INTO "ORDER" (order_id, client_id, container_id, order_date, order_state)
    VALUES (1, 1, 1, TO_DATE('2021.04.25','YYYY.MM.DD'), 'LOADED');
INSERT INTO "ORDER" (order_id, client_id, container_id, order_date, order_state)
    VALUES (2, 1, 2, TO_DATE('2021.04.25','YYYY.MM.DD'), 'LOADED');
INSERT INTO "ORDER" (order_id, client_id, container_id, order_date, order_state)
    VALUES (3, 1, 3, TO_DATE('2021.04.25','YYYY.MM.DD'), 'LOADED');
    
--for client 2 --
INSERT INTO "ORDER" (order_id, client_id, container_id, order_date, order_state)
    VALUES (4, 2, 4, TO_DATE('2021.04.15','YYYY.MM.DD'), 'FINISHED');
INSERT INTO "ORDER" (order_id, client_id, container_id, order_date, order_state)
    VALUES (5, 2, 5, TO_DATE('2021.04.15','YYYY.MM.DD'), 'FINISHED');
    
--for client 3 --
INSERT INTO "ORDER" (order_id, client_id, container_id, order_date, order_state)
    VALUES (6, 3, 6, TO_DATE('2021.04.15','YYYY.MM.DD'), 'FINISHED');

--for client 4 --
INSERT INTO "ORDER" (order_id, client_id, container_id, order_date, order_state)
    VALUES (7, 4, 7, TO_DATE('2021.04.21','YYYY.MM.DD'), 'FINISHED');

--for client 5 --
INSERT INTO "ORDER" (order_id, client_id, container_id, order_date, order_state)
    VALUES (8, 5, 8, TO_DATE('2021.04.21','YYYY.MM.DD'), 'FINISHED');

--for client 6 --
INSERT INTO "ORDER" (order_id, client_id, container_id, order_date, order_state)
    VALUES (9, 6, 9, TO_DATE('2021.04.21','YYYY.MM.DD'), 'IN-PROGRESS');


--# tabela CargoManifest #--
-- for client 1 --
INSERT INTO CargoManifest (cargoManifestId, cargoManifest_date, CMTYPE) VALUES ('1', TO_DATE('2021.04.25','YYYY.MM.DD'), 'L');
INSERT INTO CargoManifest (cargoManifestId, cargoManifest_date, CMTYPE) VALUES ('2', TO_DATE('2021.05.05','YYYY.MM.DD'), 'U');
-- for client 2 --
INSERT INTO CargoManifest (cargoManifestId, cargoManifest_date, CMTYPE) VALUES ('3', TO_DATE('2021.04.15','YYYY.MM.DD'), 'L');
INSERT INTO CargoManifest (cargoManifestId, cargoManifest_date, CMTYPE) VALUES ('4', TO_DATE('2021.04.30','YYYY.MM.DD'), 'U');
-- for client 3 --
INSERT INTO CargoManifest (cargoManifestId, cargoManifest_date, CMTYPE) VALUES ('5', TO_DATE('2021.04.15','YYYY.MM.DD'), 'L');
INSERT INTO CargoManifest (cargoManifestId, cargoManifest_date, CMTYPE) VALUES ('6', TO_DATE('2021.04.30','YYYY.MM.DD'), 'U');

-- for client 4, 5 and 6 (loading) --
INSERT INTO CargoManifest (cargoManifestId, cargoManifest_date, CMTYPE) VALUES ('7', TO_DATE('2021.04.21','YYYY.MM.DD'), 'L');

-- for client 4 --
INSERT INTO CargoManifest (cargoManifestId, cargoManifest_date, CMTYPE) VALUES ('8', TO_DATE('2021.04.30','YYYY.MM.DD'), 'U');

-- for client 5 --
INSERT INTO CargoManifest (cargoManifestId, cargoManifest_date, CMTYPE) VALUES ('9', TO_DATE('2021.05.05','YYYY.MM.DD'), 'U');

-- for client 6 --
INSERT INTO CargoManifest (cargoManifestId, cargoManifest_date, CMTYPE) VALUES ('10', TO_DATE('2021.05.10','YYYY.MM.DD'), 'U');




-- ## tabela Container_CargoManifest ##

--client 1
--for cargoManifest 1
--loading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (1, 1, 12, 12, 21, 200.00);
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (2, 1, 12, 12, 21, 200.00);
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (3, 1, 12, 12, 21, 200.00);
--for cargoManifest 2
--unloading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (1,2, 12, 12, 21, 200.00);
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (2,2, 12, 12, 21, 200.00);
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (3,2, 12, 12, 21, 200.00);
--client 2
--loading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (4, 3, 12, 12, 21, 200.00);
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (5, 3, 12, 12, 21, 200.00);
--unloading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (4, 4, 12, 12, 21, 200.00);
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (5, 4, 12, 12, 21, 200.00);

--client 3
--loading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (6, 5, 12, 12, 21, 200.00);
--unloading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (6, 6, 12, 12, 21, 200.00);
    
--client 4
--loading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (7, 7, 30, 30, 30, 200.00);
--unloading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (7, 8, 30, 30, 30, 200.00);

--client 5
--loading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (8, 7, 50, 50, 50, 200.00);
--unloading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (8, 9, 50, 50, 50, 200.00);
    
--client 6
--loading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (9, 7, 12, 12, 21, 200.00);
--unloading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (9, 10, 12, 12, 21, 200.00);

    


-- ## tabela Vehicle ## --vehicle initial location pode ter que estar associado a uma location
INSERT INTO Vehicle (vehicleID, vehicle_initialLocation, vehicle_initialLocation_ID) VALUES ('12-13-WD', 'PortWarehouse6',  6);
INSERT INTO Vehicle (vehicleID, vehicle_initialLocation, vehicle_initialLocation_ID) VALUES ('123456789', 'PortWarehouse1', 1);
INSERT INTO Vehicle (vehicleID, vehicle_initialLocation, vehicle_initialLocation_ID) VALUES ('123456790', 'PortWarehouse1', 1);
INSERT INTO Vehicle (vehicleID, vehicle_initialLocation, vehicle_initialLocation_ID) VALUES ('123456791', 'PortWarehouse1', 1);
INSERT INTO Vehicle (vehicleID, vehicle_initialLocation, vehicle_initialLocation_ID) VALUES ('123456792', 'PortWarehouse1', 1);
INSERT INTO Vehicle (vehicleID, vehicle_initialLocation, vehicle_initialLocation_ID) VALUES ('123456793', 'PortWarehouse1', 1);



-- ## tabela Trip ##
---viagem a meio
INSERT INTO Trip (id, dataPartida, dataChegada, vehicleId, LastStopSeqNum) 
    VALUES (1, TO_DATE('2021.04.25','YYYY.MM.DD'), TO_DATE('2021.05.05','YYYY.MM.DD'), '123456789', 1);

---viagem no fim
INSERT INTO Trip (id, dataPartida, dataChegada, vehicleId, LastStopSeqNum) 
    VALUES (2, TO_DATE('2021.04.15','YYYY.MM.DD'), TO_DATE('2021.04.30','YYYY.MM.DD'), '123456790', 2);
    
---viagem no fim
INSERT INTO Trip (id, dataPartida, dataChegada, vehicleId, LastStopSeqNum) 
    VALUES (3, TO_DATE('2021.04.15','YYYY.MM.DD'), TO_DATE('2021.04.30','YYYY.MM.DD'), '12-13-WD', 2);
    
---viagem no meio
INSERT INTO Trip (id, dataPartida, dataChegada, vehicleId, LastStopSeqNum) 
    VALUES (4, TO_DATE('2021.04.21','YYYY.MM.DD'), TO_DATE('2021.05.10','YYYY.MM.DD'), '123456793', 3);
    

-- ## tabela Order_Trip ## --
--for trip 1
INSERT INTO Order_Trip (tripID, order_ID)
    VALUES (1,1);
INSERT INTO Order_Trip (tripID, order_ID)
    VALUES (1,2);
INSERT INTO Order_Trip (tripID, order_ID)
    VALUES (1,3);
    
--for trip 2
INSERT INTO Order_Trip (tripID, order_ID)
    VALUES (2,4);
INSERT INTO Order_Trip (tripID, order_ID)
    VALUES (2,5);
   
--fpr trip 3
INSERT INTO Order_Trip (tripID, order_ID)
    VALUES (3,6);
    
--for trip 4
INSERT INTO Order_Trip (tripID, order_ID)
    VALUES (4,7);   
INSERT INTO Order_Trip (tripID, order_ID)
    VALUES (4,8);
INSERT INTO Order_Trip (tripID, order_ID)
    VALUES (4,9);


-- ## tabela Location ##
INSERT INTO Location (id, name, continent, country, latitude, longitude, capacity,occupancy) 
    VALUES (1,'PortWarehouse1','Europe', 'Portugal', 8.8, 8.2, 200, 50);
INSERT INTO Location (id, name, continent, country, latitude, longitude, capacity,occupancy)
    VALUES (2,'PortWarehouse2','Europe', 'Spain', 5.0, 5.1, 400, 50);
INSERT INTO Location (id, name, continent, country, latitude, longitude, capacity,occupancy)
    VALUES (3,'PortWarehouse3','Europe', 'Spain', 5.0, 5.1, 200, 50);
INSERT INTO Location (id, name, continent, country, latitude, longitude, capacity,occupancy)
    VALUES (4,'PortWarehouse4','Europe', 'Spain', 5.0, 5.1, 400, 50);
INSERT INTO Location (id, name, continent, country, latitude, longitude, capacity,occupancy)
    VALUES (5,'PortWarehouse5','Europe', 'Spain', 5.0, 5.1, 200, 50);
INSERT INTO Location (id, name, continent, country, latitude, longitude, capacity,occupancy)
    VALUES (6,'PortWarehouse6','Europe', 'Spain', 5.0, 5.1, 400, 50);
INSERT INTO Location (id, name, continent, country, latitude, longitude, capacity,occupancy)
    VALUES (7,'PortWarehouse7','Europe', 'Spain', 5.0, 5.1, 200, 50);
INSERT INTO Location (id, name, continent, country, latitude, longitude, capacity,occupancy)
    VALUES (8,'PortWarehouse8','Europe', 'Spain', 5.0, 5.1, 400, 50);
INSERT INTO Location (id, name, continent, country, latitude, longitude, capacity,occupancy)
    VALUES (9,'PortWarehouse9','Europe', 'Spain', 5.0, 5.1, 200, 50);
INSERT INTO Location (id, name, continent, country, latitude, longitude, capacity,occupancy)
    VALUES (10,'PortWarehouse10','Europe', 'Spain', 5.0, 5.1, 400, 50);
INSERT INTO Location (id, name, continent, country, latitude, longitude, capacity,occupancy)
    VALUES (11,'PortWarehouse11','Europe', 'Spain', 5.0, 5.1, 200, 50);
    

-- ## tabela Stops ##

--stops para viagem 1 --
INSERT INTO Stops(tripId, locationId, seqNum, unloadingCargoManifestId, loadingCargoManifestId, dataChegada, dataPartida)
    VALUES (1, 1, 1, null, 1, TO_DATE('2021.04.25','YYYY.MM.DD'), TO_DATE('2021.05.01','YYYY.MM.DD') );
INSERT INTO Stops(tripId, locationId, seqNum, unloadingCargoManifestId, loadingCargoManifestId, dataChegada, dataPartida)
    VALUES (1, 2, 2, 2, null, TO_DATE('2021.05.05','YYYY.MM.DD'), null);
    
--stops para viagem 2 --
INSERT INTO Stops(tripId, locationId, seqNum, unloadingCargoManifestId, loadingCargoManifestId, dataChegada, dataPartida)
    VALUES (2, 3, 1, null, 3, TO_DATE('2021.04.15','YYYY.MM.DD'), TO_DATE('2021.04.20','YYYY.MM.DD'));
INSERT INTO Stops(tripId, locationId, seqNum, unloadingCargoManifestId, loadingCargoManifestId, dataChegada, dataPartida)
    VALUES (2, 4, 2, 4, null, TO_DATE('2021.04.30','YYYY.MM.DD'), null);

--stops para viagem 3 --
INSERT INTO Stops(tripId, locationId, seqNum, unloadingCargoManifestId, loadingCargoManifestId, dataChegada, dataPartida)
    VALUES (3, 6, 1, null, 5, TO_DATE('2021.04.15','YYYY.MM.DD'), TO_DATE('2021.04.20','YYYY.MM.DD'));
INSERT INTO Stops(tripId, locationId, seqNum, unloadingCargoManifestId, loadingCargoManifestId, dataChegada, dataPartida)
    VALUES (3, 7, 2, 6, null, TO_DATE('2021.04.30','YYYY.MM.DD'), null);
    
--stops para viagem 4 --
INSERT INTO Stops(tripId, locationId, seqNum, unloadingCargoManifestId, loadingCargoManifestId, dataChegada, dataPartida)
    VALUES (4, 5, 1, null, 7, TO_DATE('2021.04.21','YYYY.MM.DD'), TO_DATE('2021.04.25','YYYY.MM.DD'));
INSERT INTO Stops(tripId, locationId, seqNum, unloadingCargoManifestId, loadingCargoManifestId, dataChegada, dataPartida)
    VALUES (4, 9, 2, 8, null, TO_DATE('2021.04.30','YYYY.MM.DD'), TO_DATE('2021.05.01','YYYY.MM.DD'));
INSERT INTO Stops(tripId, locationId, seqNum, unloadingCargoManifestId, loadingCargoManifestId, dataChegada, dataPartida)
    VALUES (4, 10, 3, 9, null, TO_DATE('2021.05.05','YYYY.MM.DD'), TO_DATE('2021.05.06','YYYY.MM.DD'));
INSERT INTO Stops(tripId, locationId, seqNum, unloadingCargoManifestId, loadingCargoManifestId, dataChegada, dataPartida)
    VALUES (4, 11, 4, 10, null, TO_DATE('2021.05.10','YYYY.MM.DD'), null);


  
-- ## tabela Truck ##
INSERT INTO Truck (truckID) VALUES ('12-13-WD');

    
-- ## tabela Warehouse ##
INSERT INTO Warehouse (warehouseId) VALUES (6);
INSERT INTO Warehouse (warehouseId) VALUES (7);
INSERT INTO Warehouse (warehouseId) VALUES (8);

-- ## tabela Port ##
INSERT INTO Port (PortId, dockingAreaCapacity) VALUES (1, 20);
INSERT INTO Port (PortId, dockingAreaCapacity) VALUES (2, 20);
INSERT INTO Port (PortId, dockingAreaCapacity) VALUES (3, 20);
INSERT INTO Port (PortId, dockingAreaCapacity) VALUES (4, 20);
INSERT INTO Port (PortId, dockingAreaCapacity) VALUES (5, 20);
INSERT INTO Port (PortId, dockingAreaCapacity) VALUES (9, 20);
INSERT INTO Port (PortId, dockingAreaCapacity) VALUES (10, 20);
INSERT INTO Port (PortId, dockingAreaCapacity) VALUES (11, 20);




-- ## tabela VesselType ##
INSERT INTO VesselType (code, shipType) VALUES (1, 'type1');

-- ## tabela Transceiver ##
INSERT INTO Transceiver (id) VALUES (1);

-- ## tabela Ship ##
INSERT INTO Ship (MMSI, name, IMOId, energyGenerator, generatorsPowerOutput, callSign, length, width, capacity, vesselTypeCode, LoadingCargoManifestRegistry) 
    VALUES ('123456789', 'nameShip1', 1234567, 300, 200, 'callSign1', 100.32, 30.67, 200, 1, 1);
INSERT INTO Ship (MMSI, name, IMOId, energyGenerator, generatorsPowerOutput, callSign, length, width, capacity, vesselTypeCode, LoadingCargoManifestRegistry) 
    VALUES ('123456790', 'nameShip2', 1234567, 300, 200, 'callSign2', 100.32, 30.67, 200, 1, 1);
INSERT INTO Ship (MMSI, name, IMOId, energyGenerator, generatorsPowerOutput, callSign, length, width, capacity, vesselTypeCode, LoadingCargoManifestRegistry) 
    VALUES ('123456791', 'nameShip3', 1234567, 300, 200, 'callSign3', 100.32, 30.67, 200, 1, 1);
INSERT INTO Ship (MMSI, name, IMOId, energyGenerator, generatorsPowerOutput, callSign, length, width, capacity, vesselTypeCode, LoadingCargoManifestRegistry) 
    VALUES ('123456792', 'nameShip4', 1234567, 300, 200, 'callSign4', 100.32, 30.67, 200, 1, 1);
INSERT INTO Ship (MMSI, name, IMOId, energyGenerator, generatorsPowerOutput, callSign, length, width, capacity, vesselTypeCode, LoadingCargoManifestRegistry) 
    VALUES ('123456793', 'nameShip5', 1234567, 300, 200, 'callSign5', 100.32, 30.67, 200, 1, 1);   
    

--# tabela ShipCaptain #--
INSERT INTO ShipCaptain (id,name) VALUES ('1','James');
INSERT INTO ShipCaptain (id,name) VALUES ('2','Jonh');
INSERT INTO ShipCaptain (id,name) VALUES ('3','Andre');
INSERT INTO ShipCaptain (id,name) VALUES ('4','Michael');

    
--# tabela ShipCaptain_Ship #--
INSERT INTO ShipCaptain_Ship (shipCaptainId, ShipMMSI) VALUES ('1','123456789');
INSERT INTO ShipCaptain_Ship (shipCaptainId, ShipMMSI) VALUES ('1','123456790');
INSERT INTO ShipCaptain_Ship (shipCaptainId, ShipMMSI) VALUES ('1','123456791');
INSERT INTO ShipCaptain_Ship (shipCaptainId, ShipMMSI) VALUES ('1','123456792');
INSERT INTO ShipCaptain_Ship (shipCaptainId, ShipMMSI) VALUES ('1','123456793');


-- ## tabela DynamicData ##
INSERT INTO dynamicdata (shipMMSI, dateTime, latitude, longitude, sog, cog, heading, position, transceiverId) 
    VALUES ('123456789', TO_DATE('2021.12.26','YYYY.MM.DD'), 38.8, 8.2, 100, 170, 120, 200, 1); 