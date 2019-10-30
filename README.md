A Flink application project using Scala and SBT.

To run and test your application locally, you can just execute `sbt run` then select the main class that contains the Flink job . 

You can also package the application into a fat jar with `sbt clean assembly`, then submit it as usual, with something like: 

```
mkdir flink_lstm in your home dir
```

deploy jar:

```
 scp -i your_key_file target/scala-2.11/flink-rnn-assembly-0.1-SNAPSHOT.jar your_user_name@spark-cluster-master.newprolab.com:~/flink_lstm
 scp -i your_key_file target/scala-2.11/kafka_client.py your_user_name@spark-cluster-master.newprolab.com:~/flink_lstm
 scp -i your_key_file target/scala-2.11/launchApp.sh your_user_name@spark-cluster-master.newprolab.com:~/flink_lstm
 scp -i your_key_file target/scala-2.11/configureTopics.sh your_user_name@spark-cluster-master.newprolab.com:~/flink_lstm
```

to run flink job on cluster:

```
cd flink_lstm
./launchApp.sh
./kafka_client.py
./configureTopics.sh
```


You can also run your application from within IntelliJ:  select the classpath of the 'mainRunner' module in the run/debug configurations.
Simply open 'Run -> Edit configurations...' and then select 'mainRunner' from the "Use classpath of module" dropbox. 
