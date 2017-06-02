package vo;

import java.io.Serializable;

/**
 * Created by 95016056 on 2017-05-29.
 */

public class AddressVO implements Serializable{
    String mapName = "";
    String zipNo = "";
    String jibunAddr = "";
    String roadAddrPart1 = "";

    String desc = "";
    double lon = 0.0;
    double lat = 0.0;
    double dist = 0;
    int index = 1;
    boolean check = false;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getZipNo() {        return zipNo;    }

    public void setZipNo(String zipNo) {
        this.zipNo = zipNo;
    }

    public String getJibunAddr() {
        return jibunAddr;
    }

    public void setJibunAddr(String jibunAddr) {
        this.jibunAddr = jibunAddr;
    }


    public String getRoadAddrPart1() {
        return roadAddrPart1;
    }

    public void setRoadAddrPart1(String roadAddrPart1) {
        this.roadAddrPart1 = roadAddrPart1;
    }
    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }
    public int getIndex() {
        return index;
    }

    public int getPlusIndex() {
        return index+1;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public void setIndex() {
        this.index = this.index+1;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

}

