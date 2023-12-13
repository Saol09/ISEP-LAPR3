set SERVEROUTPUT on;

--204
create or replace FUNCTION get_container_info(
    container_id container.CONTAINERID%type
) 
RETURN sys_refcursor
is
    container_info sys_refcursor;
    most_recent_manifest sys_refcursor;
    manifest_id cargomanifest.cargomanifestid%type;
    manifest_type cargomanifest.cmtype%type;
BEGIN
    open most_recent_manifest for
        select cm.cargomanifestid,cm.cmtype from cargomanifest cm, container_cargomanifest ccm where ccm.container_id = container_id 
        and ccm.cargomanifestid = cm.cargomanifestid order by cm.cargoManifest_date desc fetch first 1 rows only;
    fetch most_recent_manifest into manifest_id,manifest_type;
    dbms_output.put_line(manifest_id || ' ' || manifest_type);
    if(manifest_type = 'U') then
    dbms_output.put_line('true');
    open container_info for
        select c.CONTAINER_TYPE,l.name from container c, stops s, location l where s.UNLOADINGCARGOMANIFESTID = manifest_id and l.id = s.LOCATIONID;
    else
    open container_info for
        select c.CONTAINER_TYPE,sp.name from container c, stops s,trip t ,ship sp where s.LOADINGCARGOMANIFESTID = manifest_id and s.TRIPID = t.id and sp.MMSI = t.ID;
    end if;
    return container_info;
END;
/

declare   
c_registo sys_refcursor;
tipo varchar(255);
location varchar(255);

begin
    c_registo := get_container_info(1);
    loop
        fetch c_registo into tipo,location;
        exit when c_registo%notfound;
    end loop;
    dbms_output.put_line('Tipo de Contentor: ' ||  tipo || ' Localização: ' || location);
end;
/

--208
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
begin
    declare   
    rate number;
    
    begin
        rate := occupancy_rate('123456789',1);
        dbms_output.put_line('Occupancy Rate: ' ||  rate || '%');
    end;
end;
/

--206
CREATE OR REPLACE FUNCTION container_list_next_port (
    viagem_id Trip.id%type,
    stop_seqnum Stops.seqnum%type
)
RETURN SYS_REFCURSOR
IS
    containers_info SYS_REFCURSOR;
    
BEGIN

    OPEN containers_info FOR
    
        SELECT Container.containerid, Container.payload, Container.container_type
        FROM Trip
        INNER JOIN Stops ON viagem_id = Stops.tripid AND (stop_seqnum+1) = Stops.seqnum
        INNER JOIN CargoManifest ON TO_NUMBER(CargoManifest.cargomanifestid) = Stops.unloadingcargomanifestid
        INNER JOIN Container_CargoManifest ON Container_CargoManifest.cargomanifestid = TO_NUMBER(CargoManifest.cargomanifestid)
        INNER JOIN Container ON Container.containerid = Container_CargoManifest.cargomanifestid;
        
    RETURN containers_info;
END;
/
begin 

    DECLARE
    info SYS_REFCURSOR; 
    cont_id Container.containerid%TYPE; cont_payload Container.payload%TYPE; cont_type Container.container_type%TYPE;

    begin

        info := container_list_next_port(1,1);
        loop
            fetch info 
            into cont_id, cont_payload, cont_type; 
            exit when info%notfound;
            dbms_output.put_line('Id: ' || cont_id || ' Payload: ' || cont_payload || ' Type: ' || cont_type);
        end loop;
        
        CLOSE info;
    end;
end;
/

--207

with cargoManifests_count as (select distinct cargoManifest.cargoManifestId
from cargoManifest inner join stops on cargoManifest.cargoManifestId =stops.unloadingCargoManifestId or cargoManifest.cargoManifestId =stops.loadingCargoManifestId
inner join trip on stops.tripId = trip.Id inner join vehicle on trip.vehicleID = vehicle.vehicleID inner join ship on vehicle.vehicleID = ship.mmsi 
inner join shipCaptain_ship on ship.mmsi = shipCaptain_ship.shipmmsi
where (shipCaptain_ship.shipCaptainId = 1 and extract (year from cargoManifest.cargoManifest_date) = '2021'))
-- vai buscar o numero de cargos manifests associados a stops,  associados a viagens, associados a veiculos que são ships cujo capitao foi o captain com id 1
    
    

select  (Count(Container_CargoManifest.containerId)/Count(cargoManifest.cargoManifestId))as Media_ContentoresCargoManifest, Count(cargoManifest.cargoManifestId) as NUMERO_CARGOS_MANIFEST from 
cargoManifest full outer join Container_CargoManifest on Container_CargoManifest.cargoManifestId = cargoManifest.cargoManifestId where cargoManifest.cargoManifestId in(
select * from cargoManifests_count) -- apresenta a media e o count de cargo Manifests


--209

with cargoManifestLoads as ( select distinct  container_cargomanifest.containerId from container_cargomanifest inner join cargomanifest on container_cargomanifest.cargomanifestid = cargomanifest.cargoManifestId inner join stops
on cargoManifest.cargoManifestId = stops.loadingCargoManifestId inner join trip on stops.tripId = trip.id 
inner join vehicle on trip.vehicleId = vehicle.vehicleID inner join ship on vehicle.vehicleID = ship.mmsi
where ship.mmsi = '123456789'  and cargomanifest.cargoManifest_date <= to_Date('2021.04.25','YYYY.MM.DD')) -- vai buscar o numero de loadings de um barco
,
 cargoManifestsUnloads as (select distinct container_cargomanifest.containerId from container_cargomanifest inner join cargomanifest on container_cargomanifest.cargomanifestid = cargomanifest.cargoManifestId inner join stops
on cargoManifest.cargoManifestId = stops.unloadingCargoManifestId inner join trip on stops.tripId = trip.id 
inner join vehicle on trip.vehicleId = vehicle.vehicleID inner join ship on vehicle.vehicleID = ship.mmsi
where ship.mmsi = '123456789' and cargomanifest.cargoManifest_date <= to_Date('2021.04.25','YYYY.MM.DD')) -- vai buscar o numero de unloadings de um barco


select (((select count(*) from cargoManifestLoads minus (select * from  cargoManifestsUnloads)) / (select ship.capacity from ship))*100) as Ocuppancy_Rate_Given_Moment from dual 

--307
/
CREATE or replace TRIGGER insuficient_warehouse_capacity
    after INSERT or update
    ON Stops 
    FOR EACH ROW
    
        declare 
        l_stop_location_id number;
        s_unloadind_manifest_id number;
        s_loadind_manifest_id number;
        l_capacity number;
        l_occupancy number;
        c_occupancy number;
        
        begin   
        l_stop_location_id := :new.locationId;
        s_unloadind_manifest_id := :new.unloadingCargoManifestId;
        s_loadind_manifest_id := :new.loadingCargoManifestId;
        select l.capacity, l.occupancy into l_capacity,l_occupancy from location l where l.id = l_stop_location_id;--getting the location info
        if(s_unloadind_manifest_id is not null) then--check if there was a unloading
            select count(*) into c_occupancy from Container_CargoManifest m where m.cargoManifestId  = :new.unloadingCargoManifestId;--getting the occupancy of the unloading to the warehouse
            if((l_occupancy+c_occupancy)>l_capacity) then 
                raise_application_error (-20001,'AVISO: Capacidade Insuficiente');
            end if;
            update location set occupancy = occupancy + c_occupancy where ID = l_stop_location_id;
        else
            update location set occupancy = occupancy - c_occupancy where ID = l_stop_location_id;
        end if;
        
        end;
/

--309
/
CREATE or replace TRIGGER ship_availability
    after INSERT ON CargoManifest 
    FOR EACH ROW
    
        declare 
        i_date TIMESTAMP;
        ship_id number;
        c_manifest number;
        begin   
            i_date := :new.cargoManifest_date;
            
            select v.vehicleID into ship_id from Vehicle v
            inner join Stops on Stops.unloadingCargoManifestId = :new.cargoManifestId or Stops.loadingCargoManifestId = :new.cargoManifestId
            inner join Trip on Trip.id = Stops.tripId
            inner join Vehicle on Vehicle.vehicleID = Trip.vehicleId;
            
            
            select count(*) into c_manifest from CargoManifest cm
            inner join Trip on Trip.vehicleid = ship_id
            inner join Stops on Stops.tripid = Trip.id
            inner join CargoManifest on CargoManifest.cargomanifestid = Stops.unloadingCargoManifestId or CargoManifest.cargomanifestid = Stops.loadingCargoManifestId
            where cm.cargoManifest_date = i_date;
            
            if(c_manifest > 0) then 
                raise_application_error (-20001,'AVISO: Ship ocupado nessa data');
            end if;
            
            exception 
            when no_data_found then dbms_output.put_line('No data found!');
            
        end;
/

-- 305
create or replace function routeLeasedContainer(f_orderId "ORDER".order_id%type)
return SYS_REFCURSOR is info_routerLeasedContainer SYS_REFCURSOR;
begin
    open info_routerLeasedContainer for
        with orderLoading as (select location.name, stops.seqnum, cargoManifest.cargoManifest_Date from trip 
        inner join stops on trip.Id = stops.tripId 
        inner join location on stops.locationId = location.id
        inner join cargoManifest on loadingCargoManifestId = cargoManifest.cargoManifestId
        inner join container_cargoManifest on cargoManifest.cargoManifestId = container_cargoManifest.cargoManifestId
        inner join container on container_cargoManifest.container_id = container.containerID 
        inner join "ORDER" on container.containerID ="ORDER".container_id
        where "ORDER".order_id = 1 
        order by cargoManifest.cargoManifest_date  asc),    -- vai buscar a informacao do trajeto feito pelo contentor arrendado
        orderUnloading as (select location.name, stops.seqnum, cargoManifest.cargoManifest_Date from trip 
        inner join stops on trip.Id = stops.tripId 
        inner join location on stops.locationId = location.id
        inner join cargoManifest on unloadingCargoManifestId = cargoManifest.cargoManifestId
        inner join container_cargoManifest on cargoManifest.cargoManifestId = container_cargoManifest.cargoManifestId
        inner join container on container_cargoManifest.container_id = container.containerID 
        inner join "ORDER" on container.containerID ="ORDER".container_id
        where "ORDER".order_id = 1 
        order by cargoManifest.cargoManifest_date  asc) --vai buscar os stops em que foi unloaded
        ,
        allStopsTripLoad as (select distinct location.name, stops.seqNum, cargoManifest.cargoManifest_Date, vehicle.vehicleID  from location 
        inner join stops on location.id = stops.locationId 
        inner join trip on stops.tripId = trip.Id
        inner join vehicle on trip.vehicleId = vehicle.vehicleID
        inner join cargoSlot on vehicle.vehicleID = cargoSlot.vehicleID
        inner join "ORDER" on cargoSlot.order_id = "ORDER".order_id
        inner join cargoManifest on stops.loadingCargoManifestId in cargoManifest.cargoManifestId 
        where "ORDER".order_id = 1) -- vai buscar os stops em que foi loaded
        ,
        allStopsTripUnload as (select distinct location.name, stops.seqNum, cargoManifest.cargoManifest_Date, vehicle.vehicleID  from location 
        inner join stops on location.id = stops.locationId 
        inner join trip on stops.tripId = trip.Id
        inner join vehicle on trip.vehicleId = vehicle.vehicleID
        inner join cargoSlot on vehicle.vehicleID = cargoSlot.vehicleID
        inner join "ORDER" on cargoSlot.order_id = "ORDER".order_id
        inner join cargoManifest on stops.unloadingCargoManifestId in cargoManifest.cargoManifestId     
        where "ORDER".order_id = 1)
        ,
        allStopsTrip as (select * from allStopsTripLoad union select * from allStopsTripUnload ) -- vai buscar todas as stops duma viagem

        select * from allStopsTrip where seqNum <= (select unloa.seqNum from orderUnloading unloa ) and seqNum >= (select loa.seqNum from orderloading loa)
        order by seqNum; -- vai buscar todos os stops do contentor arrendado por ordem de num de paragem
    return info_routerLeasedContainer;
    exception 
    when no_data_found then return null;
end;
/
declare
ex_naoExisteInformacao exception;
info_routerLeasedContainer_called SYS_REFCURSOR;
name_called location.name%type;
seqNum_called stops.seqNum%type;
cargoManifest_date_called cargoManifest.cargoManifest_date%type;
vehicleId_called vehicle.vehicleId%type;
contador number;
begin
    begin
        contador:=1;
        info_routerLeasedContainer_called:= routeLeasedContainer(1);
        loop
            fetch info_routerLeasedContainer_called into name_called, seqNum_called, cargoManifest_date_called, vehicleId_called;
            exit when info_routerLeasedContainer_called%notfound;
            dbms_output.put_line('Paragem Numero:' || contador || '     Name:' || name_called || '      Sequency Number'|| seqNum_called || '   Stop Date:' || cargoManifest_date_called || '   Vehicle:' || vehicleId_called );
            contador:=contador+1;
        end loop;
        
        close info_routerLeasedContainer_called;
    end;
end;    
/

--306
create or replace function getAllOcupancyRateWarehouses
return SYS_REFCURSOR is info_occupancy_rate_warehouses SYS_REFCURSOR;
begin 
    open info_occupancy_rate_warehouses for 
        select location.Id, (location.occupancy/ location.capacity)*100 as occupancyRate  from location 
        where id in (select warehouse.warehouseId from warehouse);
    return info_occupancy_rate_warehouses;
    exception 
    when no_data_found then return null;
end;
/
create or replace function getUnloadsWarehouseLastMonth(f_locationId location.Id%type, f_date cargoManifest.cargoManifest_date%type)
return number is unloads_warehouse number;
begin 
        select count(cargoManifest.cargoManifestId)  into unloads_warehouse from location 
        inner join stops on location.Id = stops.locationId
        inner join cargoManifest on stops.unloadingCargoManifestId = cargoManifest.cargoManifestId  
        where location.id = f_locationId and (cargoManifest.cargoManifest_date >= add_Months(f_date, -1) and  cargoManifest.cargoManifest_date  <= f_date);
        return unloads_warehouse;
    exception 
         when no_data_found then return null;
end;
/
declare 
info_occupancy_rate_warehouses_called SYS_REFCURSOR;
locationId_called location.id%type;
occupancyRate_called number;
numberUnloads_called number;
begin
    begin
        info_occupancy_rate_warehouses_called := getAllOcupancyRateWarehouses();
        loop 
            fetch info_occupancy_rate_warehouses_called into locationId_called, occupancyRate_called;
            exit when info_occupancy_rate_warehouses_called%notfound;
            numberUnloads_called:= getUnloadsWarehouseLastMonth(locationId_called, TO_DATE('2021.05.15','YYYY.MM.DD'));
            dbms_output.put_line('Warehouse Id:' || locationId_called || '  Occupancy Rate:'    || occupancyRate_called ||  '   Number Of Unloading Cargo Manifests:'  || numberUnloads_called);
         end loop;
         
         close info_occupancy_rate_warehouses_called;
    end;
end;
/
--312
create or replace FUNCTION get_leased_container_info(
    client_id integer,
    container_id container.CONTAINERID%type
) 
RETURN sys_refcursor
is
    logged_user_name varchar(20);
    validate_container integer;
    validate_leased_user SYS_REFCURSOR;
    order_id "ORDER".order_id%type;
    order_client_id "ORDER".client_id%type;
    order_container_id "ORDER".container_id%type;
    order_date timestamp;
    order_state "ORDER".order_state%type;
    order_user_name varchar(20);
    container_info sys_refcursor;
    invalid_container_id exception;
    container_not_leased_to_client exception;
BEGIN
    SELECT USER into logged_user_name FROM dual;
    select count(*) into validate_container from container c where c.containerid = container_id;--verfico se existe o contentor
    if( validate_container != 0 ) then -- se existe
    open validate_leased_user for
        select * from "ORDER" o where o.container_id = container_id and (o.order_state = 'LOADED' OR o.order_state = 'IN-PROGRESS' ) order by o.order_date desc 
        fetch first 1 rows only;--vou buscar o ultimo registo do contentor
        fetch validate_leased_user into order_id,order_client_id,order_container_id,order_date,order_state;
        select c.clientName into order_user_name from Client c where c.id = order_client_id;--obten��o do nome do cliente que fez a ultima order do contentor
        if( validate_leased_user%found and order_user_name = logged_user_name) then --se o ultimo registo do contentor est� dado como LOADED ou IN-PROGRESS e se o nome de quem fez a order for igual ao nome de quem est� logado
            container_info := GET_CONTAINER_INFO(container_id);
        else
            raise container_not_leased_to_client;
        end if;
    else    
        raise invalid_container_id;
    end if;
    exception
        when invalid_container_id then
            dbms_output.put_line('10 � invalid container id');
        when container_not_leased_to_client then
            dbms_output.put_line('11 � container is not leased by client');
    return container_info;
END;
/
--405
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
    no_data_found exception;
BEGIN
    --numero de manifestos between those dates
    select count(*) into n_manifest from CargoManifest cm
    inner join stops s on cm.cargoManifestID = s.loadingCargoManifestId
    inner join Trip t on s.tripID = t.ID
    where t.vehicleId = ship_id
    and cm.cargoManifest_date between data_inicial and data_final;
    
    if(n_manifest = 0) then
        raise no_data_found;
    else
    --all manifests between those dates
    open manifests for    
    select cm.cargoManifestId from CargoManifest cm
    inner join stops s on cm.cargoManifestId = s.loadingCargoManifestID
    inner join trip t on s.tripid = t.id
    where t.vehicleId = ship_id and cm.cargoManifest_date between data_inicial and data_final;    
    
    loop
        fetch manifests into manifest_id;
        --average := average + occupancy_rate(ship_id,manifest_id);
    exit when manifests%notfound;
    average := average + occupancy_rate(ship_id,manifest_id);
    end loop;
    
    return (average/n_manifest);
    
    end if;
    
    exception
        when no_data_found then dbms_output.put_line('No data found!');
    
    
END;

/



declare 
average number;
begin
    begin
        average := get_average_occupancy_rate('123456793',TO_DATE('2021.04.21','YYYY.MM.DD'), TO_DATE('2021.05.10','YYYY.MM.DD'));
        dbms_output.put_line('Average ' || average || '%');
    end;
end;
/


--407
CREATE OR REPLACE PROCEDURE necessary_resources_search (
    port_id Location.id%type,
    today_time TIMESTAMP
)
AS
all_cargoManifest SYS_REFCURSOR;
all_container SYS_REFCURSOR;
cm_ids CargoManifest.cargomanifestid%type;
vehicle_identifier Vehicle.vehicleID%type;
container_info Container_CargoManifest%rowtype;
next_week_date TIMESTAMP;
BEGIN
    -- Ir� calcular a data depois de uma semana (a data � dada para propositos de teste)
    next_week_date := NEXT_DAY(today_time, TO_CHAR(today_time, 'DAY'));
    
    -- Recolhe-se os cargo manifest ao porto associado
    OPEN all_cargoManifest FOR
        SELECT DISTINCT cm.cargoManifestId From CargoManifest cm
        INNER JOIN Location ON Location.id = port_id
        INNER JOIN Stops ON Stops.locationId = Location.id
        INNER JOIN CargoManifest ON cm.cargoManifestId = Stops.loadingcargomanifestid OR cm.cargoManifestId = Stops.unloadingcargomanifestid
        ORDER BY cm.cargoManifestId ASC;
       
    
    LOOP
        
        FETCH all_cargoManifest INTO cm_ids;
        EXIT WHEN all_cargoManifest%NOTFOUND;
        dbms_output.put_line('Cargo Manifest id: ' || cm_ids || '   Date: ' || to_char(next_week_date, 'dd/mm/yyyy'));
        
        -- Recolhe-se os containers para cada cargo manifest
        OPEN all_container FOR
            SELECT DISTINCT *
            FROM Container_CargoManifest 
            WHERE Container_CargoManifest.cargomanifestid = cm_ids;
        
        LOOP
        
            FETCH all_container INTO container_info;
            EXIT WHEN all_container%NOTFOUND;
            
            -- Id do veiculo associado ao correspondente cargo manifest do container
            SELECT DISTINCT v.vehicleid INTO vehicle_identifier FROM Vehicle v
            INNER JOIN CargoManifest ON CargoManifest.cargoManifestId = container_info.cargoManifestId
            INNER JOIN Stops ON CargoManifest.cargoManifestId = Stops.loadingcargomanifestid OR CargoManifest.cargoManifestId = Stops.unloadingcargomanifestid
            INNER JOIN Trip ON Trip.id = Stops.tripId
            INNER JOIN Vehicle ON v.vehicleID = Trip.vehicleId;
            
            
            dbms_output.put_line('      Vehicle id:' || vehicle_identifier || '  Container id: ' || container_info.container_id || '  x: ' || container_info.position_x || '  y: ' || container_info.position_y || '  z: ' || container_info.position_z);
        
        END LOOP;
        
        dbms_output.put_line('  Number of containers: ' || all_container%rowcount);
        dbms_output.put_line(' ');
        
        CLOSE all_container;
        
    END LOOP;
   
   CLOSE all_cargoManifest;
   
END;
/
begin

    necessary_resources_search(1, TO_DATE('2021.04.25','YYYY.MM.DD'));
    necessary_resources_search(2, TO_DATE('2021.04.25','YYYY.MM.DD'));
    necessary_resources_search(3, TO_DATE('2021.04.25','YYYY.MM.DD'));
    necessary_resources_search(11, TO_DATE('2021.04.25','YYYY.MM.DD'));
        
end;
/