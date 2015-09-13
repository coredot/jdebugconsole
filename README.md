# JDebug Console
A simple Java GUI for profiling your code and displaying debug messages. Below are some simple examples of how it works.


######RateStat Example
This statistic calculates the sum of the values added per second averaged over a 10 second window.
```
RateStat rateStat = new RateStat("My Rate", 10); // 10 seconds
for (int c=0;c<300;c++) {
    rateStat.add(5); // Add any kind of number.
    Debug.setStatistic(rateStat); // Updates the GUI
    Thread.sleep(100); // Add a delay to simulate workload
}
```
######TotalStat Example
This statistic calculates the total sum of the values added.
```
TotalStat totalStat = new TotalStat("My Total");
for (int c=0;c<300;c++) {
    totalStat.add(1); // Add any kind of number.
    Debug.setStatistic(totalStat); // Updates the GUI
}
```
######TimeStat Example
This statistic calculates the average and accumulative time taken for a given task. The average time is calculated over a 10 second window.
```
TimeStat timeStat = new TimeStat("My Time", 10); // 10 seconds
for (int c=0;c<300;c++) {
    StopWatch watch = timeStat.start(); // Start the stopwatch
    Thread.sleep(100); // Add a delay to simulate workload
    watch.stop(); // Stop the stopwatch.
    Debug.setStatistic(timeStat); // Updates the GUI
}
```
######Debug Message Example
This method prints out messages.
```
Debug.println("A debug message");
```
######Adding JDebug Console to your Maven project
```
<dependency>
    <groupId>com.coredot</groupId>
    <artifactId>jdebugconsole</artifactId>
    <version>1.1.0</version>
</dependency>
```
