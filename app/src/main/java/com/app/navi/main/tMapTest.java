package com.app.navi.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.navi.R;
import utility.BaseActivity;
import utility.Common;
import utility.ImageOverlay;
import utility.LogManager;
import com.skp.Tmap.BizCategory;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapData.BizCategoryListenerCallback;
import com.skp.Tmap.TMapData.ConvertGPSToAddressListenerCallback;
import com.skp.Tmap.TMapData.FindAllPOIListenerCallback;
import com.skp.Tmap.TMapData.FindAroundNamePOIListenerCallback;
import com.skp.Tmap.TMapData.FindPathDataAllListenerCallback;
import com.skp.Tmap.TMapData.FindPathDataListenerCallback;
import com.skp.Tmap.TMapData.TMapPathType;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapGpsManager.onLocationChangedCallback;
import com.skp.Tmap.TMapLabelInfo;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapTapi;
import com.skp.Tmap.TMapView;
import com.skp.Tmap.TMapView.MapCaptureImageListenerCallback;
import com.skp.Tmap.TMapView.TMapLogoPositon;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by 95016056 on 2017-05-31.
 */

public class tMapTest extends BaseActivity implements onLocationChangedCallback
{
    @Override
    public void onLocationChange(Location location) {
        LogManager.printLog("onLocationChange :::> " + location.getLatitude() +  " " + location.getLongitude() + " " + location.getSpeed() + " " + location.getAccuracy());
        if(m_bTrackingMode)
        {
            mMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
//			mMapView.setCenterPoint(127.0624252, 37.5077583);
//			mMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        }
    }

    private TMapView		mMapView = null;

    private Context 		mContext;
    private ArrayList<Bitmap> mOverlayList;
    private ImageOverlay mOverlay;

    public static String mApiKey = "8c7c0140-221f-35c9-997d-a3506f8748ef"; // 발급받은 appKey
    public static String mBizAppID; // 발급받은 BizAppID (TMapTapi로 TMap앱 연동을 할 때 BizAppID 꼭 필요)

    private static final int[] mArrayMapButton = {
            R.id.btnNaviGuide,
            // 20170508 JWCha : 자전거 길안내 중지에 따른 조치 - START
		/*
		R.id.btnBicycle_Path,
		*/
            // 20170508 JWCha : 자전거 길안내 중지에 따른 조치 - END
            R.id.btnFindAllPoi,
            R.id.btnConvertToAddress,
            R.id.btnGetBizCategory,
            R.id.btnGetAroundBizPoi,
            R.id.btnCapture,
            R.id.btnDisalbeZoom,
            R.id.btnInvokeRoute,
            R.id.btnInvokeSetLocation,
            R.id.btnInvokeSearchPortal,
            R.id.btnTimeMachine,
            R.id.btnTMapInstall,
            R.id.btnMarkerPoint2,
    };

    private 	int 		m_nCurrentZoomLevel = 0;
    private 	double 		m_Latitude  = 0;
    private     double  	m_Longitude = 0;
    private 	boolean 	m_bShowMapIcon = false;

    private 	boolean 	m_bTrafficeMode = false;
    private 	boolean 	m_bSightVisible = false;
    private 	boolean 	m_bTrackingMode = false;

    private 	boolean 	m_bOverlayMode = false;

    ArrayList<String>		mArrayID;

    ArrayList<String>		mArrayCircleID;
    private static 	int 	mCircleID;

    ArrayList<String>		mArrayLineID;
    private static 	int 	mLineID;

    ArrayList<String>		mArrayPolygonID;
    private static  int 	mPolygonID;

    ArrayList<String>       mArrayMarkerID;
    private static int 		mMarkerID;

    TMapGpsManager gps = null;


    /**
     * onCreate()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mContext = this;

        mMapView = new TMapView(this);

        configureMapView();

        initView();

        addView(mMapView);
        setContentView(R.layout.main_activity);
        mArrayID = new ArrayList<String>();

        mArrayCircleID = new ArrayList<String>();
        mCircleID = 0;

        mArrayLineID = new ArrayList<String>();
        mLineID = 0;

        mArrayPolygonID = new ArrayList<String>();
        mPolygonID = 0;

        mArrayMarkerID	= new ArrayList<String>();
        mMarkerID = 0;

        gps = new TMapGpsManager(tMapTest.this);
        gps.setMinTime(1000);
        gps.setMinDistance(5);
        gps.setProvider(gps.NETWORK_PROVIDER);
        gps.OpenGps();

        mMapView.setTMapLogoPosition(TMapLogoPositon.POSITION_BOTTOMRIGHT);
    }

    /**
     * setSKPMapApiKey()에 ApiKey를 입력 한다.
     * setSKPMapBizappId()에 mBizAppID를 입력한다.
     * -> setSKPMapBizappId는 TMapTapi(TMap앱 연동)를 사용할때 BizAppID 설정 해야 한다. TMapTapi 사용하지 않는다면 setSKPMapBizappId를 하지 않아도 된다.
     */
    private void configureMapView() {
        mMapView.setSKPMapApiKey(mApiKey);
        //song
//		mMapView.setSKPMapBizappId(mBizAppID);
    }

    /**
     * initView - 버튼에 대한 리스너를 등록한다.
     */
    private void initView() {
        for (int btnMapView : mArrayMapButton) {
            Button ViewButton = (Button)findViewById(btnMapView);
            ViewButton.setOnClickListener(this);
        }

        mMapView.setOnApiKeyListener(new TMapView.OnApiKeyListenerCallback() {
            @Override
            public void SKPMapApikeySucceed() {
                LogManager.printLog("MainActivity SKPMapApikeySucceed");
            }

            @Override
            public void SKPMapApikeyFailed(String errorMsg) {
                LogManager.printLog("MainActivity SKPMapApikeyFailed " + errorMsg);
            }
        });

        //song
//		mMapView.setOnBizAppIdListener(new TMapView.OnBizAppIdListenerCallback() {
//			@Override
//			public void SKPMapBizAppIdSucceed() {
//				LogManager.printLog("MainActivity SKPMapBizAppIdSucceed");
//			}
//
//			@Override
//			public void SKPMapBizAppIdFailed(String errorMsg) {
//				LogManager.printLog("MainActivity SKPMapBizAppIdFailed " + errorMsg);
//			}
//		});


        mMapView.setOnEnableScrollWithZoomLevelListener(new TMapView.OnEnableScrollWithZoomLevelCallback() {
            @Override
            public void onEnableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                LogManager.printLog("MainActivity onEnableScrollWithZoomLevelEvent " + zoom + " " + centerPoint.getLatitude() + " " + centerPoint.getLongitude());
            }
        });

        mMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                LogManager.printLog("MainActivity onDisableScrollWithZoomLevelEvent " + zoom + " " + centerPoint.getLatitude() + " " + centerPoint.getLongitude());
            }
        });

        mMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                LogManager.printLog("MainActivity onPressUpEvent " + markerlist.size());
                return false;
            }

            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                LogManager.printLog("MainActivity onPressEvent " + markerlist.size());

                for (int i = 0; i < markerlist.size(); i++) {
                    TMapMarkerItem item = markerlist.get(i);
                    LogManager.printLog("MainActivity onPressEvent " + item.getName() + " " + item.getTMapPoint().getLatitude() + " " + item.getTMapPoint().getLongitude());
                }
                return false;
            }
        });

        mMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point) {
                LogManager.printLog("MainActivity onLongPressEvent " + markerlist.size());
            }
        });

        mMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {
                String strMessage = "";
                strMessage = "ID: " + markerItem.getID() + " " + "Title " + markerItem.getCalloutTitle();
                Common.showAlertDialog(tMapTest.this, "Callout Right Button", strMessage);
            }
        });

        mMapView.setOnClickReverseLabelListener(new TMapView.OnClickReverseLabelListenerCallback() {
            @Override
            public void onClickReverseLabelEvent(TMapLabelInfo findReverseLabel) {
                if(findReverseLabel != null) {
                    LogManager.printLog("MainActivity setOnClickReverseLabelListener " + findReverseLabel.id + " / " + findReverseLabel.labelLat
                            + " / " + findReverseLabel.labelLon + " / " + findReverseLabel.labelName);

                }
            }
        });

        m_nCurrentZoomLevel = -1;
        m_bShowMapIcon = false;
        m_bTrafficeMode = false;
        m_bSightVisible = false;
        m_bTrackingMode = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gps.CloseGps();
        if(mOverlayList != null){
            mOverlayList.clear();
        }
    }

    /**
     * onClick Event
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            // 20170508 JWCha : 자전거 길안내 중지에 따른 조치 - START
		/*
		case R.id.btnBicycle_Path     :     drawBicyclePath(); 	    break;
		*/
            // 20170508 JWCha : 자전거 길안내 중지에 따른 조치 - END

            case R.id.btnFindAllPoi		  :     findAllPoi();			break;
            case R.id.btnConvertToAddress :     convertToAddress(); 	break;
            case R.id.btnGetBizCategory   : 	getBizCategory(); 		break;
            case R.id.btnGetAroundBizPoi  :     getAroundBizPoi(); 		break;
                  /* 티맵 */
            case R.id.btnInvokeRoute	  :     invokeRoute();			break;
            case R.id.btnInvokeSetLocation: 	invokeSetLocation();    break;
            case R.id.btnInvokeSearchPortal: 	invokeSearchProtal(); 	break;
            case R.id.btnTMapInstall	  :     tmapInstall(); 			break;

            case R.id.btnNaviGuide		  :     naviGuide();			break;
            /* 티맵 */
            case R.id.btnCapture		  :     captureImage(); 		break;
            case R.id.btnDisalbeZoom	  : 	disableZoom();			break;
            case R.id.btnTimeMachine	  :   	timeMachine(); 			break;
        }
    }

    public TMapPoint randomTMapPoint() {
        double latitude = ((double)Math.random() ) * (37.575113-37.483086) + 37.483086;
        double longitude = ((double)Math.random() ) * (127.027359-126.878357) + 126.878357;

        latitude = Math.min(37.575113, latitude);
        latitude = Math.max(37.483086, latitude);

        longitude = Math.min(127.027359, longitude);
        longitude = Math.max(126.878357, longitude);

        LogManager.printLog("randomTMapPoint" + latitude + " " + longitude);

        TMapPoint point = new TMapPoint(latitude, longitude);

        return point;
    }

    public void overlay() {
        m_bOverlayMode = !m_bOverlayMode;
        if(m_bOverlayMode) {
            mMapView.setZoomLevel(6);

            if(mOverlay == null){
                mOverlay = new ImageOverlay(this, mMapView);
            }

            mOverlay.setLeftTopPoint(new TMapPoint(45.640171, 114.9652948));
            mOverlay.setRightBottomPoint(new TMapPoint(29.2267177, 138.7206798));
            mOverlay.setImage(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.test_image));

            if(mOverlayList == null){
                mOverlayList = new ArrayList<Bitmap>();
                mOverlayList.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.test_image));
                mOverlayList.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ani1));
                mOverlayList.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ani2));
            }

            mOverlay.setAnimationIcons(mOverlayList);
            mOverlay.setAniDuration(10000);
            mOverlay.startAnimation();
            mMapView.addTMapOverlayID(0, mOverlay);
        } else {
            mOverlay.stopAnimation();
            mMapView.removeTMapOverlayID(0);
        }
    }

    public Bitmap overlayMark(Bitmap bmp1, Bitmap bmp2, int width, int height) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());

        int marginLeft = 7;
        int marginTop = 5;

        if(width >= 1500 || height > 1500) {
            bmp2 = Bitmap.createScaledBitmap(bmp2, bmp1.getWidth() - 40, bmp1.getHeight() - 50, true);
            marginLeft = 20;
            marginTop = 10;
        } else if(width >= 1200 || height > 1200) {
            bmp2 = Bitmap.createScaledBitmap(bmp2, bmp1.getWidth() - 22, bmp1.getHeight() - 35, true);
            marginLeft = 11;
            marginTop = 7;
        } else {
            bmp2 = Bitmap.createScaledBitmap(bmp2, bmp1.getWidth() - 15, bmp1.getHeight() - 25, true);
        }

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(bmp2, marginLeft, marginTop, null);
        return bmOverlay;
    }

    private String getContentFromNode(Element item, String tagName){
        NodeList list = item.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            if (list.item(0).getFirstChild() != null) {
                return list.item(0).getFirstChild().getNodeValue();
            }
        }
        return null;
    }

    /**
     * naviGuide
     * 길안내
     */
    public void naviGuide() {
        TMapPoint point1 = mMapView.getCenterPoint();
        TMapPoint point2 = randomTMapPoint();

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataAll(point1, point2, new FindPathDataAllListenerCallback() {
            @Override
            public void onFindPathDataAll(Document doc) {
                LogManager.printLog("onFindPathDataAll: " + doc);
            }
        });
    }

    public void drawCarPath() {


        TMapPoint point1 = mMapView.getCenterPoint();
        TMapPoint point2 = randomTMapPoint();

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapPathType.CAR_PATH, point1, point2, new FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                mMapView.addTMapPath(polyLine);
            }
        });
    }

    public void drawPedestrianPath() {
        TMapPoint point1 = mMapView.getCenterPoint();
        TMapPoint point2 = randomTMapPoint();

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapPathType.PEDESTRIAN_PATH, point1, point2, new FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                polyLine.setLineColor(Color.BLUE);
                mMapView.addTMapPath(polyLine);
            }
        });
    }

    /**
     * getCenterPoint
     * 지도의 중심점을 가지고 온다.
     */
    public void getCenterPoint() {
        TMapPoint point = mMapView.getCenterPoint();

        Common.showAlertDialog(this, "", "지도의 중심 좌표는 " + point.getLatitude() + " " + point.getLongitude() );
    }

    /**
     * findAllPoi
     * 통합검색 POI를 요청한다.
     */
    public void findAllPoi() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("POI 통합 검색");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strData = input.getText().toString();
                TMapData tmapdata = new TMapData();

                tmapdata.findAllPOI(strData, new FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                        for (int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem  item = poiItem.get(i);

                            LogManager.printLog("POI Name: " + item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
                                    "Point: " + item.getPOIPoint().toString());
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }


    /**
     * convertToAddress
     * 지도에서 선택한 지점을 주소를 변경요청한다.
     */
    public void convertToAddress() {
        TMapPoint point = mMapView.getCenterPoint();

        TMapData tmapdata = new TMapData();

        if (mMapView.isValidTMapPoint(point)) {
            tmapdata.convertGpsToAddress(point.getLatitude(), point.getLongitude(), new ConvertGPSToAddressListenerCallback() {
                @Override
                public void onConvertToGPSToAddress(String strAddress) {
                    LogManager.printLog("선택한 위치의 주소는 " + strAddress);
                }
            });

//		    tmapdata.geoCodingWithAddressType("F02", "서울시", "구로구", "새말로", "6", "", new GeoCodingWithAddressTypeListenerCallback() {
//
//				@Override
//				public void onGeoCodingWithAddressType(TMapGeocodingInfo geocodingInfo) {
//					LogManager.printLog(">>> strMatchFlag : " + geocodingInfo.strMatchFlag);
//					LogManager.printLog(">>> strLatitude : " + geocodingInfo.strLatitude);
//					LogManager.printLog(">>> strLongitude : " + geocodingInfo.strLongitude);
//					LogManager.printLog(">>> strCity_do : " + geocodingInfo.strCity_do);
//					LogManager.printLog(">>> strGu_gun : " + geocodingInfo.strGu_gun);
//					LogManager.printLog(">>> strLegalDong : " + geocodingInfo.strLegalDong);
//					LogManager.printLog(">>> strAdminDong : " + geocodingInfo.strAdminDong);
//					LogManager.printLog(">>> strBunji : " + geocodingInfo.strBunji);
//					LogManager.printLog(">>> strNewMatchFlag : " + geocodingInfo.strNewMatchFlag);
//					LogManager.printLog(">>> strNewLatitude : " + geocodingInfo.strNewLatitude);
//					LogManager.printLog(">>> strNewLongitude : " + geocodingInfo.strNewLongitude);
//					LogManager.printLog(">>> strNewRoadName : " + geocodingInfo.strNewRoadName);
//					LogManager.printLog(">>> strNewBuildingIndex : " + geocodingInfo.strNewBuildingIndex);
//					LogManager.printLog(">>> strNewBuildingName : " + geocodingInfo.strNewBuildingName);
//				}
//			});
        }
    }

    /**
     * getBizCategory
     * 업종별 category를 요청한다.
     */
    public void getBizCategory() {
        TMapData tmapdata = new TMapData();

        tmapdata.getBizCategory(new BizCategoryListenerCallback() {
            @Override
            public void onGetBizCategory(ArrayList<BizCategory> poiItem) {
                for (int i = 0; i < poiItem.size(); i++) {
                    BizCategory item = poiItem.get(i);
                    LogManager.printLog("UpperBizCode " + item.upperBizCode + " " + "UpperBizName " + item.upperBizName );
                    LogManager.printLog("MiddleBizcode " + item.middleBizCode + " " + "MiddleBizName " + item.middleBizName);
                }
            }
        });
    }

    /**
     * getAroundBizPoi
     * 업종별 주변검색 POI 데이터를 요청한다.
     */
    public void getAroundBizPoi() {
        TMapData tmapdata = new TMapData();

        TMapPoint point = mMapView.getCenterPoint();

        tmapdata.findAroundNamePOI(point, "편의점;은행", 1, 99, new FindAroundNamePOIListenerCallback() {
            @Override
            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                for (int i = 0; i < poiItem.size(); i++) {
                    TMapPOIItem item = poiItem.get(i);
                    LogManager.printLog("POI Name: " + item.getPOIName() + "," + "Address: "
                            + item.getPOIAddress().replace("null", ""));
                }
            }
        });
    }

    public void setTileType() {
        AlertDialog dlg = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Select MAP Tile Type")
                .setSingleChoiceItems(R.array.a_tiletype, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        LogManager.printLog("Set Map Tile Type " + item);
                        dialog.dismiss();
                        mMapView.setTileType(item);
                    }
                }).show();
    }

    public void setBicycle() {
        mMapView.setBicycleInfo(!mMapView.IsBicycleInfo());
    }

    public void setBicycleFacility() {
        mMapView.setBicycleFacilityInfo(!mMapView.isBicycleFacilityInfo());
    }

    public void invokeRoute() {
        final TMapPoint point = mMapView.getCenterPoint();
        TMapData tmapdata = new TMapData();

        if(mMapView.isValidTMapPoint(point)) {
            tmapdata.convertGpsToAddress(point.getLatitude(), point.getLongitude(), new ConvertGPSToAddressListenerCallback() {
                @Override
                public void onConvertToGPSToAddress(String strAddress) {
                    TMapTapi tmaptapi = new TMapTapi(tMapTest.this);
                    float fY = (float)point.getLatitude();
                    float fX = (float)point.getLongitude();
                    tmaptapi.invokeRoute(strAddress, fX, fY);
                }
            });
        }
    }

    public void invokeSetLocation() {
        final TMapPoint point = mMapView.getCenterPoint();
        TMapData tmapdata = new TMapData();

        tmapdata.convertGpsToAddress(point.getLatitude(), point.getLongitude(), new ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String strAddress) {
                TMapTapi tmaptapi = new TMapTapi(tMapTest.this);
                float fY = (float) point.getLatitude();
                float fX = (float) point.getLongitude();
                tmaptapi.invokeSetLocation(strAddress, fX, fY);
            }
        });
    }

    public void invokeSearchProtal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("T MAP 통합 검색");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strSearch = input.getText().toString();

                new Thread() {
                    @Override
                    public void run() {
                        TMapTapi tmaptapi = new TMapTapi(tMapTest.this);
                        if (strSearch.trim().length() > 0)
                            tmaptapi.invokeSearchPortal(strSearch);
                    }
                }.start();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void tmapInstall() {
        new Thread() {
            @Override
            public void run() {
                TMapTapi tmaptapi = new TMapTapi(tMapTest.this);
                Uri uri = Uri.parse(tmaptapi.getTMapDownUrl().get(0));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

        }.start();
    }

    public void captureImage() {
        mMapView.getCaptureImage(20, new MapCaptureImageListenerCallback() {

            @Override
            public void onMapCaptureImage(Bitmap bitmap) {

                String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();

                File path = new File(sdcard + File.separator + "image_write");
                if (!path.exists())
                    path.mkdir();

                File fileCacheItem = new File(path.toString() + File.separator + System.currentTimeMillis() + ".png");
                OutputStream out = null;

                try {
                    fileCacheItem.createNewFile();
                    out = new FileOutputStream(fileCacheItem);

                    bitmap.compress(CompressFormat.JPEG, 90, out);

                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean bZoomEnable = false;

    public void disableZoom() {
        bZoomEnable = !bZoomEnable;
        mMapView.setUserScrollZoomEnable(bZoomEnable);
    }

    public void timeMachine() {
        TMapData tmapdata = new TMapData();

        HashMap<String, String> pathInfo = new HashMap<String, String>();
        pathInfo.put("rStName", "T Tower");
        pathInfo.put("rStlat", Double.toString(37.566474));
        pathInfo.put("rStlon", Double.toString(126.985022));
        pathInfo.put("rGoName", "신도림");
        pathInfo.put("rGolat", "37.50861147");
        pathInfo.put("rGolon", "126.8911457");
        pathInfo.put("type", "arrival");

        Date currentTime = new Date();
        tmapdata.findTimeMachineCarPath(pathInfo,  currentTime, null);
    }
}