package toast.test.android.bpyc.com.myapplication;

import android.app.Application;

import toast.test.android.bpyc.com.myapplication.Util.StorageUtil;

/**
 * Created by xyzj on 2017/1/3.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StorageUtil.getInstance().init(getApplicationContext());

    }
}
