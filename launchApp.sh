JOB_NAME="${JOB_NAME:-Flink_LSTM_Example}"
FLINK_HOME=${FLINK_HOME:-/opt/flink-1.9.1}
JAR_NAME="${JAR_NAME:-flink-rnn-assembly-0.1-SNAPSHOT.jar}"

"${FLINK_HOME}"/bin/flink run -m yarn-cluster -ynm "${JOB_NAME}" -p 3 ~/flink_lstm/"${JAR_NAME}"
