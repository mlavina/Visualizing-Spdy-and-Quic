
import com.sun.tools.javac.util.ArrayUtils;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import org.pcap4j.util.ByteArrays;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Aaron on 4/28/16.
 */
public class PcapParser {
    public static SiteData generateDataFromFile(File file) {
        SiteData data = new SiteData();
        try {
            String siteName = file.getName().replace(".pcap", "").replace("fixed_", "");
            data.setSiteName(siteName);
            data.setAlexaRank(getAlexaRank(siteName));

            data.setIpAddress(InetAddress.getByName(siteName).getHostAddress());

            data.setSpdy(getSPDY(siteName));
            data.setQuic(getQUIC(siteName));

            List<DataObject> objects = new ArrayList<DataObject>();







            PcapHandle handle = Pcaps.openOffline(file.getPath());

            Packet packet = handle.getNextPacketEx();
            while(packet != null) {

                //determine if this packet is from the site or going to it. Otherwise ignore it
                boolean destMatches = false;
                boolean sourceMatches = false;
                InetAddress[] addresses = InetAddress.getAllByName(siteName);
                for(InetAddress address: addresses) {
                    if(address.getHostAddress().equals(getSourceIP(packet))) {
                        sourceMatches = true;
                    }
                    if(address.getHostAddress().equals(getDestIP(packet))) {
                        destMatches = true;
                    }
                }

                // parse and get info for packet headed to site
                if(destMatches) {
                    if(packet.getRawData().length >= 69) {
                        if(contains(packet.getRawData(), new byte[]{71, 69, 84})) {


                            DataObject o = new DataObject();
                            o.setRequestTimestamp(handle.getTimestamp().getTime());

                            byte[] rawData = packet.getRawData();
                            String fileType = "";
                            for(int i = 0; i < rawData.length-3; i++) {
                                if(rawData[i] == 71 && rawData[i+1] == 69 && rawData[i+2] == 84) {
                                    //until next space add bytes
                                    ArrayList<Byte> resourceURL = new ArrayList<Byte>();
                                    i += 4;
                                    byte b = rawData[i];
                                    while(b != 32 && i < rawData.length - 1) {
                                        resourceURL.add(b);
                                        b = rawData[i];
                                        i++;
                                    }
                                    String url = new String(org.apache.commons.lang.ArrayUtils.toPrimitive(resourceURL.toArray(new Byte[0])));
                                    fileType = url.substring(url.lastIndexOf(".") + 1);
                                    if(url.lastIndexOf(".") == -1) {
                                        fileType = "";
                                    }
                                    if(fileType.length() > 5) {
                                        int q = url.indexOf("?");
                                        if(q == -1) {
                                            fileType = "";
                                        } else {
                                            int d = url.substring(0, q).lastIndexOf(".");
                                            if(d != -1) {
                                                fileType = url.substring(d + 1, q);
                                            } else {
                                                fileType = "";
                                            }
                                        }

                                    }

                                }
                            }
                            o.setFileType(fileType);

                            byte b = rawData[14];
                            int low = b & 0x0F;
                            //  int high = b >> 4;
                            int headerLength = low * 4; //bytes of header
                            int totLen1 = rawData[16];
                            int totLen2 = rawData[17];
                            if(totLen1 < 0) totLen1 += 256;
                            if(totLen2 < 0) totLen2 += 256;
                            int totalLength =Integer.parseInt(Integer.toHexString(totLen1) + Integer.toHexString(totLen2),16);
                            int tcpHeaderLength = rawData[14+headerLength+12];
                            if(tcpHeaderLength<0) tcpHeaderLength+= 256;
                            tcpHeaderLength = tcpHeaderLength/4;

                            String seqBytes = "";
                            for (int i = 0; i < 4; i++) {
                                int v = rawData[i + 14 + headerLength + 4];
                                if(v < 0) {
                                    v += 256;
                                }
                                seqBytes += Integer.toHexString(v);

                            }
                            long seqNumber = Long.parseLong(seqBytes, 16);
                            o.setResponseAckNum(seqNumber + (totalLength - headerLength - tcpHeaderLength));

                            data.addObject(o);
                        }
                    }
                }
                // http response
                if(sourceMatches) {
                    byte[] rawData = packet.getRawData();


                    byte b = rawData[14];
                    int low = b & 0x0F;
                    //  int high = b >> 4;
                    int headerLength = low * 4; //bytes of header
                    String ackBytes = "";
                    for (int i = 0; i < 4; i++) {
                        int v = rawData[i + 14 + headerLength + 8];
                        if(v < 0) {
                            v += 256;
                        }
                        ackBytes += Integer.toHexString(v);

                    }
                    long ackNumber = Long.parseLong(ackBytes, 16);

                    for(DataObject obj : data.getObjects()) {
                        if(obj.getResponseAckNum() == ackNumber) {
                            obj.setResponseTimestamp(handle.getTimestamp().getTime());
                            int totLen1 = rawData[16];
                            int totLen2 = rawData[17];
                            if(totLen1 < 0) totLen1 += 256;
                            if(totLen2 < 0) totLen2 += 256;
                            int totalLength =Integer.parseInt(Integer.toHexString(totLen1) + Integer.toHexString(totLen2),16);
                            obj.setNumOfBytes(totalLength);
                        }
                    }



                }

                packet = handle.getNextPacketEx();
            }

        } catch (EOFException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private static String getDestIP(Packet packet) {
        byte[] destBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            destBytes[i] = packet.getRawData()[i + 30];
        }
        return(ByteArrays.getInet4Address(destBytes, 0).getHostAddress());
    }

    private static String getSourceIP(Packet packet) {
        byte[] sourceBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            sourceBytes[i] = packet.getRawData()[i + 26];
        }
        return(ByteArrays.getInet4Address(sourceBytes, 0).getHostAddress());
    }

    private static int getAlexaRank(String siteName) throws FileNotFoundException {
        File file = new File("res/AlexaList.txt");
        Scanner sc = new Scanner(file);
        int count = 1;
        while(sc.hasNext()) {
            if(siteName.equals("www."+sc.next())) {
                return count;
            }
            count++;
        }
        return count;
    }

    private static boolean contains(byte[] set1, byte[] set2) {
        OUTER:
        for (int i = 0; i < set1.length - set2.length; i++) {
            for (int j = 0; j < set2.length; j++) {
                if (set1[i + j] != set2[j])
                    continue OUTER;
            }
            return true;
        }
        return false;
    }

    private static boolean getSPDY(String site) {
        File file = new File("res/spdysites.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] info = line.split(" ");
            if(("www."+info[0]).equals(site)) {
                return(Arrays.asList(info).contains("spdy"));
            }

        }
        return false;
    }

    private static boolean getQUIC(String site) {
        File file = new File("res/spdysites.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] info = line.split(" ");
            if(("www."+info[0]).equals(site)) {
                return(Arrays.asList(info).contains("quic"));
            }

        }
        return false;
    }
}
