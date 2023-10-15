ALGORITHM :
1. Load the input data from the input.txt file in HDFS and split it into lines.
2. Tokenize each line into individual words.
3. Group the words together and count the occurrences of each word.
4. Store the word count result in HDFS in a directory named word_count_output.
5. Display the word count result using the DUMP command.


-- Load the input data from HDFS
input_data = LOAD 'input.txt' USING PigStorage(' ') AS (line:chararray);


-- Tokenize each line into words
words = FOREACH input_data GENERATE FLATTEN(TOKENIZE(line)) AS word;


-- Group and count the words
word_counts = GROUP words BY word;
word_count_result = FOREACH word_counts GENERATE group AS word, COUNT(words) AS count;


-- Store the word count result in HDFS
STORE word_count_result INTO 'word_count_output';


-- Display the result
DUMP word_count_result;