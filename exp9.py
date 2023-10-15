# Table creation:
CREATE TABLE IF NOT EXISTS PRODUCT ( id STRING, type STRING, quantity FLOAT ) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' STORED AS TEXTFILE;


# Insertion of Data:
INSERT INTO TABLE product VALUES (101, 'Mobile', 25);


# Alter table:
# Suppose you want to update the "quantity" column for a specific "id." 
# First, create a new dataset with the updated values.
INSERT OVERWRITE TABLE product SELECT id, type, CASE WHEN id = 103 THEN 80 ELSE quantity END AS quantity FROM product;


# Delete a row:
CREATE TABLE product_filtered AS SELECT * FROM product WHERE quantity >= 30;