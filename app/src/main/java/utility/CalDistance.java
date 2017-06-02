package utility;

import vo.AddressVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by 95016056 on 2017-06-01.
 */

public class CalDistance {
    public CalDistance(){

    }

    /* 선택된 주소별로 거리 계산 및 최단 거리 별로 순위지정 */
    public ArrayList<AddressVO> getDistancePriority(ArrayList<AddressVO> list, AddressVO location){
        /* 변수 선언 */
        int len= list.size();
       // LxyVO lxy = new LxyVO();
        AddressVO curLo = location;
        ArrayList<AddressVO> temp = list;
        ArrayList<AddressVO> aList;
        AddressVO tVO;
        ArrayList<AddressVO> results =  new ArrayList<AddressVO>();

        for(int i=0; i <  len; i++){
            aList = new ArrayList<AddressVO>();
            for(AddressVO vo : temp){
                vo.setDist(getDistance(vo.getLat(), vo.getLon(), curLo.getLat(), curLo.getLon()));
                aList.add(vo);
            }

            /* 거리 계산후 짧은 거리 순으로 정렬 */
            Collections.sort(aList, new Comparator<AddressVO>(){
                @Override
                public int compare(AddressVO o1, AddressVO o2){
                    if(o1.getDist() > o2.getDist()){
                        return 1;
                    } else if(o1.getDist() < o2.getDist()){
                        return -1;
                    } else{
                        return 0;
                    }
                }
            });

            /* 현 위치(지정위치) 에서 제일 가까운 순으로 lIST 정렬 */
            tVO = new AddressVO();
            tVO = aList.get(0);
            temp.remove(tVO);
            tVO.setIndex(i+1);
            tVO.setCheck(true);
            curLo = tVO;
            results.add(tVO);
        }

        return results;
    }
    /* 경도 위도로 거리 계산 */
    public double getDistance(Double lat, Double lon, Double lat1, Double lon1){
        double EARTH_R, Rad, radLat1, radLat2, radDist;
        double distance, ret, dist;

        EARTH_R = 6371000.0;
        Rad = Math.PI/180;
        radLat1 = Rad * lat;
        radLat2 = Rad * lat1;
        radDist = Rad * (lon - lon1);

        distance = Math.sin(radLat1) * Math.sin(radLat2);
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist);
        ret = EARTH_R * Math.acos(distance);

        dist = Math.round(ret);

        return dist;
    }
}



