# Nebula Exchange for China Mobile
Nebula Exchange is an Apache Spark application. It is used to migrate cluster data in hive or csv to Nebula Graph or migrate algo result into hive. 

## How to Compile

    ```bash
    $ cd mobile-graph-compute/nebula-exchange
    $ mvn clean package -Dmaven.test.skip=true -Dgpg.skip -Dmaven.javadoc.skip=true
    ```
   
## How to use

read from hive:
```
$SPARK_HOME/bin/spark-submit --class com.mobile.graph.compute.reader.HiveReader  nebula-exchange-1.0.jar "{\"hdfs\":\"hdfs://192.168.8.149:9000/\",\"connection\":{\"waredir\":\"hdfs://CM-149:9000/user/vesoft/hive/warehouse\",\"connectionURL\":\"jdbc:mysql://192.168.8.149:3306/metastore?createDatabaseIfNotExist=TRUE\",\"driverName\":\"com.mysql.cj.jdbc.Driver\",\"userName\":\"nebula\",\"password\":\"Nebula@123\"},\"sql\":\"select id,name from default.person\"}"
```

migrate data from Hive to Nebula
```
$SPARK_HOME/bin/spark-submit --class com.vesoft.nebula.exchange.Exchange nebula-exchange-1.0.jar -c /path/to/application.conf -h
```