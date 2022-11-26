CREATE TABLE EVENT (
    event_type varchar(40) NOT NULL,
    user_id varchar(40) NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

CREATE TABLE SELECT_STORE (
    store_id varchar(40) NOT NULL
) INHERITS (EVENT);

CREATE TABLE BACK_STORE (
    store_id varchar(40) NOT NULL
) INHERITS (EVENT);

CREATE TABLE SELECT_PRODUCT (
    store_id varchar(40) NOT NULL,
    product_id varchar(40) NOT NULL
) INHERITS (EVENT);

CREATE TABLE BACK_PRODUCT (
    store_id varchar(40) NOT NULL,
    product_id varchar(40) NOT NULL
) INHERITS (EVENT);

CREATE TABLE ADD_TO_CART (
    store_id varchar(40) NOT NULL,
    product_id varchar(40) NOT NULL
) INHERITS (EVENT);

CREATE TABLE ORDER_PLACED (
    store_id varchar(40) NOT NULL,
    product_ids text NOT NULL
) INHERITS (EVENT);
