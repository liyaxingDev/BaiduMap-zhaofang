package myproject.com.baidumapmarker;

import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lyx
 * @date 2018/11/13
 */
public class MainPresenter {

    private final MainContarct mContarct;
    List<LatLng> mScreenLatLng = new ArrayList<LatLng>();

    private PolygonOptions mOoPolyline;

    public MainPresenter(MainContarct contarct) {
        mContarct = contarct;
    }

    /**
     * 定位当前位置
     *
     * @param baiduMap
     */
    public void startLocation(final BaiduMap baiduMap) {

        final BDLocation mLocalLocation = LocationUtils.getInstance().mLocalLocation;

        if (mLocalLocation != null) {
            getMarkerData(baiduMap.getMapStatus().zoom);
            location(baiduMap, mLocalLocation);
        } else {
            LocationUtils.getInstance().setOnLocationListener(new LocationUtils.OnLocationListener() {
                @Override
                public void onSuccess(BDLocation location) {
                    location(baiduMap, location);
                    getMarkerData(baiduMap.getMapStatus().zoom);
                }

                @Override
                public void onFaild() {
                    Toast.makeText(BaseApplication.mApp, "定位失败,请检查定位权限是否开启", Toast.LENGTH_SHORT).show();
                }
            }).start();
        }
    }

    /**
     * 定位移动
     *
     * @param baiduMap
     * @param mLocalLocation
     */
    private void location(BaiduMap baiduMap, BDLocation mLocalLocation) {
        MyLocationData myLocationData = new MyLocationData.Builder()
                .accuracy(mLocalLocation.getRadius())
                .latitude(mLocalLocation.getLatitude())
                .longitude(mLocalLocation.getLongitude()).build();
        baiduMap.setMyLocationData(myLocationData);
        LatLng ll = new LatLng(mLocalLocation.getLatitude(),
                mLocalLocation.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(16.0f);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }

    /**
     * 绘制触摸范围
     *
     * @param points   触摸点
     * @param baiduMap 地图对象
     */
    public void drawPolygon(List<LatLng> points, BaiduMap baiduMap) {

        //绘制面
        if (mOoPolyline == null) {

            mOoPolyline = new PolygonOptions()
                    .points(points)
                    .stroke(new Stroke(10, 0xAAFF0000))
                    .fillColor(0xAAFFFF00);
        } else {
            mOoPolyline.points(points);
        }
        baiduMap.addOverlay(mOoPolyline);


        //计算缩放比例
        List<Double> latitudeList = new ArrayList<Double>();
        List<Double> longitudeList = new ArrayList<Double>();

        for (int i = 0; i < points.size(); i++) {
            double latitude = points.get(i).latitude;
            double longitude = points.get(i).longitude;
            latitudeList.add(latitude);
            longitudeList.add(longitude);
        }
        Double maxLatitude = Collections.max(latitudeList);
        Double minLatitude = Collections.min(latitudeList);
        Double maxLongitude = Collections.max(longitudeList);
        Double minLongitude = Collections.min(longitudeList);


        int zoom[] = {10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 1000, 2000, 25000, 50000, 100000, 200000, 500000, 1000000, 2000000};

        // 创建点坐标A
        LatLng pointA = new LatLng(maxLongitude, maxLatitude);
        // 创建点坐标B
        LatLng pointB = new LatLng(minLongitude, minLatitude);
        //获取两点距离,保留小数点后两位
        double distance = DistanceUtil.getDistance(pointA, pointB);
        for (int i = 0; i < zoom.length; i++) {
            int zoomNow = zoom[i];
            if (zoomNow - distance > 0) {
                float level = 18 - i + 3.7f;
                //设置地图显示级别为计算所得level
                baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(level).build()));
                break;
            }
        }

        //定位中心点
        LatLng center = new LatLng((maxLatitude + minLatitude) / 2, (maxLongitude + minLongitude) / 2);
        MapStatusUpdate status1 = MapStatusUpdateFactory.newLatLng(center);
        baiduMap.animateMapStatus(status1, 500);

    }

    /**
     * 接口获取图层数据集
     *
     * @param zoom
     */

    public void getMarkerData(final float zoom) {
        new Thread(new Runnable() {
            @Override
            public void run() {

//                String mBean = "{\"msg\":\"查询成功\",\"total\":7,\"code\":0,\"data\":[{\"min_area\":\"\",\"max_area\":\"\",\"map_point\":\"116.464985,39.876464\",\"buildmsg\":\"建业写字楼\",\"id\":\"0b016bc068371f5101683c2cd2de01a8\",\"buildImg\":\"ac01dbee-0c34-4f1e-9579-fee8d4aaec6c.png\",\"businessName\":\"潘家园\",\"region_name\":\"朝阳区\",\"average_price\":\"\",\"business_district\":\"0b016bc067d3ba340167d439a51300f6\",\"subway_station\":null,\"line\":null,\"business_type\":0,\"longitude\":\"116.464985\",\"latitude\":\"39.876464\",\"houseCount\":1},{\"min_area\":\"\",\"max_area\":\"\",\"map_point\":\"116.462312,39.883859\",\"buildmsg\":\"劲松大厦\",\"id\":\"0b016bc068371f5101683c33ada001b4\",\"buildImg\":\"e1dfd1f0-c5a4-48dc-9c5f-c0981432db32.png\",\"businessName\":\"劲松\",\"region_name\":\"朝阳区\",\"average_price\":\"\",\"business_district\":\"0b016bc067d3ba340167d4397e1200f4\",\"subway_station\":null,\"line\":null,\"business_type\":0,\"longitude\":\"116.462312\",\"latitude\":\"39.883859\",\"houseCount\":1},{\"min_area\":\"\",\"max_area\":\"\",\"map_point\":\"116.272554,39.88891\",\"buildmsg\":\"金隅大成时代写字楼\",\"id\":\"0b016bc067ce82f20167cf6a0c7300af\",\"buildImg\":\"77f53c3f-2978-4118-94bc-24369cc2dea0.png\",\"businessName\":\"青塔\",\"region_name\":\"丰台区\",\"average_price\":\"25\",\"business_district\":\"0b016bc067d3ba340167d3fa07100065\",\"subway_station\":null,\"line\":null,\"business_type\":0,\"longitude\":\"116.272554\",\"latitude\":\"39.88891\",\"houseCount\":5},{\"min_area\":\"\",\"max_area\":\"\",\"map_point\":\"116.225362,39.90631\",\"buildmsg\":\"石景山万达广场\",\"id\":\"0b016bc067ee17c00167ef20c6f901b9\",\"buildImg\":\"f8ccccaa-67e8-4e75-9319-36bb47ee7855.png\",\"businessName\":\"鲁谷/八宝山\",\"region_name\":\"石景山区\",\"average_price\":\"\",\"business_district\":\"0b016bc067d3ba340167d40e35140099\",\"subway_station\":null,\"line\":null,\"business_type\":0,\"longitude\":\"116.225362\",\"latitude\":\"39.90631\",\"houseCount\":2},{\"min_area\":\"\",\"max_area\":\"\",\"map_point\":\"116.218786,39.906479\",\"buildmsg\":\"万商大厦\",\"id\":\"0b016bc067ee17c00167ef0c560001af\",\"buildImg\":\"9976636e-75dd-47b8-a1c3-75dbca757243.png\",\"businessName\":\"鲁谷/八宝山\",\"region_name\":\"石景山区\",\"average_price\":\"5\",\"business_district\":\"0b016bc067d3ba340167d40e35140099\",\"subway_station\":null,\"line\":null,\"business_type\":0,\"longitude\":\"116.218786\",\"latitude\":\"39.906479\",\"houseCount\":3},{\"min_area\":\"\",\"max_area\":\"\",\"map_point\":\"116.254763,39.980247\",\"buildmsg\":\"北坞创新园\",\"id\":\"0b016bc068744ad501688406325d016d\",\"buildImg\":\"7c6cb341-67cc-4cba-8071-fe701aa8968e.png\",\"businessName\":\"海淀周边\",\"region_name\":\"海淀区\",\"average_price\":\"\",\"business_district\":\"0b016bc067d3ba340167d40ac9e50077\",\"subway_station\":null,\"line\":null,\"business_type\":0,\"longitude\":\"116.254763\",\"latitude\":\"39.980247\",\"houseCount\":1},{\"min_area\":\"\",\"max_area\":\"\",\"map_point\":\"116.227406,40.079163\",\"buildmsg\":\"绿海大厦\",\"id\":\"0b016bc068744ad5016883c8e03a0163\",\"buildImg\":\"d5838f2e-3ec4-4074-afa3-cffd82c3ad2a.png\",\"businessName\":\"海淀周边\",\"region_name\":\"海淀区\",\"average_price\":\"\",\"business_district\":\"0b016bc067d3ba340167d40ac9e50077\",\"subway_station\":null,\"line\":null,\"business_type\":0,\"longitude\":\"116.227406\",\"latitude\":\"40.079163\",\"houseCount\":1}]}";
                String mBean = "{\n" +
                        "    \"msg\": \"查询成功\",\n" +
                        "    \"total\": 7,\n" +
                        "    \"code\": 0,\n" +
                        "    \"data\": [\n" +
                        "        {\n" +
                        "            \"min_area\": \"\",\n" +
                        "            \"max_area\": \"\",\n" +
                        "            \"map_point\": \"116.464985,39.876464\",\n" +
                        "            \"buildmsg\": \"建业写字楼\",\n" +
                        "            \"id\": \"0b016bc068371f5101683c2cd2de01a8\",\n" +
                        "            \"buildImg\": \"ac01dbee-0c34-4f1e-9579-fee8d4aaec6c.png\",\n" +
                        "            \"region_id\": 110105,\n" +
                        "            \"businessName\": \"潘家园\",\n" +
                        "            \"region_name\": \"朝阳区\",\n" +
                        "            \"average_price\": \"\",\n" +
                        "            \"business_district\": \"0b016bc067d3ba340167d439a51300f6\",\n" +
                        "            \"subway_station\": null,\n" +
                        "            \"line\": null,\n" +
                        "            \"business_type\": 0,\n" +
                        "            \"longitude\": \"116.464985\",\n" +
                        "            \"latitude\": \"39.876464\",\n" +
                        "            \"houseCount\": 1,\n" +
                        "            \"buildSum\": 9,\n" +
                        "            \"buildCount\": 5\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"min_area\": \"\",\n" +
                        "            \"max_area\": \"\",\n" +
                        "            \"map_point\": \"116.462312,39.883859\",\n" +
                        "            \"buildmsg\": \"劲松大厦\",\n" +
                        "            \"id\": \"0b016bc068371f5101683c33ada001b4\",\n" +
                        "            \"buildImg\": \"e1dfd1f0-c5a4-48dc-9c5f-c0981432db32.png\",\n" +
                        "            \"region_id\": 110105,\n" +
                        "            \"businessName\": \"劲松\",\n" +
                        "            \"region_name\": \"朝阳区\",\n" +
                        "            \"average_price\": \"\",\n" +
                        "            \"business_district\": \"0b016bc067d3ba340167d4397e1200f4\",\n" +
                        "            \"subway_station\": null,\n" +
                        "            \"line\": null,\n" +
                        "            \"business_type\": 0,\n" +
                        "            \"longitude\": \"116.462312\",\n" +
                        "            \"latitude\": \"39.883859\",\n" +
                        "            \"houseCount\": 1,\n" +
                        "            \"buildSum\": 9,\n" +
                        "            \"buildCount\": 3\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"min_area\": \"\",\n" +
                        "            \"max_area\": \"\",\n" +
                        "            \"map_point\": \"116.272554,39.88891\",\n" +
                        "            \"buildmsg\": \"金隅大成时代写字楼\",\n" +
                        "            \"id\": \"0b016bc067ce82f20167cf6a0c7300af\",\n" +
                        "            \"buildImg\": \"77f53c3f-2978-4118-94bc-24369cc2dea0.png\",\n" +
                        "            \"region_id\": 110106,\n" +
                        "            \"businessName\": \"青塔\",\n" +
                        "            \"region_name\": \"丰台区\",\n" +
                        "            \"average_price\": \"25\",\n" +
                        "            \"business_district\": \"0b016bc067d3ba340167d3fa07100065\",\n" +
                        "            \"subway_station\": null,\n" +
                        "            \"line\": null,\n" +
                        "            \"business_type\": 0,\n" +
                        "            \"longitude\": \"116.28\",\n" +
                        "            \"latitude\": \"39.85\",\n" +
                        "            \"houseCount\": 7,\n" +
                        "            \"buildSum\": 70,\n" +
                        "            \"buildCount\": 2\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"min_area\": \"\",\n" +
                        "            \"max_area\": \"\",\n" +
                        "            \"map_point\": \"116.225362,39.90631\",\n" +
                        "            \"buildmsg\": \"石景山万达广场\",\n" +
                        "            \"id\": \"0b016bc067ee17c00167ef20c6f901b9\",\n" +
                        "            \"buildImg\": \"f8ccccaa-67e8-4e75-9319-36bb47ee7855.png\",\n" +
                        "            \"region_id\": 110107,\n" +
                        "            \"businessName\": \"鲁谷/八宝山\",\n" +
                        "            \"region_name\": \"石景山区\",\n" +
                        "            \"average_price\": \"\",\n" +
                        "            \"business_district\": \"0b016bc067d3ba340167d40e35140099\",\n" +
                        "            \"subway_station\": null,\n" +
                        "            \"line\": null,\n" +
                        "            \"business_type\": 0,\n" +
                        "            \"longitude\": \"116.225362\",\n" +
                        "            \"latitude\": \"39.90631\",\n" +
                        "            \"houseCount\": 2,\n" +
                        "            \"buildSum\": 40,\n" +
                        "            \"buildCount\": 12\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"min_area\": \"\",\n" +
                        "            \"max_area\": \"\",\n" +
                        "            \"map_point\": \"116.218786,39.906479\",\n" +
                        "            \"buildmsg\": \"万商大厦\",\n" +
                        "            \"id\": \"0b016bc067ee17c00167ef0c560001af\",\n" +
                        "            \"buildImg\": \"9976636e-75dd-47b8-a1c3-75dbca757243.png\",\n" +
                        "            \"region_id\": 110107,\n" +
                        "            \"businessName\": \"鲁谷/八宝山\",\n" +
                        "            \"region_name\": \"石景山区\",\n" +
                        "            \"average_price\": \"5\",\n" +
                        "            \"business_district\": \"0b016bc067d3ba340167d40e35140099\",\n" +
                        "            \"subway_station\": null,\n" +
                        "            \"line\": null,\n" +
                        "            \"business_type\": 0,\n" +
                        "            \"longitude\": \"116.218786\",\n" +
                        "            \"latitude\": \"39.906479\",\n" +
                        "            \"houseCount\": 3,\n" +
                        "            \"buildSum\": 40,\n" +
                        "            \"buildCount\": 12\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"min_area\": \"\",\n" +
                        "            \"max_area\": \"\",\n" +
                        "            \"map_point\": \"116.254763,39.980247\",\n" +
                        "            \"buildmsg\": \"北坞创新园\",\n" +
                        "            \"id\": \"0b016bc068744ad501688406325d016d\",\n" +
                        "            \"buildImg\": \"7c6cb341-67cc-4cba-8071-fe701aa8968e.png\",\n" +
                        "            \"region_id\": 110108,\n" +
                        "            \"businessName\": \"海淀周边\",\n" +
                        "            \"region_name\": \"海淀区\",\n" +
                        "            \"average_price\": \"\",\n" +
                        "            \"business_district\": \"0b016bc067d3ba340167d40ac9e50077\",\n" +
                        "            \"subway_station\": null,\n" +
                        "            \"line\": null,\n" +
                        "            \"business_type\": 0,\n" +
                        "            \"longitude\": \"116.254763\",\n" +
                        "            \"latitude\": \"39.980247\",\n" +
                        "            \"houseCount\": 1,\n" +
                        "            \"buildSum\": 202,\n" +
                        "            \"buildCount\": 16\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"min_area\": \"\",\n" +
                        "            \"max_area\": \"\",\n" +
                        "            \"map_point\": \"116.227406,40.079163\",\n" +
                        "            \"buildmsg\": \"绿海大厦\",\n" +
                        "            \"id\": \"0b016bc068744ad5016883c8e03a0163\",\n" +
                        "            \"buildImg\": \"d5838f2e-3ec4-4074-afa3-cffd82c3ad2a.png\",\n" +
                        "            \"region_id\": 110108,\n" +
                        "            \"businessName\": \"海淀周边\",\n" +
                        "            \"region_name\": \"海淀区\",\n" +
                        "            \"average_price\": \"\",\n" +
                        "            \"business_district\": \"0b016bc067d3ba340167d40ac9e50077\",\n" +
                        "            \"subway_station\": null,\n" +
                        "            \"line\": null,\n" +
                        "            \"business_type\": 0,\n" +
                        "            \"longitude\": \"116.227406\",\n" +
                        "            \"latitude\": \"40.079163\",\n" +
                        "            \"houseCount\": 1,\n" +
                        "            \"buildSum\": 202,\n" +
                        "            \"buildCount\": 16\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";



                String mBean1 =         " {\"msg\":\"查询成功\",\"code\":0,\"data\":[{\"id\":\"0b016bc068371f5101683c2cd2de01a8\",\"buildmsg\":\"建业写字楼\",\"map_point\":\"116.464985,39.876464\",\"average_price\":\"\",\"business_type\":0,\"delflag\":0,\"max_area\":\"\",\"min_area\":\"\",\"img\":\"ac01dbee-0c34-4f1e-9579-fee8d4aaec6c.png\",\"region_id\":110105,\"building_category\":2,\"business_district\":\"0b016bc067d3ba340167d439a51300f6\",\"businessName\":\"潘家园\",\"region_name\":\"朝阳区\",\"subway_station\":null,\"line\":null,\"longitude\":\"116.464985\",\"latitude\":\"39.876464\",\"houseCount\":1,\"regionBuildSum\":2,\"buildSum\":1},{\"id\":\"0b016bc068371f5101683c33ada001b4\",\"buildmsg\":\"劲松大厦\",\"map_point\":\"116.462312,39.883859\",\"average_price\":\"\",\"business_type\":0,\"delflag\":0,\"max_area\":\"\",\"min_area\":\"\",\"img\":\"e1dfd1f0-c5a4-48dc-9c5f-c0981432db32.png\",\"region_id\":110105,\"building_category\":2,\"business_district\":\"0b016bc067d3ba340167d4397e1200f4\",\"businessName\":\"劲松\",\"region_name\":\"朝阳区\",\"subway_station\":null,\"line\":null,\"longitude\":\"116.462312\",\"latitude\":\"39.883859\",\"houseCount\":1,\"regionBuildSum\":2,\"buildSum\":1},{\"id\":\"0b016bc068371f51016850a2d98f0322\",\"buildmsg\":\"金泰商贸大厦\",\"map_point\":\"116.397643,39.854286\",\"average_price\":\"\",\"business_type\":0,\"delflag\":0,\"max_area\":\"\",\"min_area\":\"\",\"img\":\"5f7123e3-46b0-4ae5-926c-8db226ea3b93.png\",\"region_id\":110106,\"building_category\":2,\"business_district\":\"0b016bc0685ebcc90168604359f8023e\",\"businessName\":\"木樨园\",\"region_name\":\"丰台区\",\"subway_station\":null,\"line\":null,\"longitude\":\"116.397643\",\"latitude\":\"39.854286\",\"houseCount\":1,\"regionBuildSum\":1,\"buildSum\":1},{\"id\":\"0b016bc067ee17c00167ef20c6f901b9\",\"buildmsg\":\"石景山万达广场\",\"map_point\":\"116.225362,39.90631\",\"average_price\":\"\",\"business_type\":0,\"delflag\":0,\"max_area\":\"\",\"min_area\":\"\",\"img\":\"f8ccccaa-67e8-4e75-9319-36bb47ee7855.png\",\"region_id\":110107,\"building_category\":2,\"business_district\":\"0b016bc067d3ba340167d40e35140099\",\"businessName\":\"鲁谷/八宝山\",\"region_name\":\"石景山区\",\"subway_station\":null,\"line\":null,\"longitude\":\"116.225362\",\"latitude\":\"39.90631\",\"houseCount\":2,\"regionBuildSum\":2,\"buildSum\":2},{\"id\":\"0b016bc067ee17c00167ef0c560001af\",\"buildmsg\":\"万商大厦\",\"map_point\":\"116.218786,39.906479\",\"average_price\":\"5\",\"business_type\":0,\"delflag\":0,\"max_area\":\"\",\"min_area\":\"\",\"img\":\"9976636e-75dd-47b8-a1c3-75dbca757243.png\",\"region_id\":110107,\"building_category\":2,\"business_district\":\"0b016bc067d3ba340167d40e35140099\",\"businessName\":\"鲁谷/八宝山\",\"region_name\":\"石景山区\",\"subway_station\":null,\"line\":null,\"longitude\":\"116.218786\",\"latitude\":\"39.906479\",\"houseCount\":3,\"regionBuildSum\":2,\"buildSum\":2},{\"id\":\"0b016bc068744ad501688406325d016d\",\"buildmsg\":\"北坞创新园\",\"map_point\":\"116.254763,39.980247\",\"average_price\":\"\",\"business_type\":0,\"delflag\":0,\"max_area\":\"\",\"min_area\":\"\",\"img\":\"7c6cb341-67cc-4cba-8071-fe701aa8968e.png\",\"region_id\":110108,\"building_category\":2,\"business_district\":\"0b016bc067d3ba340167d40ac9e50077\",\"businessName\":\"海淀周边\",\"region_name\":\"海淀区\",\"subway_station\":null,\"line\":null,\"longitude\":\"116.254763\",\"latitude\":\"39.980247\",\"houseCount\":1,\"regionBuildSum\":2,\"buildSum\":2},{\"id\":\"0b016bc068744ad5016883c8e03a0163\",\"buildmsg\":\"绿海大厦\",\"map_point\":\"116.226608,40.077439\",\"average_price\":\"\",\"business_type\":0,\"delflag\":0,\"max_area\":\"\",\"min_area\":\"\",\"img\":\"d5838f2e-3ec4-4074-afa3-cffd82c3ad2a.png\",\"region_id\":110108,\"building_category\":2,\"business_district\":\"0b016bc067d3ba340167d40ac9e50077\",\"businessName\":\"海淀周边\",\"region_name\":\"海淀区\",\"subway_station\":null,\"line\":null,\"longitude\":\"116.226608\",\"latitude\":\"40.077439\",\"houseCount\":1,\"regionBuildSum\":2,\"buildSum\":2}]}";

                MapPointBean data = MapPointBean.objectFromData(mBean1);


                List<RegionItem> items = new ArrayList<RegionItem>();
                List<String> isadd = new ArrayList<String>();
                isadd.clear();
                items.clear();

                for (int i = 0; i < data.data.size(); i++) {
                    if (!data.data.get(i).latitude.isEmpty() && !data.data.get(i).longitude.isEmpty()) {
//                    LogUtils.i("showMapPoint--", data.data.get(i).businessName + "-===--" + data.data.get(i).buildmsg);
                        if (zoom > 15) {
                            级别 = 3;

                            if (!isadd.contains(data.data.get(i).buildmsg)) {
                                //纬度经度 区 商圈   楼盘  房源id 楼盘下房源数量
                                items.add(new RegionItem(new LatLng(Double.parseDouble(data.data.get(i).latitude), Double.parseDouble(data.data.get(i).longitude)),
                                        data.data.get(i).region_name,//区
                                        data.data.get(i).businessName,//商圈
                                        data.data.get(i).buildmsg,//楼盘
                                        data.data.get(i).id,//房源
                                        data.data.get(i).regionBuildSum,//区下又多少楼盘
                                        data.data.get(i).buildSum,//商圈下又多少楼盘
                                        data.data.get(i).houseCount,//楼盘下房源数量
                                        级别//楼盘下房源数量
                                ));
                                isadd.add(data.data.get(i).buildmsg);
                            }


                        } else if (zoom >= 13) {
                            级别 = 2;

                            if (!isadd.contains(data.data.get(i).businessName)) {
                                //纬度经度 区 商圈   楼盘  房源id 楼盘下房源数量
                                items.add(new RegionItem(new LatLng(Double.parseDouble(data.data.get(i).latitude), Double.parseDouble(data.data.get(i).longitude)),
                                        data.data.get(i).region_name,//区
                                        data.data.get(i).businessName,//商圈
                                        data.data.get(i).buildmsg,//楼盘
                                        data.data.get(i).id,//房源
                                        data.data.get(i).regionBuildSum,//区下又多少楼盘
                                        data.data.get(i).buildSum,//商圈下又多少楼盘
                                        data.data.get(i).houseCount,//楼盘下房源数量
                                        级别//楼盘下房源数量
                                ));
                                isadd.add(data.data.get(i).businessName);
                            }
                        } else {
                            级别 = 1;
                            if (!isadd.contains(data.data.get(i).region_name)) {
                                //纬度经度 区 商圈   楼盘  房源id 楼盘下房源数量
                                items.add(new RegionItem(new LatLng(Double.parseDouble(data.data.get(i).latitude), Double.parseDouble(data.data.get(i).longitude)),
                                        data.data.get(i).region_name,//区
                                        data.data.get(i).businessName,//商圈
                                        data.data.get(i).buildmsg,//楼盘
                                        data.data.get(i).id,//房源
                                        data.data.get(i).regionBuildSum,//区下又多少楼盘
                                        data.data.get(i).buildSum,//商圈下又多少楼盘
                                        data.data.get(i).houseCount,//楼盘下房源数量
                                        级别//楼盘下房源数量
                                ));
                                isadd.add(data.data.get(i).region_name);
                            }
                        }

                    }
                }

                for (int i = 0; i <items.size() ; i++) {
                    Log.i("items--",items.get(i).getPosition()+"--"+items.get(i).区);
                }


//

                mContarct.getMarkerDataSuccess(items);


//                mContarct.getMarkerDataSuccess(mRequestMakerLatLngsList);
                //大到小

            }
        }).start();
    }

    int 级别;

    public int getZoom() {

        return 级别;
    }


}
