------------------------------------------------------FUNCAO SHIPS -----------------------------------


CREATE OR REPLACE FUNCTION occupancyDockingArea (
    port_month CargoManifest.cargoManifest_date%TYPE,
    port_place Location.id%TYPE)
RETURN NUMBER
IS
    Occupancy NUMBER(5,2);
    
BEGIN

---SELECT PERCENTAGE OF OCCUPANCY IN PORT

WITH vehiclesOnPort AS(
--select all vehicles que pararam naquela posicao ate aquela data
SELECT distinct Vehicle.vehicleID
FROM Vehicle
INNER JOIN Trip ON Vehicle.vehicleID = Trip.vehicleID
INNER JOIN Stops ON Trip.id = Stops.tripID
INNER JOIN CargoManifest ON Stops.loadingCargoManifestID = CargoManifest.cargoManifestID
WHERE Stops.locationID = port_place  AND CargoManifest.cargoManifest_date <= port_month
UNION
SELECT distinct Vehicle.vehicleID
FROM Vehicle
INNER JOIN Trip ON Vehicle.vehicleID = Trip.vehicleID
INNER JOIN Stops ON Trip.id = Stops.tripID
INNER JOIN CargoManifest ON Stops.unloadingCargoManifestID = CargoManifest.cargoManifestID
WHERE Stops.locationID = port_place  AND CargoManifest.cargoManifest_date <= port_month

--Quero retirar os que ja nao estao
--vou buscar todos os que sairam desse porto ate a data 
--se o vehicle associado a trip que passou nessa stop ate a data ja passou por stops com seqNum maior retiro
MINUS
SELECT distinct Vehicle.vehicleID
FROM Vehicle
INNER JOIN Trip ON Vehicle.vehicleID = Trip.vehicleID
INNER JOIN Stops ON Trip.id = Stops.tripID
INNER JOIN CargoManifest ON Stops.loadingCargoManifestID = CargoManifest.cargoManifestID
WHERE Stops.locationID = port_place AND CargoManifest.cargoManifest_date <= port_month
AND Trip.ID IN --get the id of trips that have passed through that location ate a data
       (SELECT distinct Trip.id
        FROM Trip
        INNER JOIN Vehicle ON Vehicle.vehicleID = Trip.vehicleID
        INNER JOIN Stops ON Trip.id = Stops.tripID
        INNER JOIN CargoManifest ON Stops.loadingCargoManifestID = CargoManifest.cargoManifestID
        WHERE Stops.locationID = port_place  AND CargoManifest.cargoManifest_date <= port_month
        UNION
        SELECT distinct Trip.id
        FROM Trip
        INNER JOIN Vehicle ON Vehicle.vehicleID = Trip.vehicleID
        INNER JOIN Stops ON Trip.id = Stops.tripID
        INNER JOIN CargoManifest ON Stops.unloadingCargoManifestID = CargoManifest.cargoManifestID
        WHERE Stops.locationID = port_place  AND CargoManifest.cargoManifest_date <= port_month)
AND Stops.seqNum < (SELECT distinct Stops.seqNum    --aquela trip ja passou por stops com seqNum maior ate a a data
                    FROM Stops
                    INNER JOIN Trip ON Trip.id = Stops.tripID
                    INNER JOIN Vehicle ON Vehicle.vehicleID = Trip.vehicleID
                    INNER JOIN CargoManifest ON Stops.loadingCargoManifestID = CargoManifest.cargoManifestID
                    WHERE Stops.locationID = port_place  AND CargoManifest.cargoManifest_date <= port_month
                    UNION
                    SELECT distinct Stops.seqNum    --aquela trip ja passou por stops com seqNum maior ate a a data
                    FROM Stops
                    INNER JOIN Trip ON Trip.id = Stops.tripID
                    INNER JOIN Vehicle ON Vehicle.vehicleID = Trip.vehicleID
                    INNER JOIN CargoManifest ON Stops.unloadingCargoManifestID = CargoManifest.cargoManifestID
                    WHERE Stops.locationID = port_place  AND CargoManifest.cargoManifest_date <= port_month)
UNION
SELECT distinct Vehicle.vehicleID
FROM Vehicle
INNER JOIN Trip ON Vehicle.vehicleID = Trip.vehicleID
INNER JOIN Stops ON Trip.id = Stops.tripID
INNER JOIN CargoManifest ON Stops.unloadingCargoManifestID = CargoManifest.cargoManifestID
WHERE Stops.locationID = port_place AND CargoManifest.cargoManifest_date <= port_month
AND Trip.ID IN --get the id of trips that have passed through that location ate a data
       (SELECT distinct Trip.id
        FROM Trip
        INNER JOIN Vehicle ON Vehicle.vehicleID = Trip.vehicleID
        INNER JOIN Stops ON Trip.id = Stops.tripID
        INNER JOIN CargoManifest ON Stops.loadingCargoManifestID = CargoManifest.cargoManifestID
        WHERE Stops.locationID = port_place  AND CargoManifest.cargoManifest_date <= port_month
        UNION
        SELECT distinct Trip.id
        FROM Trip
        INNER JOIN Vehicle ON Vehicle.vehicleID = Trip.vehicleID
        INNER JOIN Stops ON Trip.id = Stops.tripID
        INNER JOIN CargoManifest ON Stops.unloadingCargoManifestID = CargoManifest.cargoManifestID
        WHERE Stops.locationID = port_place  AND CargoManifest.cargoManifest_date <= port_month)
AND Stops.seqNum < (SELECT distinct Stops.seqNum    --aquela trip ja passou por stops com seqNum maior ate a a data
                    FROM Stops
                    INNER JOIN Trip ON Trip.id = Stops.tripID
                    INNER JOIN Vehicle ON Vehicle.vehicleID = Trip.vehicleID
                    INNER JOIN CargoManifest ON Stops.loadingCargoManifestID = CargoManifest.cargoManifestID
                    WHERE Stops.locationID = port_place  AND CargoManifest.cargoManifest_date <= port_month
                    UNION
                    SELECT distinct Stops.seqNum    --aquela trip ja passou por stops com seqNum maior ate a a data
                    FROM Stops
                    INNER JOIN Trip ON Trip.id = Stops.tripID
                    INNER JOIN Vehicle ON Vehicle.vehicleID = Trip.vehicleID
                    INNER JOIN CargoManifest ON Stops.unloadingCargoManifestID = CargoManifest.cargoManifestID
                    WHERE Stops.locationID = port_place  AND CargoManifest.cargoManifest_date <= port_month) )
,
NoTripVehicles AS(
SELECT distinct Vehicle.vehicleID
FROM Vehicle
INNER JOIN Ship ON Vehicle.vehicleID = Ship.MMSI
WHERE Vehicle.vehicle_initialLocation_ID = port_place AND Ship.MMSI NOT IN 
               (SELECT distinct Vehicle.vehicleID
                FROM Vehicle
                INNER JOIN Trip ON Vehicle.vehicleID = Trip.vehicleID))
,
--get portCapacity
PortShipCapacity AS(
SELECT Port.dockingAreaCapacity
FROM Port
WHERE Port.portID = port_place)
Select (((SELECT COUNT(*) FROM vehiclesOnPort) + (SELECT COUNT(*) FROM NoTripVehicles)) / (SELECT * FROM PortShipCapacity)) * 100  INTO Occupancy FROM DUAL;
---END OF SELECT

RETURN Occupancy;
END;
/




------------------------------------------------------FUNCAO CONTAINERS -----------------------------------


CREATE OR REPLACE FUNCTION mapOfOccupancy (
    port_month CargoManifest.cargoManifest_date%TYPE,
    port_place Location.id%TYPE)
RETURN NUMBER
IS
    Occupancy NUMBER(5,2);
    
    
BEGIN

--select of occupancy
WITH UnloadsMinusLoadsPort_Month AS(
SELECT COUNT(Container_CargoManifest.container_id)
FROM Container_CargoManifest
INNER JOIN CargoManifest ON Container_CargoManifest.cargoManifestID = CargoManifest.cargoManifestID
INNER JOIN Stops ON CargoManifest.cargoManifestID = Stops.unloadingCargoManifestID
WHERE CargoManifest.cargoManifest_date <= port_month AND Stops.LocationID = port_place
MINUS
SELECT COUNT(Container_CargoManifest.container_id)
FROM Container_CargoManifest
INNER JOIN CargoManifest ON Container_CargoManifest.cargoManifestID = CargoManifest.cargoManifestID
INNER JOIN Stops ON CargoManifest.cargoManifestID = Stops.loadingCargoManifestID
WHERE CargoManifest.cargoManifest_date <= port_month AND Stops.LocationID = port_place
)
,
PortCapacity AS(
SELECT Location.capacity
FROM Location
WHERE Location.id = port_place)
Select ((SELECT * FROM UnloadsMinusLoadsPort_Month)) / (SELECT * FROM PortCapacity) * 100 INTO Occupancy FROM DUAL;

RETURN Occupancy;
END;
/   
-----------------------
--FUNCAO PARA RETORNAR TODOS OS DIAS DA SEMANA --
------------------------


CREATE OR REPLACE FUNCTION getAllDaysOfMonth (
    port_month CargoManifest.cargoManifest_date%TYPE)
RETURN SYS_REFCURSOR
IS
    days_of_month SYS_REFCURSOR; 
    
    BEGIN
    
    OPEN days_of_month FOR
    select to_date(trunc(port_month, 'MM'))+level-1
    from dual 
    connect by level <= TO_CHAR(LAST_DAY(trunc(port_month, 'MM')),'DD');
    
    RETURN days_of_month;
END;
/



----------

---------------------

begin 

DECLARE
occupancyContainers NUMBER(5,2);
occupancyShips NUMBER(5,2);
days_of_month SYS_REFCURSOR;
dayOfMonth CargoManifest.cargoManifest_date%TYPE;

begin
    
    --loop through all days of month
    days_of_month := getAllDaysOfMonth(TO_DATE('2021.05.05','YYYY.MM.DD'));
    LOOP
        FETCH days_of_month INTO dayOfMonth;
        exit when days_of_month%notfound;
    --dbms_output.put_line('dayOfMonth: ' || dayOfMonth);
    occupancyContainers := mapOfOccupancy(dayOfMonth, 2);
    occupancyShips := occupancyDockingArea(dayOfMonth, 2);
    IF occupancyContainers IS NULL AND occupancyShips IS NULL THEN
    dbms_output.put_line('Occupancy Warehouse: 0%' ||'|| Occupancy Port: 0%'  || '           || date: ' || dayOfMonth);
    
    ELSIF occupancyContainers IS NULL AND occupancyShips IS NOT NULL THEN
    dbms_output.put_line('Occupancy Warehouse: 0%' ||'|| Occupancy Port: '  || occupancyShips || '% ' || '           || date: ' || dayOfMonth);
    
    ELSIF occupancyShips IS NULL AND occupancyContainers IS NOT NULL THEN
    dbms_output.put_line('Occupancy Warehouse: ' || occupancyContainers || '% ' ||'|| Occupancy Port: 0%' ||  '        || date: ' || dayOfMonth);
    
    ELSE
    dbms_output.put_line('Occupancy Warehouse: ' || occupancyContainers || '% ' ||'|| Occupancy Port: '  || occupancyShips || '% ' ||  '        || date: ' || dayOfMonth);
    
    END IF;
    end loop;
    
    close days_of_month;
    


end;

end;
/
