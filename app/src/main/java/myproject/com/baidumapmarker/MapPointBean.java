package myproject.com.baidumapmarker;

import com.google.gson.Gson;

import java.util.List;

/**
 * @author lyx
 * @date 2018/11/13
 */
public class MapPointBean {

    /**
     * msg : 查询成功
     * total : 3
     * code : 0
     * data : [{"min_area":"","max_area":"","map_point":"116.272554,39.88891","buildmsg":"金隅大成时代写字楼","id":"0b016bc067ce82f20167cf6a0c7300af","buildImg":"77f53c3f-2978-4118-94bc-24369cc2dea0.png","businessName":"青塔","region_name":"丰台区","average_price":"25","business_district":"0b016bc067d3ba340167d3fa07100065","subway_station":null,"line":null,"business_type":0,"longitude":"116.272554","latitude":"39.88891","houseCount":2},{"min_area":"","max_area":"","map_point":"116.225362,39.90631","buildmsg":"石景山万达广场","id":"0b016bc067ee17c00167ef20c6f901b9","buildImg":"f8ccccaa-67e8-4e75-9319-36bb47ee7855.png","businessName":"鲁谷/八宝山","region_name":"石景山区","average_price":"","business_district":"0b016bc067d3ba340167d40e35140099","subway_station":null,"line":null,"business_type":0,"longitude":"116.225362","latitude":"39.90631","houseCount":2},{"min_area":"","max_area":"","map_point":"116.218786,39.906479","buildmsg":"万商大厦","id":"0b016bc067ee17c00167ef0c560001af","buildImg":"9976636e-75dd-47b8-a1c3-75dbca757243.png","businessName":"鲁谷/八宝山","region_name":"石景山区","average_price":"5","business_district":"0b016bc067d3ba340167d40e35140099","subway_station":null,"line":null,"business_type":0,"longitude":"116.218786","latitude":"39.906479","houseCount":3}]
     */

    public String msg;
    public int total;
    public int code;
    public List<DataBean> data;

    public static MapPointBean objectFromData(String str) {

        return new Gson().fromJson(str, MapPointBean.class);
    }


    public static class DataBean {

        /**
         * min_area :
         * max_area :
         * map_point : 116.218786,39.906479
         * buildmsg : 万商大厦
         * id : 0b016bc067ee17c00167ef0c560001af
         * buildImg : 9976636e-75dd-47b8-a1c3-75dbca757243.png
         * region_id : 110107
         * businessName : 鲁谷/八宝山
         * region_name : 石景山区
         * average_price : 5
         * business_district : 0b016bc067d3ba340167d40e35140099
         * subway_station : null
         * line : null
         * business_type : 0
         * longitude : 116.218786
         * latitude : 39.906479
         * houseCount : 3
         * buildSum : 40
         * buildCount : 12
         */

        public String min_area;
        public String max_area;
        public String map_point;
        public String buildmsg;
        public String id;
        public String buildImg;
        public int region_id;
        public String businessName;
        public String region_name;
        public String average_price;
        public String business_district;
        public Object subway_station;
        public Object line;
        public int business_type;
        public String longitude;
        public String latitude;
        public String houseCount;
        public String regionBuildSum;
        public String buildSum;
    }

    @Override
    public String toString() {
        return "MapPointBean{" +
                "msg='" + msg + '\'' +
                ", total=" + total +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
