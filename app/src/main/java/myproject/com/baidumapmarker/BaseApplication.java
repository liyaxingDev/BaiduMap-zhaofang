package myproject.com.baidumapmarker;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * @author lyx
 * @date 2019/3/8
 */
public class BaseApplication extends Application {

    public static BaseApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        SDKInitializer.initialize(this);

    }
}
