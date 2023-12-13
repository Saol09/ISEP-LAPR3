

--QUERY Coalesce


--se o location name for null, entao usa o vehicle_initialLocation

select distinct vehicle.vehicleid,
Coalesce (location.name, vehicle.vehicle_initialLocation) as LOCATION
from vehicle inner join ship on vehicle.vehicleid = ship.mmsi
full outer join trip on vehicle.vehicleid = trip.vehicleid
full outer join stops on trip.id = stops.tripid
full outer join location on stops.locationid = location.id
group by vehicle.vehicleid, location.name, Stops.SeqNum, trip.dataChegada, trip.dataPartida, trip.LastStopSeqNum, vehicle.vehicle_initialLocation
having stops.SeqNum = (select Max(Stops.SeqNum) from stops inner join trip on stops.tripid = trip.id AND trip.vehicleid = vehicle.vehicleid)
    AND  (trip.dataPartida <(select NEXT_DAY( SYSDATE, 'MONDAY' ) from dual) AND trip.dataChegada<=(select NEXT_DAY( SYSDATE, 'MONDAY' ) from dual))
    OR   (trip.dataPartida >(select NEXT_DAY( SYSDATE, 'MONDAY' ) from dual) AND trip.LastStopSeqNum < MAX(trip.LastStopSeqNum) )
    OR   (vehicle.vehicleid NOT IN (select distinct vehicle.vehicleid from vehicle inner join trip on vehicle.vehicleid = trip.vehicleid))
order by vehicle.vehicleid


--QUERY com CASE

select distinct vehicle.vehicleid,
CASE 
    WHEN vehicle.vehicleid NOT IN (select trip.vehicleid from trip)
    THEN vehicle.vehicle_initialLocation
    ELSE location.name
    END as No_Trips_SHIPS_location
from vehicle inner join ship on vehicle.vehicleid = ship.mmsi
full outer join trip on vehicle.vehicleid = trip.vehicleid
full outer join stops on trip.id = stops.tripid
full outer join location on stops.locationid = location.id
group by vehicle.vehicleid, location.name, Stops.SeqNum, trip.dataChegada, trip.dataPartida, trip.LastStopSeqNum, vehicle.vehicle_initialLocation
having stops.SeqNum = (select Max(Stops.SeqNum) from stops inner join trip on stops.tripid = trip.id AND trip.vehicleid = vehicle.vehicleid)
    AND  (trip.dataPartida <(select NEXT_DAY( SYSDATE, 'MONDAY' ) from dual) AND trip.dataChegada<=(select NEXT_DAY( SYSDATE, 'MONDAY' ) from dual))
    OR   (trip.dataPartida >(select NEXT_DAY( SYSDATE, 'MONDAY' ) from dual) AND trip.LastStopSeqNum < MAX(trip.LastStopSeqNum) )
    OR   (vehicle.vehicleid NOT IN (select distinct vehicle.vehicleid from vehicle inner join trip on vehicle.vehicleid = trip.vehicleid))
order by vehicle.vehicleid



--QUERY COM SegundaFeira definida


select distinct vehicle.vehicleid,
Coalesce (location.name, vehicle.vehicle_initialLocation) as LOCATION
from vehicle inner join ship on vehicle.vehicleid = ship.mmsi
full outer join trip on vehicle.vehicleid = trip.vehicleid
full outer join stops on trip.id = stops.tripid
full outer join location on stops.locationid = location.id
group by vehicle.vehicleid, location.name, Stops.SeqNum, trip.dataChegada, trip.dataPartida, trip.LastStopSeqNum, vehicle.vehicle_initialLocation
having stops.SeqNum = (select Max(Stops.SeqNum) from stops inner join trip on stops.tripid = trip.id AND trip.vehicleid = vehicle.vehicleid)
    AND  (trip.dataPartida <(TO_DATE('2021.12.06','YYYY.MM.DD')) AND trip.dataChegada<=(TO_DATE('2021.12.06','YYYY.MM.DD')))
    OR   (trip.dataPartida >(TO_DATE('2021.12.06','YYYY.MM.DD')) AND trip.LastStopSeqNum < MAX(trip.LastStopSeqNum) )
    OR   (vehicle.vehicleid NOT IN (select distinct vehicle.vehicleid from vehicle inner join trip on vehicle.vehicleid = trip.vehicleid))
order by vehicle.vehicleid




---Função

CREATE OR REPLACE FUNCTION get_available_ships (
    monday Trip.dataChegada%type
)
RETURN SYS_REFCURSOR
IS
    ships_info SYS_REFCURSOR;
    
BEGIN

IF monday = null THEN
RETURN NULL;

ELSE
    OPEN ships_info FOR
        select distinct vehicle.vehicleid,
Coalesce (location.name, vehicle.vehicle_initialLocation) as LOCATION
from vehicle inner join ship on vehicle.vehicleid = ship.mmsi
full outer join trip on vehicle.vehicleid = trip.vehicleid
full outer join stops on trip.id = stops.tripid
full outer join location on stops.locationid = location.id
group by vehicle.vehicleid, location.name, Stops.SeqNum, trip.dataChegada, trip.dataPartida, trip.LastStopSeqNum, vehicle.vehicle_initialLocation
having stops.SeqNum = (select Max(Stops.SeqNum) from stops inner join trip on stops.tripid = trip.id AND trip.vehicleid = vehicle.vehicleid)
    AND  (trip.dataPartida <(monday) AND trip.dataChegada<=(monday))
    OR   (trip.dataPartida >(monday) AND trip.LastStopSeqNum < MAX(trip.LastStopSeqNum) )
    OR   (vehicle.vehicleid NOT IN (select distinct vehicle.vehicleid from vehicle inner join trip on vehicle.vehicleid = trip.vehicleid))
order by vehicle.vehicleid;
       END IF;
    RETURN ships_info;
END;
/


--CALL FUNCTION

begin 

    DECLARE
    info SYS_REFCURSOR; 
    vehicle_id Vehicle.vehicleId%TYPE;
    ship_location location.name%TYPE;
    

    begin

        info := get_available_ships(TO_DATE('2021.12.06','YYYY.MM.DD'));
        loop
            fetch info 
            into vehicle_id, ship_location;
            exit when info%notfound;
        dbms_output.put_line('Id: ' || vehicle_id || ' Location: ' || ship_location);
        end loop;
        
        CLOSE info;
    end;
end;
/




