import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 4/28/16.
 */
public class FileReader {
    public static void main(String[] args) {
        List<SiteData> dataList = new ArrayList<SiteData>();
        File folder = new File("../../pcapFiles");
        for(int i = folder.listFiles().length -1; i > 0; i--) {
            File file = folder.listFiles()[i];
            if(file.getName().startsWith("fixed_www")) {
                dataList.add(PcapParser.generateDataFromFile(file));

            }

        }
        Writer writer = null;
        try {
            writer = new FileWriter("output4.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Double> spdyTimes = new ArrayList<Double>();
        ArrayList<Double> spdyQuicTimes = new ArrayList<Double>();
        ArrayList<Double> otherTimes = new ArrayList<Double>();
        Gson gson = new GsonBuilder().create();
        ArrayList<SiteData> finalList = new ArrayList<SiteData>();
        for(SiteData data: dataList) {
            SiteData newData = new SiteData();
            newData.setQuic(data.isQuic()); newData.setSpdy(data.isSpdy()); newData.setIpAddress(data.getIpAddress());
            newData.setSiteName(data.getSiteName()); newData.setIpAddress(data.getIpAddress()); newData.setAlexaRank(data.getAlexaRank());
            long smallTimeStamp = Long.MAX_VALUE;
            for(DataObject obj: data.getObjects()) {
                if(obj.getResponseTimestamp() != 0 && obj.getNumOfBytes() != 0) {
                    if(obj.getRequestTimestamp() < smallTimeStamp) {
                        smallTimeStamp = obj.getRequestTimestamp();
                    }
                    newData.addObject(obj);
                }
            }
            if(newData.getObjects().size() > 0) {
                long totalResponseTime = 0;
                double finalResponseTime = 0;
                double totalBytes = 0;
                for(DataObject obj: newData.getObjects()) {
                    totalBytes += obj.getNumOfBytes();
                    obj.setRequestTimestamp(obj.getRequestTimestamp()-smallTimeStamp);
                    obj.setResponseTimestamp(obj.getResponseTimestamp()-smallTimeStamp);
                    totalResponseTime += obj.getResponseTimestamp() - obj.getRequestTimestamp();
                    if(obj.getResponseTimestamp() > finalResponseTime) {
                        finalResponseTime = obj.getResponseTimestamp();
                    }
                }
                double avgResponseTime = totalResponseTime/newData.getObjects().size();
                if(newData.isQuic()) {
                    spdyQuicTimes.add(finalResponseTime);
                } else if(newData.isSpdy()) {
                    spdyTimes.add(finalResponseTime);
                } else {
                    otherTimes.add(finalResponseTime);
                }
                newData.setPageLoadTime(finalResponseTime);
                newData.setThroughput(totalBytes/finalResponseTime);
                newData.setAvgResponseTime(avgResponseTime);
                finalList.add(newData);
            }
        }
//        System.out.println("SPDY + QUIC:");
//        for(Double time : spdyQuicTimes) {
//            System.out.println(time);
//        }
//        System.out.println("SPDY:");
//        for(Double time : spdyTimes) {
//            System.out.println(time);
//        }
//        System.out.println("NOT SPDY OR QUIC:");
//        for(Double time : otherTimes) {
//            System.out.println(time);
//        }

        gson.toJson(finalList, writer);
    }
}
