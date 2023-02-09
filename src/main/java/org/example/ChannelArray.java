package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ChannelArray {
    private String pathFile;
    private ByteBuffer buffer;
    private IntBuffer intBuffer;
    private int currentPositionFile = 0;
    private int sizeFile;
    private List<int[]> listArr = new ArrayList<>();
    private List<String> listString = new ArrayList<>();

    public ChannelArray(String pathFile, int sizeBuffer) {
        this.pathFile = pathFile;
        this.buffer = ByteBuffer.allocate(sizeBuffer);
        this.intBuffer = IntBuffer.allocate(sizeBuffer);
        installSizeFile();
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public IntBuffer getIntBuffer() {
        return intBuffer;
    }

    public void setIntBuffer(IntBuffer intBuffer) {
        this.intBuffer = intBuffer;
    }

    public int getCurrentPositionFile() {
        return currentPositionFile;
    }

    public void setCurrentPositionFile(int currentPositionFile) {
        this.currentPositionFile = currentPositionFile;
    }

    public int getSizeFile() {
        return sizeFile;
    }

    public void setSizeFile(int sizeFile) {
        this.sizeFile = sizeFile;
    }

     public void incrementCurrentPositionFile() {
        currentPositionFile = currentPositionFile + buffer.position();
    }

    public void readPartData() {
        try (FileChannel channel = FileChannel.open(Path.of(pathFile), StandardOpenOption.READ)) {
//            this.buffer.clear();
//            this.intBuffer.clear();
            this.buffer = ByteBuffer.allocate(this.buffer.capacity());
            this.intBuffer = IntBuffer.allocate(this.intBuffer.capacity());
            channel.read(this.buffer, currentPositionFile);
            ByteBuffer byteBuffer = this.buffer;
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                this.intBuffer.put((char) byteBuffer.get());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void installSizeFile() {
        try (FileChannel channel = FileChannel.open(Path.of(pathFile), StandardOpenOption.READ)) {
            this.sizeFile = (int) channel.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
