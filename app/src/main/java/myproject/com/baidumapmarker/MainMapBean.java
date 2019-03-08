package myproject.com.baidumapmarker;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lyx
 * @date 2018/11/13
 */
public class MainMapBean {
    public List<RegionItem> mRequestMakerLatLngsList = new ArrayList<>();
    public HashMap<String, RegionItem> showMakerLatLngsList = new HashMap<>();
    public List<LatLng> mScreenLatLng = new ArrayList<>();
    public int mZoom = 1;
    public boolean isCavens;

    public int zoomType(float zoom) {
        if (zoom > 15) {
            return 3;
        } else if (zoom >= 13) {
            return 2;
        } else {
            return 1;
        }

    }
}
