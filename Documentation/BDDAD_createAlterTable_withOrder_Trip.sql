drop table container CASCADE CONSTRAINTS PURGE;
drop table Vehicle CASCADE CONSTRAINTS PURGE;
drop table Truck CASCADE CONSTRAINTS PURGE;
drop table VesselType CASCADE CONSTRAINTS PURGE;
drop table Ship CASCADE CONSTRAINTS PURGE;
drop table ShipCaptain_Ship CASCADE CONSTRAINTS PURGE;
drop table ShipCaptain CASCADE CONSTRAINTS PURGE;
DROP TABLE Location CASCADE CONSTRAINTS PURGE;
DROP TABLE SystemUser CASCADE CONSTRAINTS PURGE;
drop table Transceiver CASCADE CONSTRAINTS PURGE;
drop table DynamicData CASCADE CONSTRAINTS PURGE;
DROP TABLE Trip CASCADE CONSTRAINTS PURGE;
DROP TABLE Port CASCADE CONSTRAINTS PURGE;
DROP TABLE Warehouse CASCADE CONSTRAINTS PURGE;
DROP TABLE Container_CargoManifest CASCADE CONSTRAINTS PURGE;
DROP TABLE Stops CASCADE CONSTRAINTS PURGE;
DROP TABLE CargoManifest CASCADE CONSTRAINTS PURGE;
DROP TABLE "ORDER" CASCADE CONSTRAINTS PURGE;
drop table Client CASCADE CONSTRAINTS PURGE;
DROP TABLE ORDER_TRIP CASCADE CONSTRAINTS PURGE;

-- ## tabela CLIENT ##
CREATE TABLE Client (
id   	            number(10)        	    CONSTRAINT pk_clientId     		        PRIMARY KEY,                            
clientName       	varchar(255)     	    CONSTRAINT nn_clientName         		NOT NULL
);

-- ## tabela Container_Leasing ##
CREATE TABLE "ORDER" (
order_id            integer                     constraint pk_order_id                  primary key,
client_id   	    number(10)        	        CONSTRAINT nn_client_id     		    NOT NULL,                            
container_id 	    INTEGER     	            CONSTRAINT nn_container_id         		NOT NULL,
order_date                timestamp             constraint nn_date                      not null,
order_state               varchar(255)          constraint nn_order_state               not null,   
                                                constraint ck_order_state               CHECK(REGEXP_LIKE(order_state, 'LOADED||IN-PROGRESS||FINISHED'))
);


-- ## tabela CONTAINER ##
CREATE TABLE Container (
containerID   	INTEGER        	    CONSTRAINT pk_containerId     		PRIMARY KEY,                            
payload       	INTEGER     	    CONSTRAINT nn_payload         		NOT NULL,
tare  			INTEGER             CONSTRAINT nn_tare    				NOT NULL,
gross  			INTEGER             CONSTRAINT nn_gross    				NOT NULL, 
temperature  	INTEGER   		    CONSTRAINT nn_temperature    		NOT NULL, 
ISOCode			varchar(4)  		CONSTRAINT nn_isoCode				NOT NULL,    
  									CONSTRAINT ck_isoCode				CHECK(REGEXP_LIKE(ISOCode, '^\d{2}[A-Z]{2}$|^\d{2}[A-Z]\d$')),
checkDigit      INTEGER             CONSTRAINT nn_checkDigit            NOT NULL,
maxVolume       INTEGER             CONSTRAINT nn_maxVolume             NOT NULL,
container_type            varchar(255)             constraint nn_type                  not null
);

-- ## tabela Vehicle ##
CREATE TABLE Vehicle (
vehicleID   	        VARCHAR(255)        CONSTRAINT pk_vehicleId     		PRIMARY KEY,
vehicle_initialLocation VARCHAR(255)        CONSTRAINT nn_vehicleIL             NOT NULL,
vehicle_initialLocation_ID INTEGER        CONSTRAINT nn_vehicleILID             NOT NULL 
);

-- ## tabela Truck ##
CREATE TABLE Truck (
truckID   	            VARCHAR(255)        CONSTRAINT pk_truckId     		        PRIMARY KEY,
                                            CONSTRAINT ck_truckId                   CHECK(REGEXP_LIKE(truckId,'^\d{2}-[A-Z]{2}-\d{2}$|^\d{2}-\d{2}-[A-Z]{2}$|^[A-Z]{2}-\d{2}-\d{2}$'))
);

-- ## tabela VesselType ##
CREATE TABLE VesselType (
code   	                INTEGER        	        CONSTRAINT pk_vesselTypeId     		    PRIMARY KEY,
shipType                VARCHAR(255)            CONSTRAINT nn_shipType                  NOT NULL                              
);


-- ## tabela Ship ##
CREATE TABLE Ship (
MMSI   	                VARCHAR(9)          CONSTRAINT pk_shipId     		    PRIMARY KEY,
name                    VARCHAR(255)        CONSTRAINT nn_shipName              NOT NULL,
IMOId                   NUMBER(7)           CONSTRAINT nn_imoId                 NOT NULL,
energyGenerator         INTEGER,             
generatorsPowerOutput   INTEGER,
callSign                VARCHAR(255)        CONSTRAINT nn_callSign              NOT NULL,
length                  NUMBER(5,2)                                                             DEFAULT 0.00,
width                   NUMBER(5,2)                                                             DEFAULT 0.00,
capacity                INTEGER                                                                 DEFAULT 0,
vesselTypeCode          INTEGER                                                                 DEFAULT 0,
LoadingCargoManifestRegistry INTEGER
);


-- ## tabela ShipCaptain_Ship ##
CREATE TABLE ShipCaptain_Ship (
shipCaptainid         VARCHAR(255),
shipMMSI             VARCHAR(9),

CONSTRAINT pk_shipCaptain_shiptId PRIMARY KEY(shipCaptainid,shipMMSI)
);

-- ## tabela ShipCaptain ##
CREATE TABLE ShipCaptain (
id                  VARCHAR(255)    CONSTRAINT pk_captainId    		    PRIMARY KEY,
name                VARCHAR(255)
);

-- ## tabela PortWarehouse ##
CREATE TABLE Location (
id   	                INTEGER             CONSTRAINT pk_portWarehouseId     	            PRIMARY KEY,
name                    VARCHAR(255)        CONSTRAINT nn_name                              NOT NULL,
continent               VARCHAR(255)        CONSTRAINT nn_continent                         NOT NULL,
country                 VARCHAR(255)        CONSTRAINT nn_contry                            NOT NULL,
latitude                NUMBER(2,1)        CONSTRAINT nn_portWarehouseLatitude             NOT NULL,
                                            CONSTRAINT ck_portWarehouseLatitude             CHECK(latitude<=90.0 AND latitude >= 0.0),
longitude               NUMBER(2,1)        CONSTRAINT nn_portWarehouseLongitude            NOT NULL,
                                            CONSTRAINT ck_portWarehouseLongitude            CHECK(longitude<=180.0 AND longitude >= 0.0),
capacity                NUMBER              constraint nn_location_capacity                 not null,
occupancy               NUMBER              constraint nn_location_occupancy                 not null
);

CREATE TABLE Port   (
portId                      INTEGER             CONSTRAINT pk_portId                PRIMARY KEY,
dockingAreaCapacity         INTEGER             CONSTRAINT nn_DAcapacity            NOT NULL
);

CREATE TABLE Warehouse   (
warehouseId                      INTEGER             CONSTRAINT pk_warehouseId                PRIMARY KEY
);

create table Stops(
tripId                  INTEGER             constraint nn_tripId    not null,
locationId              Integer             constraint nn_locationId    not null,
seqNum                  integer              constraint nn_seqNum     not null,
unloadingCargoManifestId    integer,
loadingCargoManifestId  integer,
dataChegada             TIMESTAMP             constraint nn_stops_dataChegada not null,
dataPartida             TIMESTAMP,


CONSTRAINT pk_stopsId PRIMARY KEY(tripId,locationId)

);

-- ## tabela User ##
CREATE TABLE SystemUser   (
id                      INTEGER             CONSTRAINT pk_userId                PRIMARY KEY,
name                    VARCHAR(255)        CONSTRAINT nn_userName              NOT NULL
);

-- ## tabela Transceiver ##
CREATE TABLE Transceiver   (
id                      INTEGER             CONSTRAINT pk_transceiverId                PRIMARY KEY
);

-- ## tabela DynamicData ##
CREATE TABLE DynamicData   (
shipMMSI                VARCHAR(9),
dateTime                TIMESTAMP,   
latitude                NUMBER(4,1)         CONSTRAINT nn_dynamicDataLatitude               NOT NULL,
                                            CONSTRAINT ck_dynamicDataLatitude               CHECK(latitude<=90 AND latitude >= 0),
longitude               NUMBER(4,1)         CONSTRAINT nn_dynamicDataLongitude              NOT NULL,
                                            CONSTRAINT ck_dynamicDataLongitude              CHECK(longitude<=180 AND longitude >= 0),
sog                     NUMBER(5,1)         CONSTRAINT nn_sog                               NOT NULL,
cog                     NUMBER(5,2)         CONSTRAINT nn_cog                               NOT NULL,
                                            CONSTRAINT ck_cog                               CHECK(cog>=0 AND cog<360),
heading                 NUMBER(5,2)                                                                         DEFAULT 511,
                                            CONSTRAINT ck_heading                           CHECK((heading>=0 AND heading<360) OR heading = 511),
position                INTEGER,
transceiverId           INTEGER             CONSTRAINT nn_transceiverId                     NOT NULL,

CONSTRAINT pk_dynamicDataId PRIMARY KEY(shipMMSI,dateTime)
);


-- ## tabela Viagem ##
CREATE TABLE Trip   (
id                      INTEGER             CONSTRAINT pk_viagemId                 PRIMARY KEY,
dataPartida             TIMESTAMP	        CONSTRAINT nn_dataPartida              NOT NULL,
dataChegada             TIMESTAMP	        CONSTRAINT nn_dataChegada              NOT NULL,
vehicleId               VARCHAR(255),
LastStopSeqNum			INTEGER             CONSTRAINT nn_LastStopSeqNum 		   NOT NULL  
);

-- ## tabela CargoManifest ##
CREATE TABLE CargoManifest   (
cargoManifestId                     INTEGER             CONSTRAINT pk_CargoManifestId                      PRIMARY KEY,
cargoManifest_date 					TIMESTAMP 			CONSTRAINT nn_CargoManifestDate				       NOT NULL,
CMTYPE				                VARCHAR(255)	    CONSTRAINT nn_CargoManifestType					   NOT NULL,
                                                        constraint ck_cmType                                CHECK(REGEXP_LIKE(CMTYPE,'U|L'))
);

CREATE TABLE Container_CargoManifest   (
cargoManifestId         INTEGER,
container_id            integer,
position_x              integer,
position_y              integer,
position_z              integer,
weigth                  number(5,2),

CONSTRAINT pk_container_CargoManifestId PRIMARY KEY(cargoManifestId,container_id)
);


CREATE TABLE Order_Trip (
tripID                  INTEGER         CONSTRAINT nn_OT_tripID NOT NULL,
order_ID                  INTEGER         CONSTRAINT nn_OT_orderID NOT NULL,

CONSTRAINT pk_Order_Trip PRIMARY KEY(tripID,order_ID)
);



ALTER TABLE "ORDER"     ADD CONSTRAINT fk_client_id    foreign key (client_id)           references Client(id);
ALTER TABLE "ORDER"     ADD CONSTRAINT fk_container_id    foreign key (container_id)           references container(containerID);

ALTER TABLE Truck                   ADD CONSTRAINT fk_truckID                   FOREIGN KEY (truckID)                   REFERENCES Vehicle(vehicleID);
ALTER TABLE Ship                    ADD CONSTRAINT fk_shipID                    FOREIGN KEY (MMSI)                      REFERENCES Vehicle(vehicleID);
ALTER TABLE Ship                    ADD CONSTRAINT fk_vesselType                FOREIGN KEY (vesselTypeCode)            REFERENCES VesselType(code);
ALTER TABLE DynamicData             ADD CONSTRAINT fk_shipMMSI                  FOREIGN KEY (shipMMSI)                  REFERENCES Ship(MMSI);
ALTER TABLE DynamicData             ADD CONSTRAINT fk_tranceiverId              FOREIGN KEY (transceiverId)             REFERENCES Transceiver(id);
ALTER TABLE Trip                    ADD CONSTRAINT fk_TripvehicleId                 foreign key (vehicleId)                 references Vehicle(vehicleID);
ALTER TABLE Stops                   ADD CONSTRAINT fk_tripId                    foreign key (tripId)                 references Trip(id);
ALTER TABLE Stops                   ADD CONSTRAINT fk_locationId                    foreign key (locationId)                 references Location(id);
ALTER TABLE Port                   ADD CONSTRAINT fk_portId                    foreign key (portId)                 references Location(id);
ALTER TABLE Warehouse                   ADD CONSTRAINT fk_warehouseId                    foreign key (warehouseId)                 references Location(id);

alter table Container_CargoManifest add constraint fk_CCMcargoManifestId    foreign key (cargoManifestId)           references CargoManifest(cargoManifestId);
alter table Container_CargoManifest add constraint fk_CCMcontainer_id    foreign key (container_id)           references container(containerID);


ALTER TABLE ShipCaptain_Ship add constraint fk_SCShipMMSI        foreign key (ShipMMSI)             references Ship(MMSI);
ALTER TABLE ShipCaptain_Ship add constraint fk_SCCaptainId        foreign key (shipCaptainid)             references ShipCaptain(id);

alter table Order_Trip add constraint fk_OT_tripID    foreign key (tripID)           references Trip(ID);
alter table Order_Trip add constraint fk_OT_order_ID    foreign key (order_id)           references "ORDER"(order_id);
