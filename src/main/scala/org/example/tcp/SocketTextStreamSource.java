package org.example.tcp;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.example.LSTMStreamingApp;

public class SocketTextStreamSource implements SourceFunction<String> {

    private volatile boolean running;

    public SocketTextStreamSource() {
        this.running = true;
    }

    @Override
    public void run(SourceContext<String> context) throws Exception {
        try (SocketConnection conn = LSTMStreamingApp.conn()) {
            String line;

            while (this.running && (line = conn.getReader().readLine()) != null) {
                context.collect(line);
            }
        }
    }

    @Override
    public void cancel() {
        this.running = false;
    }
}
