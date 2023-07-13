package com.pipelinepowertool.common.pipelineplugin.energy_meter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.atomic.AtomicReference;

public class EnergyMeterServiceImpl implements EnergyMeterService {

    private static final String ENERGY_READER_URL = "https://energy-reader.s3.eu-north-1.amazonaws.com/energy_reader";

    private final AtomicReference<Process> process = new AtomicReference<>();

    @Override
    public void start() throws IOException {
        URL url = new URL(ENERGY_READER_URL);
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("energy_reader");
            FileChannel channel = fileOutputStream.getChannel()) {
            channel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
        File file = new File("energy_reader");
        file.setExecutable(true);
        ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath());
        pb.directory(file.getParentFile());
        process.set(pb.start());
    }

    @Override
    public void stop() {
        process.get().destroy();
    }
}
