package view;

/**
 * @author Orel Gershonovich
 * @since 15-July-2022
 */

public class FileItem {

    private String fileName;
    private String size;
    private String length;

    public FileItem(String fileName, String size, String length) {
        this.fileName = fileName;
        this.size = size;
        this.length = length;
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

    @Override
    public String toString() {
        return fileName + "\t\t" + size + "\t\t" + length;
    }
}
