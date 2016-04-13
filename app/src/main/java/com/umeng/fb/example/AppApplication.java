package com.umeng.fb.example;

        import android.app.Application;
        import com.umeng.fb.push.FeedbackPush;


public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FeedbackPush.getInstance(this).init(false);
//        AppPush.getInstance(this).init();
    }
}
