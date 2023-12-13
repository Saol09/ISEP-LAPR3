# Wiki

This serves as a guide for explaining team decisons regarding the relevant details of the database elements as well as a justification for them.

## Entities

### Container

This entity serves as a representation of a container, containing all the estipulated attributes for it, like the Container Id, its payload , tare, gross,temperature, ISO code and max volume.

### Vehicle

This entity regards the means of transportation of the containers, acting as a parent entity for Truck and Ship, both being the vehicles of transportation of the containers.

#### Truck

To the moment, only a few information was given regarding the trucks, being capable of transporting only one container at the time. Trucks are distinguished by its vehicle id being its plate. 

#### Ship

Regarding this entity, the ships contain a lot of information. This entity stores the vehicle id (MMSI), name, IMO id and its technical informations.

##### Vessel Type

Contains the code of the type of the ship.

##### Dynamic Data

Contains the dynamic data of the ship and the transceiver id.

### Cargo

Where the container is transported, it contains the id of the cargo, the weight and information regarding the locating of the containers in the ship.

#### Unloading Cargo Manifest

Delivered to the warehouse/port operations team.

#### Loading Cargo Manifest 

Delivered to the ship crew.

### User

Contains the information regarding the users registered/using the platform.

#### Port/Warehouse Staff

Someone who loads and unloads cargo from ships/warehouses.

### Trip

Represents the trip of the cargo from one port to another, it contain the id, the ports and the corresponding arrival dates.


## Relationships

### Container - Cargo

- One-to-Many: One container can be transported in different cargos

### Vehicle - Cargo

-One-to-Many: A vehicle can transport one or multiple cargo

### Trip- PortWarehouse

- One-to-One: One trip has its origin and arrival at a PortWarehouse

### Cargo - Trip

- One-to-Many: A cargo can make multiples trips from various ports

### UnloadindCargoManifest/LoadindCargoManifest - Trip

- One-to-One: The cargo did or will realize a trip

### UnloadindCargoManifest/LoadindCargoManifest - Container

- One-to-Many: The various containers are loaded and unloaded 

### UnloadingCargoManisfest - PortWarehouseOperationsTeam

- One-to-One: The cargo is delivered to the ports operation team

### LoadingCargoManisfest - Ship

- One-to-Many: Various cargos are loaded into the ship.

### Ship - DynamicData

- One-to-Many: during a shipment, a ship has various dynamic datas.

### Ship - Vessel Type

- One-to-One: a ship only has one vessel type, numerically coded.

### PortWarehouse - PortWarehouseOperationsTeam

- One-to-Many: a PortWarehouse has at least one PortWarehouseOperationsTeam.

### PortWarehouseOperationsTeam - PortWarehouseStaff

-One-to-Many: a operation team is composed by staff



## Annotations

- The distinction between a refrigerated container an a normal one is seen by the attribute "Temperature" where, if is refrigerated, this value will be an integer and, if it's not, this valie will be null.



