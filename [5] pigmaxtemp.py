IMPLEMENT THE PIG LATIN SCRIPTS TO FIND A MAX TEMP FOR EACH AND EVERY YEAR

Algorithm
1. Load the input data from the 'input.txt' file in HDFS, assuming it's tab-separated and has two columns: date and temperature.
2. Extract the year from the date using the SUBSTRING function. Adjust the substring function according to your date format if needed.
3. Group the data by year using the GROUP operation.
4. Calculate the maximum temperature for each year using the MAX function.
5. Store the result in HDFS in a directory named 'max_temp_per_year'.
6. Display the result using the DUMP command.


-- Load the input data from HDFS, assuming the data has two columns: date and temperature 
input_data = LOAD 'input.txt' USING PigStorage('\t') AS (date:chararray, temperature:int);


-- Extract the year from the date (assuming date format is 'yyyy-MM-dd')
yearly_data = FOREACH input_data GENERATE SUBSTRING(date, 0, 4) AS year, temperature;


-- Group the data by year
yearly_grouped = GROUP yearly_data BY year;


-- Compute the maximum temperature for each year
max_temperatures = FOREACH yearly_grouped GENERATE group AS year, MAX(yearly_data.temperature) AS max_temp;


-- Store the result in HDFS
STORE max_temperatures INTO 'max_temp_per_year'; 


-- Display the result
DUMP max_temperatures;