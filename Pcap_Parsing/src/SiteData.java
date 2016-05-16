import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 4/28/16.
 */
public class SiteData {
    private String siteName;
    private String ipAddress;
    private int alexaRank;
    private boolean spdy;
    private boolean quic;
    private List<DataObject> objects = new ArrayList<DataObject>();
    private double throughput;
    private double pageLoadTime;
    private double avgResponseTime;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getAlexaRank() {
        return alexaRank;
    }

    public void setAlexaRank(int alexaRank) {
        this.alexaRank = alexaRank;
    }

    public boolean isSpdy() {
        return spdy;
    }

    public void setSpdy(boolean spdy) {
        this.spdy = spdy;
    }

    public boolean isQuic() {
        return quic;
    }

    public void setQuic(boolean quic) {
        this.quic = quic;
    }

    public void setObjects(List<DataObject> objects) {
        this.objects = objects;
    }

    public List<DataObject> getObjects() {
        return objects;
    }
    public void addObject(DataObject object) {
        this.objects.add(object);
    }
    public void removeObject(DataObject object) {
        this.objects.remove(object);
    }

    public double getThroughput() {
        return throughput;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

    public double getPageLoadTime() {
        return pageLoadTime;
    }

    public void setPageLoadTime(double pageLoadTime) {
        this.pageLoadTime = pageLoadTime;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }
}
