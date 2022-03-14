Compile with
javac -cp "./jgroups-3.6.20.Final.jar":. */*.java

Create Beckend:
java -cp "./jgroups-3.6.20.Final.jar":. -Djava.net.preferIPv4Stack=true -Djgroups.bind_addr=127.0.0.1 backend.Backend

Create Frontend:
java -cp "./jgroups-3.6.20.Final.jar":. -Djava.net.preferIPv4Stack=true -Djgroups.bind_addr=127.0.0.1 frontend.Frontend

Run the Client:
java -cp "./jgroups-3.6.20.Final.jar":. client.Client 5
