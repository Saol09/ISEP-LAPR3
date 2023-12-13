--QUERY (alterar o select)

select distinct Container_CargoManifest.containerid, Container_CargoManifest.cargoManifestid, trip.id as trip,Container.container_type, Cargo.posicaox, Cargo.posicaoy, Cargo.posicaoz, Container.payload
, location.name as NEXT_PORT
                                                                           from Container_CargoManifest
                                                                           inner join CargoManifest on Container_CargoManifest.cargoManifestId = CargoManifest.cargoManifestId
                                                                           inner join Stops on CargoManifest.CargoManifestId = Stops.UnloadingCargoManifestId
                                                                           inner join Trip on Stops.tripId = Trip.id
                                                                           inner join Container on Container_CargoManifest.containerid = container.containerid
                                                                           inner join Cargo on container.containerid = cargo.containerid
                                                                           inner join Location on stops.locationid=location.id
group by Container_CargoManifest.containerid,Container_CargoManifest.cargoManifestid, trip.id, stops.seqNum, trip.lastStopSeqNum,Container.container_type,Cargo.posicaox,Cargo.posicaoy,Cargo.posicaoz,
Container.payload, location.name
                                                                           having Stops.SeqNum = (trip.lastStopSeqNum + 1)
order by trip.id


---Funcao


CREATE OR REPLACE FUNCTION get_containers_info (
    ship_captain ShipCaptain.id%type
)
RETURN SYS_REFCURSOR
IS
    container_info SYS_REFCURSOR;
    
BEGIN

    OPEN container_info FOR
    
select distinct Container_CargoManifest.containerid, Container_CargoManifest.cargoManifestid, trip.id as trip,Container.container_type, Cargo.posicaox, Cargo.posicaoy, Cargo.posicaoz, Container.payload
, location.name as NEXT_PORT
                                                                           from Container_CargoManifest
                                                                           inner join CargoManifest on Container_CargoManifest.cargoManifestId = CargoManifest.cargoManifestId
                                                                           inner join Stops on CargoManifest.CargoManifestId = Stops.UnloadingCargoManifestId
                                                                           inner join Trip on Stops.tripId = Trip.id
                                                                           inner join Container on Container_CargoManifest.containerid = container.containerid
                                                                           inner join Cargo on container.containerid = cargo.containerid
                                                                           inner join Location on stops.locationid=location.id
                                                                           inner join vehicle on cargo.vehicleid = cargo.vehicleid
                                                                           inner join ship on vehicle.vehicleid = ship.mmsi
                                                                           inner join shipCaptain_ship on ship.mmsi = shipCaptain_ship.shipMMSI
group by Container_CargoManifest.containerid,Container_CargoManifest.cargoManifestid, trip.id, stops.seqNum, trip.lastStopSeqNum,Container.container_type,Cargo.posicaox,Cargo.posicaoy,Cargo.posicaoz,
Container.payload, location.name, shipCaptain_ship.shipCaptainId
                                                                           having Stops.SeqNum = (trip.lastStopSeqNum + 1)
                                                                                  AND shipCaptain_ship.shipCaptainId = ship_captain
order by trip.id;
                                                                       
                                                                           
     RETURN container_info;
END;
/   







--CALL FUNCTION 

begin 

    DECLARE
    info SYS_REFCURSOR; 
    containerID Container.containerId%TYPE;
    cargoManifest_ID Container_CargoManifest.cargoManifestid%TYPE;
    trip_id trip.id%TYPE;
    container_type Container.container_type%TYPE;
    x Cargo.posicaox%TYPE;
    y Cargo.posicaoy%TYPE;
    z Cargo.posicaoz%TYPE;
    payload Container.payload%TYPE;
    location_name location.name%TYPE;
    
    

    begin

        info := get_containers_info(1);
        loop
            fetch info 
            into containerID, cargoManifest_ID,trip_id, container_type, x, y, z, payload, location_name;
            exit when info%notfound;
        dbms_output.put_line('Id: ' || containerID || ' Cargo Manifest ID: ' || cargoManifest_ID || ' Trip ID: ' || trip_id || ' Container_type: ' || container_type || ' X Position: ' || x || ' Y Position: ' || y || ' Z Position: ' || z || ' Payload:' || payload || ' NEXT PORT: ' || location_name);
        end loop;
        
        CLOSE info;
    end;
end;
/

