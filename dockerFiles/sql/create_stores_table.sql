CREATE TABLE STORES (
    id varchar(40) NOT NULL PRIMARY KEY,
    name text NOT NULL,
    categories text NOT NULL
);

INSERT INTO STORES VALUES ('10', 'Home Burgers', '[burger,fast food]'),
    ('20', 'Donde Pepe', '[corn,hot dog,fast food]'),
    ('30', 'El Corral', '[burger,hot dog,fast food]'),
    ('40', 'Papa Johns', '[pizza]'),
    ('50', 'Seratta', '[italian,spaghetti, pizza]'),
    ('60', 'Wing Station', '[wings]'),
    ('70', 'KFC', '[chicken, wings]');
