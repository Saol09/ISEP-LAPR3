
---FUNCAO ALL SHIPS (VOU USAR)
CREATE OR REPLACE FUNCTION get_allShips
  RETURN SYS_REFCURSOR
AS
  my_cursor SYS_REFCURSOR;
BEGIN
  OPEN my_cursor FOR SELECT Ship.MMSI FROM Ship;
  RETURN my_cursor;
END;

--------------------

---FUNCAO PRINCIPAL
CREATE OR REPLACE FUNCTION get_daysIDLE (dataAtual TIMESTAMP, p_ship SHIP.MMSI%type)
  RETURN INTEGER
AS
  diasDesdeInicioAno INTEGER;
  diasTrabalho INTEGER;
  diasParadoEmStops INTEGER;
  v_diasIDLE INTEGER;
BEGIN 

--data atual (parametro) - o primeiro dia do ano DA DATA QUE O UTILIZADOR MANDOU POR PARAMETRO
--dias do ano ate agora
SELECT trunc(dataAtual) - (SELECT trunc(dataAtual,'YEAR') from dual) INTO diasDesdeInicioAno FROM dual;


--get dias de trabalho
select sum(trunc(Trip.dataChegada) - trunc(Trip.dataPartida)) into diasTrabalho 
                               from Trip
                               inner join Vehicle on Trip.vehicleID = Vehicle.vehicleID
                               where Vehicle.vehicleID = p_ship;
                               
--get dias parado nas stops
select sum(trunc(Stops.dataPartida) - trunc(Stops.dataChegada)) into diasParadoEmStops
                               from Stops
                               inner join Trip on Stops.tripID = Trip.id
                               inner join Vehicle on Trip.vehicleID = Vehicle.vehicleID
                               where Vehicle.vehicleID = p_ship;



--diasIDLE
--SELECT diasDesdeInicioAno - diasTrabalho INTO v_diasIDLE FROM DUAL; -- diasDesdeIniciono - diasDeTrabalho = diasIDLE

--v_diasIDLE := diasDesdeInicioAno - diasTrabalho; -- diasDesdeIniciono - diasDeTrabalho = diasIDLE

v_diasIDLE := diasDesdeInicioAno - diasTrabalho + diasParadoEmStops; -- diasDesdeIniciono - diasDeTrabalho - diasParadoEmStops = diasIDLE

RETURN v_diasIDLE;

END;
  
  

------------ CHAMAR FUNCAO PRINCIPAL


begin 

DECLARE

allShips SYS_REFCURSOR;
v_ship Ship.MMSI%TYPE;
v_diasIDLE INTEGER;
v_diasDesdeInicioAno INTEGER;
begin
    
    SELECT trunc(TO_DATE('2021.04.25','YYYY.MM.DD')) - (SELECT trunc(TO_DATE('2021.04.25','YYYY.MM.DD'),'YEAR') from dual) INTO v_diasDesdeInicioAno FROM dual;
    
    --loop through all days of month
    allShips := get_allShips();
    LOOP
    FETCH allShips INTO v_ship;
    exit when allShips%notfound;
    
    v_diasIDLE := get_daysIDLE(TO_DATE('2021.04.25','YYYY.MM.DD'), v_ship);
    
    IF v_diasIDLE IS NULL THEN
    dbms_output.put_line('DIAS IDLE: ' || v_diasDesdeInicioAno || '  SHIP: ' || v_ship || '    (0 TRIPS)');
    
    ELSE
    dbms_output.put_line('DIAS IDLE: ' || v_diasIDLE || '  SHIP: ' || v_ship);
    
    end if;
    end loop;
    
    close allShips;
    
end;
end;
/





