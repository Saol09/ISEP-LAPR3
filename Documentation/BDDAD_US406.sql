CREATE OR REPLACE FUNCTION occupancy_rate(
    ship_id Ship.MMSI%type,
    manifest_id integer
) 
RETURN number
IS
    occupancy number;
    
BEGIN
    select (select count(*) from Container_CargoManifest m where m.cargoManifestId =  manifest_id)/s.capacity*100
    into occupancy
    from ship s
    where s.MMSI = ship_id;
    return occupancy;
END;
/



----FUNCAO 405
create or replace FUNCTION get_average_occupancy_rate(
    ship_id varchar,
    data_inicial date,
    data_final date
) 
RETURN number
is
    average number:=0.0;
    manifests sys_refcursor;
    manifest_id integer;
    n_container integer;
    n_manifest integer;
BEGIN
    --all manifests between those dates
    open manifests for    
    select cm.cargoManifestId from CargoManifest cm
    inner join stops s on cm.cargoManifestId = s.loadingCargoManifestID
    inner join trip t on s.tripid = t.id
    where t.vehicleId = ship_id and cm.cargoManifest_date between data_inicial and data_final;
    
    --numero de manifestos between those dates
    select count(*) into n_manifest from CargoManifest cm
    inner join stops s on cm.cargoManifestID = s.loadingCargoManifestId
    inner join Trip t on s.tripID = t.ID
    where t.vehicleId = ship_id
    and cm.cargoManifest_date between data_inicial and data_final;
    
    
    loop
        fetch manifests into manifest_id;
        --average := average + occupancy_rate(ship_id,manifest_id);
    exit when manifests%notfound;
    average := average + occupancy_rate(ship_id,manifest_id);
    end loop;
    
    return (average/n_manifest);
    
END;
/

create or replace function get_all_finished_trips_occupancy_over(
    paramDate trip.datachegada%type, ocupancy_param number DEFAULT 60 
)
return SYS_REFCURSOR is info_finished_trips SYS_REFCURSOR;

begin
    open info_finished_trips for 
        select distinct trip.id, vehicle.vehicleid, trip.datachegada, trip.datapartida from trip  inner join vehicle on trip.vehicleid = vehicle.vehicleid where trip.datachegada < paramDate 
        and get_average_occupancy_rate(vehicle.vehicleid,trip.datapartida, trip.datachegada) > ocupancy_param;
    return info_finished_trips;
    exception 
    when no_data_found then return null;
end;
/
declare

info_all_trips_and_ships_associated_called SYS_REFCURSOR;
tripId_called trip.id%type;
vehicleid_called vehicle.vehicleid%type;
trip_data_chegada trip.datachegada%type;
trip_data_partida_called trip.datapartida%type;

begin
    begin
        info_all_trips_and_ships_associated_called := get_all_finished_trips_occupancy_over(TO_DATE('2021.05.15','YYYY.MM.DD'),0);
        loop
            fetch info_all_trips_and_ships_associated_called into tripId_called, vehicleid_called, trip_data_chegada, trip_data_partida_called;
            exit when info_all_trips_and_ships_associated_called%notfound;
            dbms_output.put_line('TRIP_ID:'|| tripId_called ||  '   VEHICLE_ID:'    || vehicleid_called  || '   TRIP DATA CHEGADA:' || trip_data_chegada || '   Trip Data Partida:'     ||  trip_data_partida_called);
        end loop;
        
        close info_all_trips_and_ships_associated_called;
    end;
end;
/
            
            