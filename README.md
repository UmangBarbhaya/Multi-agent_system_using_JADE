Taxi Service Multi-agent Systems using JADE
---
##### Author:
Umang Barbhaya

---
#### Process Followed:
Below is the process to run this Application

1. Keep the content of src folder (/auto) inside your jade folder structure
2. Modify the InitiatorAgent.java and give your Platform name and Machine address
3. Open cmd and go to the jade folder
4. Compile all the file using command \
	javac -classpath lib\jade.jar -d classes src\auto\taxi\*.java
5. Run the BaseAgent using below command (Modify the platform and machine name here too) \
java -cp lib\jade.jar;classes jade.Boot -gui -local-port 1111 -local-host umang -mtp jade.mtp.http.MessageTransportProtocol(http://UMANG:2221/acc) -agents Base1:auto.taxi.BaseAgent1 \
java -cp lib\jade.jar;classes jade.Boot -gui -local-port 1112 -local-host umang -mtp jade.mtp.http.MessageTransportProtocol(http://UMANG:2222/acc) -agents Base2:auto.taxi.BaseAgent2 \
java -cp lib\jade.jar;classes jade.Boot -gui -local-port 1113 -local-host umang -mtp jade.mtp.http.MessageTransportProtocol(http://UMANG:2223/acc) -agents Base3:auto.taxi.BaseAgent3 \
java -cp lib\jade.jar;classes jade.Boot -gui -local-port 1114 -local-host umang -mtp jade.mtp.http.MessageTransportProtocol(http://UMANG:2224/acc) -agents Base4:auto.taxi.BaseAgent4 \
java -cp lib\jade.jar;classes jade.Boot -gui -local-port 1115 -local-host umang -mtp jade.mtp.http.MessageTransportProtocol(http://UMANG:2225/acc) -agents Base5:auto.taxi.BaseAgent5 

6. Run command to start the agents booking the cab \
	java classes\auto\taxi\Main
