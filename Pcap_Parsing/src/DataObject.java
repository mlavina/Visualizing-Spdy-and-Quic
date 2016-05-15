/**
 * Created by Aaron on 5/1/16.
 */
public class DataObject {
    private long requestTimestamp;
    private long responseTimestamp;
    private int numOfBytes;

    private long responseAckNum;
    private String fileType;

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public long getResponseTimestamp() {
        return responseTimestamp;
    }

    public void setResponseTimestamp(long responseTimestamp) {
        this.responseTimestamp = responseTimestamp;
    }

    public int getNumOfBytes() {
        return numOfBytes;
    }

    public void setNumOfBytes(int numOfBytes) {
        this.numOfBytes = numOfBytes;
    }

    public long getResponseAckNum() {
        return responseAckNum;
    }

    public void setResponseAckNum(long responseAckNum) {
        this.responseAckNum = responseAckNum;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
