package com.pipelinepowertool.common.pipelineplugin.energy_meter;

import static com.pipelinepowertool.common.pipelineplugin.utils.Constants.ENERGY_READER_FILENAME;
import static com.pipelinepowertool.common.pipelineplugin.utils.Constants.ENERGY_READER_URL;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.atomic.AtomicReference;

public class EnergyMeterServiceImpl implements EnergyMeterService {


    private final AtomicReference<Process> process = new AtomicReference<>();

    @Override
    public void start() throws IOException {
        URL url = new URL(ENERGY_READER_URL);
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(ENERGY_READER_FILENAME);
            FileChannel channel = fileOutputStream.getChannel()) {
            channel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
        File file = new File(ENERGY_READER_FILENAME);
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
