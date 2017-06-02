package utility;

import android.os.AsyncTask;

/**
 * Created by 95016056 on 2017-05-29.
 */

public class OpenAPITask extends AsyncTask<String, String, StringBuffer> {

    @Override
    public StringBuffer doInBackground(String... params) {
        String keyword = params[0]; // 주소 검색 키워드
        String apiKey = params[1];  // 주소 API 키
        StringBuffer sb = new StringBuffer();
        GeocodingManager geocodingManager;
        switch (params[2]){ // 구분
            case "1":
                geocodingManager = new GeocodingManager(apiKey);
                if(params[3].equals("TAPI")) {
                    sb = geocodingManager.getTMapXY(keyword);
                }else if(params[3].equals("KAPI")){
                    sb = geocodingManager.getKMapXY(keyword);
                }
                break;
            case "2":
                GetAddress ga = new GetAddress();
                sb = ga.getAddrAPI(keyword, apiKey);
                break;
            case "3":
                geocodingManager = new GeocodingManager(apiKey);
                sb = geocodingManager.getChangeXY(keyword);
                break;
        }

        return sb;
    }

}
