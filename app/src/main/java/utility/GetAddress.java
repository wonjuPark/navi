package utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 박원주 on 2017-05-29.
 * 행자부에서 제공하는 API 사용 (기존 포털에서 제공하는 사이트는 서비스 종료 문제로 제외함)
 */

public class GetAddress{
    final static String addressURL = "http://www.juso.go.kr/addrlink/addrLinkApi.do";
    public StringBuffer getAddrAPI(String keyword, String confmKey){
        /* currentPage: 현재 페이지 번호, countPerPage: 페이지당 출력 할 결과, keyword:  검색어, confmKey: 발급받은 승인키, resultType: 검색결과 */
        int currentPage = 0;
        int countPerPage = 10;
         if(confmKey != "") ;
        String fullURL = addressURL + "?currentPage=" + currentPage  +"&countPerPage=" + countPerPage +"&keyword=" + keyword + "&confmKey=" + confmKey + "&resultType=json";
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null ;
        URL url;
        try {
            url = new URL(fullURL);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            br = new BufferedReader(new InputStreamReader(in));

            String tempStr = null;

            while((tempStr = br.readLine()) != null){
                sb.append(tempStr);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb;
    }


}
