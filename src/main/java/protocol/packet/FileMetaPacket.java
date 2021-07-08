package protocol.packet;

import java.io.File;

import static protocol.command.Command.FILE_PACKET;

public class FileMetaPacket extends Packet {

    private File file;

    private String fileName;

    private Long fileLength;

    int ACK = 0;

    public FileMetaPacket() {

    }

    @Override
    public Byte getCommand() {
        return FILE_PACKET;
    }

    public FileMetaPacket(String fileName, Long fileLength, File file) {
        this.fileName = fileName;
        this.fileLength = fileLength;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public int getACK() {
        return ACK;
    }

    public void setACK(int ACK) {
        this.ACK = ACK;
    }
}
