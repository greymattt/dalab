# Create a table named 'employees' with two column families 'personal' and 'professional' by executing the following command:
create 'employees', 'personal', 'professional'


# Insert data into the table by executing the following command: 
put 'employees', '1', 'personal:name', 'John Doe'
put 'employees', '1', 'personal:age', '30'
put 'employees', '1', 'professional:designation', 'Software Engineer' 
put 'employees', '1', 'professional:salary', '80000'


# To update the data, use the following command: 
put 'employees', '1', 'personal:age', '31'


# To delete a specific column, use the following command
delete 'employees', '1', 'personal:age'


# To delete a row, use the following command:
deleteall 'employees', '1'


# To delete the entire table, use the following command:
disable 'employees' drop 'employees'