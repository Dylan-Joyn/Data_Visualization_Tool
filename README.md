1.	Data Visualization Tool
Create an application that consumes and presents data.  This project is going to be a little more free-form and open-ended than prior projects because I would like you to choose your own data, and the way you present the data will depend on the data you choose.  A couple of places you might peruse:  Harvard DataVerse, WorldBank Databank.
However, there will be some minimum standards/requirements:

Data/Logic Layer:
1.	The data must be read in from a file and should come from a legitimate data source.
2.	You need to use Collections in some way.
3.	You need to use Streams to consume/process the data.

Visualization Layer (TablePanel, StatsPanel, ChartPanel, DetailsPanel):
1.	You should have a table showing discrete data elements.  This might not show all the data related to each item, but perhaps just the main points.  There will be a details panel (below) that shows all the details for the item in the table.
2.	You should be able to sort the table by selecting the column to sort on.
3.	You should have toggleable filters (at least three) on the data that allow you to change the data in the display.
4.	You should have a stats table showing aggregate statistics for the data.  (At LEAST three stats)  You can calculate the stats yourself or use an external library such as Apache Commons Math.
5.	You should have some sort of graphical depiction of the data—a chart of some sort.  You can make the chart yourself or use an external library such as JFreeChart.
6.	You should have a details panel, that shows the details of any item selected on the table.

Part One:  
A.	When a console test application is run, the program should read the file, 
1.	Print out the attributes of the first data item in the file.
2.	Print out the attributes of the 10th item in the file.
3.	Display the number of total number of entries in the data.
B.	When a GUI test application is run, the program should display the data in a table:
Start the Application	Data appears in TablePanel.
Start the Application	Application has a clear title.
Start the Application	Columns in table are clearly labeled.


Part Two:
Adds the other GUI features:  The detail panel, the stats panel, and the chart panel.
Test Cases:  
Start the Application	Data appears in TablePanel.
Start the Application	Data appears in StatsPanel.
Start the Application	Image appears in ChartPanel.
Start the Application	Application has a clear title.
Start the Application	Columns in table are clearly labeled.
Start the Application	Chart is clear and clearly labeled.
Select a filter and filter some data out	Once filtered, the data in the TablePanel changes.
Select a filter and filter some data out	Once filtered, the data in the StatsPanel changes.
Select a filter and filter some data out	Once filtered, the image in the ChartPanel changes.
Remove all Filters	TablePanel, ChartPanel, and StatsPanel change back.
Re-Sort the items in the table	Table items change order, but Stats and Chart do not change.
Select a data point in the table	Data for that item appears in the DetailsPanel
Select a different data point	Data for that item appears in the DetailsPanel
