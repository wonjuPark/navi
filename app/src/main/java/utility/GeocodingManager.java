package utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by 95016056 on 2017-05-31.
 */

public class GeocodingManager{
    final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    private static String apikey = "";

    public GeocodingManager(String apikey){
        this.apikey = apikey;
    }

    public StringBuffer getTMapXY(String address){
        String apiURL = "";
        StringBuffer response = null;
        try {
            String addr = URLEncoder.encode(address, "UTF-8");
            apiURL = "http://apis.skplanetx.com/tmap/geo/fullAddrGeo?addressFlag=F02&version=1&coordType=WGS84GEO&fullAddr=" + addr; //json
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("appKey", apikey);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return response;
    }

    public StringBuffer getChangeXY(String xy){
        String apiURL = "";
        String[] tXY = new String[2];
        if(xy.length() > 0)
            tXY = xy.split(",");
        StringBuffer response = null;
        try {
            //String addr = URLEncoder.encode(address, "UTF-8");
            apiURL = "http://apis.skplanetx.com/tmap/geo/coordconvert?version=1&fromCoord=WGS84GEO&toCoord=EPSG3857&lat=" + tXY[0] + "&lon=" + tXY[1]; //json
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("appKey", apikey);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return response;
    }


    public StringBuffer getKMapXY(String address){
        trustAllHosts();
        int div = 1;
        String apiURL = "";
        StringBuffer response = null;
        try {

            String addr = URLEncoder.encode(address, "UTF-8");
            apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr; //json
            URL url = new URL(apiURL);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setHostnameVerifier(DO_NOT_VERIFY);
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return response;
    }

    /**
     * Trust every server - don't check for any certificate
     */
    private void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
