package model;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Map;

/**
 * @author Orel Gershonovich
 * @since 15-July-2022
 */

public class File {

    private int id;
    private String fileName;
    private String size;
    private String length;
    private Boolean isValid;

    public File() {
    }

    public File(int id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public double getFileSize(java.io.File listOfFile) {
        long bytes = listOfFile.length();
        double kilobytes = (bytes / 1024.0);
        return kilobytes / 1024;
    }

    public String getFileLength(java.io.File path) {
        try {
            return getDurationWithMp3Spi(path);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDurationWithMp3Spi(java.io.File file) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
        if (fileFormat instanceof TAudioFileFormat) {
            Map<String, Object> properties = fileFormat.properties();
            String key = "duration";
            Long microseconds = (Long) properties.get(key);
            int mili = (int) (microseconds / 1000);
            int sec = (mili / 1000) % 60;
            int min = (mili / 1000) / 60;
            System.out.println("time = " + min + ":" + sec);
            return min + ":" + (sec < 10 ? "0" + sec : sec);
        } else {
            throw new UnsupportedAudioFileException();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
