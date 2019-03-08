package myproject.com.baidumapmarker;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import static com.baidu.location.BDLocation.LOCATION_WHERE_UNKNOW;

/**
 * @author lyx
 * @date 2018/11/13
 */
public class LocationUtils {
    private static LocationUtils mLocationUtils = new LocationUtils();
    private final LocationClient mLocationClient;
    private OnLocationListener mOnLocationListener;
    public static BDLocation mLocalLocation;
    public static double mLongitude;
    public static double mLatitude;

    public static LocationUtils getInstance() {
        return mLocationUtils;
    }

    private LocationUtils() {
        mLocationClient = new LocationClient(BaseApplication.mApp);
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {


            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location != null) {
                    int errorCode = location.getLocType();
                    if (LOCATION_WHERE_UNKNOW == errorCode) {
                        if (mOnLocationListener != null) {
                            mOnLocationListener.onFaild();
                        }
                        return;
                    }

                    mLocalLocation = location;
                    mLatitude = location.getLatitude();
                    mLongitude = location.getLongitude();
                    if (mOnLocationListener != null) {
                        mOnLocationListener.onSuccess(location);
                    }


                } else {
                    if (mOnLocationListener != null) {
                        mOnLocationListener.onFaild();
                    }
                }
            }
        });

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        option.setOpenGps(true);

        option.setLocationNotify(true);

        mLocationClient.setLocOption(option);

    }

    public void start() {
        mLocationClient.start();

    }

    public void stop() {
        mLocationClient.stop();

    }

    public LocationUtils setOnLocationListener(OnLocationListener onLocationListener) {
        mOnLocationListener = onLocationListener;
        return getInstance();
    }

    public interface OnLocationListener {
        void onSuccess(BDLocation location);

        void onFaild();
    }
}
