package myproject.com.baidumapmarker;

import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;


/**
 * @author lyx
 * @date 2018/11/13
 */

public class RegionItem implements Serializable {
    private LatLng mLatLng;
    public String 区, 商圈, 楼盘, mHousingId, mRegionBuildSum, mBuildCount, mHouseCount;



    //纬度经度 区  商圈   楼盘  房源id 楼盘下房源数量
    public RegionItem(LatLng latLng, String regionName, String title, String titlea,
                      String id, String regionBuildSum, String buildCount, String houseCount, int 级别) {


        switch (级别) {
            case 1:
                switch (regionName) {//北京每个区的中心点
                    case "东城区":
                        mLatLng = new LatLng(39.93, 116.42);
                        break;
                    case "西城区":
                        mLatLng = new LatLng(39.92, 116.37);
                        break;
                    case "崇文区":
                        mLatLng = new LatLng(39.88, 116.43);
                        break;
                    case "宣武区":
                        mLatLng = new LatLng(39.87, 116.35);
                        break;

                    case "朝阳区":
                        mLatLng = new LatLng(39.92, 116.43);
                        break;

                    case "丰台区":
                        mLatLng = new LatLng(39.85, 116.28);
                        break;
                    case "石景山区":
                        mLatLng = new LatLng(39.90, 116.22);
                        break;
                    case "海淀区":
                        mLatLng = new LatLng(39.95, 116.30);
                        break;
                    case "门头沟区":
                        mLatLng = new LatLng(39.93, 116.10);
                        break;
                    case "房山区":
                        mLatLng = new LatLng(39.75, 116.13);
                        break;
                    case "通州区":
                        mLatLng = new LatLng(39.92, 116.65);
                        break;
                    case "顺义区":
                        mLatLng = new LatLng(40.13, 116.65);
                        break;
                    case "昌平区":
                        mLatLng = new LatLng(40.22, 116.23);
                        break;
                    case "大兴区":
                        mLatLng = new LatLng(39.73, 116.33);
                        break;
                    case "怀柔区":
                        mLatLng = new LatLng(40.32, 116.63);
                        break;
                    case "平谷区":
                        mLatLng = new LatLng(40.13, 117.12);
                        break;
                    case "密云":
                        mLatLng = new LatLng(40.37, 116.83);
                        break;
                    case "延庆":
                        mLatLng = new LatLng(40.45, 115.97);
                        break;
                }

                break;
            case 2:
                mLatLng = latLng;
                break;
            case 3:
                mLatLng = latLng;
                break;
        }


        if (TextUtils.isEmpty(regionName)) {
            区 = "";
        } else {
            区 = regionName;
        }
        if (TextUtils.isEmpty(title)) {
            商圈 = "";
        } else {
            商圈 = title;
        }
        if (TextUtils.isEmpty(titlea)) {
            楼盘 = "";
        } else {
            楼盘 = titlea;
        }
        mHousingId = id;
        mRegionBuildSum = regionBuildSum;
        mBuildCount = buildCount;
        mHouseCount = houseCount;
    }

    public LatLng getPosition() {
        return mLatLng;
    }


}