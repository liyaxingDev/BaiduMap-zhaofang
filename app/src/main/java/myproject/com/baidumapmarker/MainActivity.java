package myproject.com.baidumapmarker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lyx
 * @date 2019/3/8
 */
public class MainActivity extends AppCompatActivity implements MainContarct {


    TextureMapView mMapView;
    Button location;
    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    private MainMapBean mMainMapBean = new MainMapBean();

    private Marker mMarker;
    private MainPresenter mMainPresenter;
    private BaiduMap mBaiduMap;
    List<LatLng> points = new ArrayList<>();
    private static final String mMARKERDATA = "MARKERDATA";


    private void init() {
        mMapView = findViewById(R.id.bmapView);
        location = findViewById(R.id.location);
        clear = findViewById(R.id.clear);
        mMainPresenter = new MainPresenter(this);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMaxAndMinZoomLevel(20, 6);
        // 隐藏logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        mMainPresenter.startLocation(mBaiduMap);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        intListener();
    }


    private void intListener() {

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle extraInfo = marker.getExtraInfo();
                RegionItem myItem = (RegionItem) extraInfo.getSerializable(mMARKERDATA);
                System.out.println("mMainMapBean.mZoom==11111-" + mMainMapBean.mZoom + "---" + myItem.getPosition());
                if (mMainMapBean.mZoom != 3) {
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(myItem.getPosition()).zoom(mMainMapBean.mZoom == 1 ? 13.5f : 15.5f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                } else {

                    Log.i("aaaa", myItem.楼盘);
                }
                //放大显示
                return false;
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                int zoom = mMainMapBean.zoomType(mapStatus.zoom);

                if (zoom != mMainMapBean.mZoom && !mMainMapBean.isCavens) {
                    mMapView.getMap().clear();
                    mMainMapBean.showMakerLatLngsList.clear();
                }
                mMainMapBean.mZoom = zoom;
                System.out.println("mMainMapBean.mZoom==222-" + mMainMapBean.mZoom);
                mMainPresenter.getMarkerData(mBaiduMap.getMapStatus().zoom);


            }
        });

    }

//    @OnClick({R.id.location, R.id.clear})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.location:
//                mMainPresenter.startLocation(mBaiduMap);
//                break;
//            case R.id.clear:
//                mMainMapBean.isCavens = false;
//                mMapView.getMap().clear();
//                mMainMapBean.showMakerLatLngsList.clear();
//                points.clear();

//                mContarct.getMarkerDataSuccess(null);
//                break;
//        }
//    }

    @Override
    public void getMarkerDataSuccess(List<RegionItem> latLngsList) {

        mMainMapBean.mRequestMakerLatLngsList.clear();
        mMainMapBean.mRequestMakerLatLngsList.addAll(latLngsList);
        addLayerMarkers();
    }


    //构建marker图标
    BitmapDescriptor bitmap = null;
    MarkerOptions ptionMarker = null;
    private TextView mTextView;

    private void addLayerMarkers() {
        mMainMapBean.mScreenLatLng.clear();
        if (mMainMapBean.isCavens) {
            mMainMapBean.mScreenLatLng.addAll(points);
        } else {
            if (mBaiduMap.getProjection() != null) {
                int scaleControlViewHeight = mMapView.getMeasuredHeight();
                int scaleControlViewWidth = mMapView.getMeasuredWidth();

                LatLng geoPoint = mBaiduMap.getProjection().fromScreenLocation(new Point(0, 0));

                mMainMapBean.mScreenLatLng.add(geoPoint);
                geoPoint = mBaiduMap.getProjection().fromScreenLocation(new Point(0, scaleControlViewWidth));
                mMainMapBean.mScreenLatLng.add(geoPoint);
                geoPoint = mBaiduMap.getProjection().fromScreenLocation(new Point(scaleControlViewWidth, scaleControlViewHeight));
                mMainMapBean.mScreenLatLng.add(geoPoint);
                geoPoint = mBaiduMap.getProjection().fromScreenLocation(new Point(scaleControlViewHeight, 0));
                mMainMapBean.mScreenLatLng.add(geoPoint);
            }
        }


        if (mTextView == null) {
            View inflate = View.inflate(MainActivity.this, R.layout.item_bubbo, null);
            mTextView = inflate.findViewById(R.id.tv_bubbo);
            mTextView.setTextColor(Color.WHITE);

        }
        mTextView.setBackgroundDrawable(getDrawAble());
        for (int i = 0; i < mMainMapBean.mRequestMakerLatLngsList.size(); i++) {

            LatLng position = mMainMapBean.mRequestMakerLatLngsList.get(i).getPosition();

            if (mMainMapBean.showMakerLatLngsList.get("" + position.latitude + position.longitude) == null) {
                if (SpatialRelationUtil.isPolygonContainsPoint(mMainMapBean.mScreenLatLng, mMainMapBean.mRequestMakerLatLngsList.get(i).getPosition())) {

                    if (mMainPresenter.getZoom() == 1) {
                        if (mMainMapBean.mRequestMakerLatLngsList.get(i).区.length() >= 3) {
                            mTextView.setText(mMainMapBean.mRequestMakerLatLngsList.get(i).区.substring(0, 3) + "\n" + mMainMapBean.mRequestMakerLatLngsList.get(i).区.substring(3, mMainMapBean.mRequestMakerLatLngsList.get(i).区.length()) + "\n" + mMainMapBean.mRequestMakerLatLngsList.get(i).mRegionBuildSum + "栋");
                        } else {
                            mTextView.setText(mMainMapBean.mRequestMakerLatLngsList.get(i).区 + "\n" + mMainMapBean.mRequestMakerLatLngsList.get(i).mRegionBuildSum + "栋");
                        }

                    } else if (mMainPresenter.getZoom() == 2) {
                        if (mMainMapBean.mRequestMakerLatLngsList.get(i).商圈.length() >= 3) {
                            mTextView.setText(mMainMapBean.mRequestMakerLatLngsList.get(i).商圈.substring(0, 3) + "\n" + mMainMapBean.mRequestMakerLatLngsList.get(i).商圈.substring(3, mMainMapBean.mRequestMakerLatLngsList.get(i).商圈.length()) + "\n" + mMainMapBean.mRequestMakerLatLngsList.get(i).mBuildCount + "栋");
                        } else {
                            mTextView.setText(mMainMapBean.mRequestMakerLatLngsList.get(i).商圈 + "\n" + mMainMapBean.mRequestMakerLatLngsList.get(i).mBuildCount + "栋");
                        }
                    } else {
                        if (mMainMapBean.mRequestMakerLatLngsList.get(i).楼盘.length() >= 3) {
                            mTextView.setText(mMainMapBean.mRequestMakerLatLngsList.get(i).楼盘.substring(0, 3) + "\n" + mMainMapBean.mRequestMakerLatLngsList.get(i).楼盘.substring(3, mMainMapBean.mRequestMakerLatLngsList.get(i).楼盘.length()) + "\n" + mMainMapBean.mRequestMakerLatLngsList.get(i).mHouseCount + "套");
                        } else {
                            mTextView.setText(mMainMapBean.mRequestMakerLatLngsList.get(i).楼盘 + "\n" + mMainMapBean.mRequestMakerLatLngsList.get(i).mHouseCount + "套");
                        }
                    }
                    mTextView.setGravity(Gravity.CENTER);
                    bitmap = BitmapDescriptorFactory.fromView(mTextView);

                    // 构建MarkerOption，用于在地图上添加Marker o
                    ptionMarker = new MarkerOptions().icon(bitmap).position(mMainMapBean.mRequestMakerLatLngsList.get(i).getPosition());
                    // 生长动画
                    ptionMarker.animateType(MarkerOptions.MarkerAnimateType.grow);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(mMARKERDATA, mMainMapBean.mRequestMakerLatLngsList.get(i));
                    ptionMarker.extraInfo(bundle);

                    // 在地图上添加Marker，并显示
                    mBaiduMap.addOverlay(ptionMarker);
                    //设置Marker覆盖物的ZIndex
                    mMainMapBean.showMakerLatLngsList.put("" + position.latitude + position.longitude, mMainMapBean.mRequestMakerLatLngsList.get(i));
                    ptionMarker.zIndex(i);
                }

            }
        }

    }

    private Drawable mBitmapDrawable;

    public Drawable getDrawAble() {
        int radius = dp2px(this, 35);
        if (mBitmapDrawable == null) {
            mBitmapDrawable = createRoundImageWithBorder(drawCircle(radius,
                    getResources().getColor(R.color.FEAC2C)));
        }
        return mBitmapDrawable;
    }

    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private Bitmap drawCircle(int radius, int color) {
        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
        paint.setColor(color);
        canvas.drawArc(rectF, 0, 360, true, paint);
        return bitmap;
    }

    private Drawable createRoundImageWithBorder(Bitmap bitmap) {
        //原图宽度
        int bitmapWidth = bitmap.getWidth();
        //原图高度
        int bitmapHeight = bitmap.getHeight();
        //边框宽度 pixel
        int borderWidthHalf = 30;

        //转换为正方形后的宽高
        int bitmapSquareWidth = Math.min(bitmapWidth, bitmapHeight);

        //最终图像的宽高
        int newBitmapSquareWidth = bitmapSquareWidth + borderWidthHalf;

        Bitmap roundedBitmap = Bitmap.createBitmap(newBitmapSquareWidth, newBitmapSquareWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundedBitmap);
        int x = borderWidthHalf + bitmapSquareWidth - bitmapWidth;
        int y = borderWidthHalf + bitmapSquareWidth - bitmapHeight;

        //裁剪后图像,注意X,Y要除以2 来进行一个中心裁剪
        canvas.drawBitmap(bitmap, x / 2, y / 2, null);
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidthHalf);
        borderPaint.setColor(getResources().getColor(R.color.D4FAC16F));

        //添加边框
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getWidth() / 2, newBitmapSquareWidth / 2, borderPaint);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), roundedBitmap);
        roundedBitmapDrawable.setGravity(Gravity.CENTER);
        roundedBitmapDrawable.setCircular(true);
        return roundedBitmapDrawable;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();

        showGPSContacts();

    }

    /**
     * 检测GPS、位置权限是否开启
     */
    public void showGPSContacts() {

    }



    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }


}
