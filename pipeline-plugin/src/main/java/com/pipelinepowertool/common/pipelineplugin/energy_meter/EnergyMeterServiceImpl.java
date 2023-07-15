package com.pipelinepowertool.common.pipelineplugin.energy_meter;

import static com.pipelinepowertool.common.pipelineplugin.utils.Constants.ENERGY_READER_FILENAME;
import static com.pipelinepowertool.common.pipelineplugin.utils.Constants.ENERGY_READER_URL_DEFAULT;
import static com.pipelinepowertool.common.pipelineplugin.utils.Constants.ENERGY_READER_URL_JENKINS_ALPINE;

import com.pipelinepowertool.common.core.pipeline.Pipeline;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class EnergyMeterServiceImpl implements EnergyMeterService {


    private final AtomicReference<Process> process = new AtomicReference<>();

    @Override
    public void start(Pipeline pipeline) throws IOException {
        String osRelease = getOsRelease();
        URL url = new URL(
            useSpecialInterpreter(osRelease, pipeline)
                ? ENERGY_READER_URL_JENKINS_ALPINE
                : ENERGY_READER_URL_DEFAULT);
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(ENERGY_READER_FILENAME);
            FileChannel channel = fileOutputStream.getChannel()) {
            channel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
        File file = new File(ENERGY_READER_FILENAME);
        file.setExecutable(true);
        ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath());
        pb.directory(file.getParentFile());
        Process start = pb.start();
        System.out.println("Process started");
        process.set(start);
    }

    @Override
    public void stop() {
        if (process.get() == null) {
            System.out.println("Something went wrong. Process cannot be found");
            return;
        }
        process.get().destroy();
    }

    private String getOsRelease() {
        String osRelease = null;
        try {
            Process release = Runtime.getRuntime().exec("cat /etc/os-release");
            BufferedReader r = new BufferedReader(new InputStreamReader(release.getInputStream()));
            String line;
            while ((line = r.readLine()) != null) {
                if (line.contains("NAME=")) {
                    String[] split = line.split("=");
                    osRelease = split[0];
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return osRelease;
    }

    private boolean useSpecialInterpreter(String osRelease, Pipeline pipeline) {
        return (osRelease != null && osRelease.toUpperCase(Locale.ROOT).contains("ALPINE")
            && Pipeline.JENKINS.equals(pipeline));
    }
}
