# order_fulfilment_simulation
An order fulfilment simulation program


The project is  gradle project that can be run on intellij.


To run the program on the terminal, do the following: 
- cd to the project directory
- run the following ./gradlew run

To run the program using the FIFO dispatch strategy, update the application.properties file in src/main/resources folder,  and set the following :
- app.dispatch-strategy=QUEUE


For using the matched by order strategy set the following

- app.dispatch-strategy=ORDER

