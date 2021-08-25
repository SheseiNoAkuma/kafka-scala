## Useful command line commands

### create a producer   
`kafka-console-producer --broker-list localhost:9092 --topic join-01 --property "parse.key=true" --property "key.separator=;"`


### create a consumer
`kafka-console-consumer --bootstrap-server localhost:9092 --from-beginning --topic inner-join-sink --property "print.key=true"`
