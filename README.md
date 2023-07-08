This project will consist of:
 - code to read in data from datastore, typically CSVs.
    - can abstract data access with Repository
 - business logic specific (market specific) code to transform the data into different formats for consumption
    - ex: daily price -> annual return
 - code to calculate statistical measures
    ex: volatility
 - code to perform analysis on the consumable data (pretty data) using the code to perform statistical measures
    ex: find all etfs that have higher return than s&p 500 with lower volatility over a long time period
 - (maybe in the future) code to visualize the results of an analysis


Because the datasets are large, it's likely not everything can be read into memory. The code that consumes the data
can consume it as an iterable or stream. 