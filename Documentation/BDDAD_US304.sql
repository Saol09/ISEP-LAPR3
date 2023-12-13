--CREATE TABLE
 
 -- ## tabela AuditTrails ##
CREATE TABLE AuditTrails (
    user_name        VARCHAR(255),
    dateOfOperation  TIMESTAMP,
    typeOfOperation  VARCHAR(255),    
    containerID      INTEGER,
    cargoManifestID  INTEGER,
    old_containerID      INTEGER,
    old_cargoManifestID  INTEGER

);


--CREATE TRIGGER
CREATE or replace TRIGGER container_audit_trails
after INSERT OR UPDATE OR DELETE ON Container_CargoManifest
    FOR EACH ROW
DECLARE
v_username VARCHAR(255);
v_container_id INTEGER := :new.container_id;
v_cargoManifest_id INTEGER := :new.cargoManifestID;
v_old_container_id INTEGER := :old.container_id;
v_old_cargoManifest_id INTEGER := :old.cargoManifestID;
pragma autonomous_transaction;
BEGIN
SELECT user INTO v_username
   FROM dual;
case
        --INSERTING
        when inserting then
            INSERT INTO auditTrails(user_name, dateOfOperation, typeOfOperation, containerID, cargoManifestID) VALUES (v_username, TO_DATE(sysdate, 'dd/mm/yyyy hh24:mi:ss'), 'INSERT', v_container_id, v_cargoManifest_id);
		--UPDATING
        when updating then
            INSERT INTO auditTrails(user_name, dateOfOperation, typeOfOperation, containerID, cargoManifestID, old_containerID, old_cargoManifestID) VALUES (v_username, TO_DATE(sysdate, 'dd/mm/yyyy hh24:mi:ss'), 'UPDATE', v_container_id, v_cargoManifest_id, v_old_container_id, v_old_cargoManifest_id);
        --DELETE
        when deleting then
            INSERT INTO auditTrails(user_name, dateOfOperation, typeOfOperation, containerID, cargoManifestID) VALUES (v_username, TO_DATE(sysdate, 'dd/mm/yyyy hh24:mi:ss'), 'DELETE', v_old_container_id, v_old_cargoManifest_id);
    end case;
COMMIT;
END;



--INSERT OPERATION
INSERT INTO Container_CargoManifest (container_id, cargoManifestId, position_x, position_y, position_z, weigth)
    VALUES (4, 1, 12, 12, 21, 200.00);
    
--UPDATE OPERATION
UPDATE Container_CargoManifest
SET container_id = 5, cargoManifestId=1
WHERE container_id=4 AND cargoManifestId=1;


----DELETE OPERATION
DELETE FROM Container_CargoManifest WHERE container_id=5 AND cargoManifestId=1;


--ACEDER AOS REGISTOS DE ESCRITA 
SELECT * FROM AuditTrails ORDER BY dateOfOperation, containerid, cargoManifestID;



DROP TABLE AuditTrails
