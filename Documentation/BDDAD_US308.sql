-------INSERTS PARA TESTAR---------------
    
--for client Test 308 --
INSERT INTO "ORDER" (order_id, client_id, container_id, order_date, order_state)
    VALUES (21, 4, 1, TO_DATE('2021.04.15','YYYY.MM.DD'), 'LOADED');
INSERT INTO "ORDER" (order_id, client_id, container_id, order_date, order_state)
    VALUES (22, 4, 2, TO_DATE('2021.04.15','YYYY.MM.DD'), 'LOADED');
INSERT INTO "ORDER" (order_id, client_id, container_id, order_date, order_state)
    VALUES (23, 4, 3, TO_DATE('2021.04.15','YYYY.MM.DD'), 'LOADED');



-- for client Test 308 --
INSERT INTO CargoManifest (cargoManifestId, cargoManifest_date, CMTYPE) VALUES ('21', TO_DATE('2021.04.15','YYYY.MM.DD'), 'L');
INSERT INTO CargoManifest (cargoManifestId, cargoManifest_date, CMTYPE) VALUES ('22', TO_DATE('2021.04.30','YYYY.MM.DD'), 'U');



--client Test 308
--loading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (1, 21, 12, 12, 21, 200.00);
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (2, 21, 12, 12, 21, 200.00);
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (3, 21, 12, 12, 21, 200.00);
--
--unloading
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (1, 22, 12, 12, 21, 200.00);
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (2, 22, 12, 12, 21, 200.00);
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (3, 22, 12, 12, 21, 200.00);
    




--Test 308
INSERT INTO Vehicle (vehicleID, vehicle_initialLocation, vehicle_initialLocation_ID) VALUES ('999999999', 'PortWarehouse2', 2);




--para cliente Test 308 na data 2021.04.25--
INSERT INTO CargoSlot (id, order_id, posicaoX, posicaoY, posicaoZ, vehicleID) 
    VALUES (21, 21, 12, 12, 21,'999999999');
INSERT INTO CargoSlot (id, order_id, posicaoX, posicaoY, posicaoZ, vehicleID) 
    VALUES (22, 22, 12, 12, 21,'999999999');
INSERT INTO CargoSlot (id, order_id, posicaoX, posicaoY, posicaoZ, vehicleID) 
    VALUES (23, 23, 12, 12, 21,'999999999');

   
---viagem teste 308
INSERT INTO Trip (id, dataPartida, dataChegada, vehicleId, LastStopSeqNum) 
    VALUES (7, TO_DATE('2021.04.15','YYYY.MM.DD'), TO_DATE('2021.04.30','YYYY.MM.DD'), '999999999', 1);
    


--Ship test 308
INSERT INTO Ship (MMSI, name, IMOId, energyGenerator, generatorsPowerOutput, callSign, length, width, capacity, vesselTypeCode, LoadingCargoManifestRegistry) 
    VALUES ('999999999', 'test', 1234567, 300, 200, 'callSign5', 100.32, 30.67, 1, 1, 1);   
    
   
--stops para viagem teste 308 --
INSERT INTO Stops(tripId, locationId, seqNum, unloadingCargoManifestId, loadingCargoManifestId)
    VALUES (7, 6, 1, null, 21);
    
--passa, pois cargoManifests estao a null   
INSERT INTO Stops(tripId, locationId, seqNum, unloadingCargoManifestId, loadingCargoManifestId)
    VALUES (7, 7, 2, null, null);


--nao passa
UPDATE Stops
SET unloadingCargoManifestId = null, loadingCargoManifestId=21
WHERE tripId=7 AND seqNum=2;


DELETE FROM Stops WHERE tripID = 7;



select * from stops



-----------TRIGGER---------


CREATE or replace TRIGGER check_ship_available_capacity
before INSERT OR UPDATE ON Stops 
    FOR EACH ROW
DECLARE
v_ShipInitialCapacity INTEGER;
v_CurrentShipCapacity INTEGER;
v_LoadsMinusUnloads INTEGER;
v_Ship Ship.MMSI%TYPE;
v_Trip Trip.ID%TYPE := :new.tripID;
pragma autonomous_transaction;
BEGIN

--SELECT SHIP IN WICH CARGO IS BEING INSERTED
SELECT Ship.MMSI INTO v_Ship
FROM Ship
INNER JOIN Trip ON Trip.vehicleID = Ship.MMSI
WHERE Trip.ID = :new.tripID;


--SELECT CAPACITY OF SHIP IN WICH CARGO IS BEING INSERTED
SELECT Ship.capacity INTO v_ShipInitialCapacity
FROM Ship
WHERE Ship.MMSI = v_Ship;


--CURRENT SHIP CAPACITY AFTER ALL THE LOADS AND UNLOADS ATE "A DATA"
WITH Loads_Minus_Unloads AS(
SELECT Container_CargoManifest.container_ID
FROM Container_CargoManifest 
INNER JOIN Stops on Container_CargoManifest.cargoManifestID = :new.loadingCargoManifestID
INNER JOIN Trip on :new.tripID = Trip.ID
INNER JOIN Ship on Trip.vehicleID = Ship.MMSI
WHERE Ship.MMSI = v_Ship AND Trip.ID = v_Trip AND seqNum <= :new.seqNum
MINUS
SELECT Container_CargoManifest.container_ID
FROM Container_CargoManifest 
INNER JOIN Stops on Container_CargoManifest.cargoManifestID = :new.unloadingCargoManifestID
INNER JOIN Trip on :new.tripID = Trip.ID
INNER JOIN Ship on Trip.vehicleID = Ship.MMSI
WHERE Ship.MMSI = v_Ship AND Trip.ID = v_Trip AND seqNum <= :new.seqNum)

SELECT COUNT(*) INTO v_LoadsMinusUnloads 
FROM Loads_Minus_Unloads;


v_CurrentShipCapacity := v_ShipInitialCapacity - v_LoadsMinusUnloads;

IF (v_CurrentShipCapacity < 0 )
    THEN
        raise_application_error(-20000
                , 'this insert will overlap the ships container capacity');
               
    END IF;
COMMIT;
END;