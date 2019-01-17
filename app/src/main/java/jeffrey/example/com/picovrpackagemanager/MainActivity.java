package jeffrey.example.com.picovrpackagemanager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public TextView textView1;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView1);
    }
    public void listClick(View view) {

        packagelist();

    }

    public void packagelist(){

        List<AppInfo> apklist = getAllAppList();
        String  list = "";
        for(AppInfo appInfo : apklist){
            Log.i(TAG, appInfo.toString());
            list += appInfo.toString();
        }
        textView1.setText(list);

    }

    public List<AppInfo> getAllAppList(){
        PackageManager packageManager = getPackageManager();
        List<AppInfo> allAppsList = new ArrayList<AppInfo>();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : list) {
            AppInfo appInfo = new AppInfo();
            appInfo.setPackageName(resolveInfo.activityInfo.packageName);//package name
            appInfo.setActivityName(resolveInfo.activityInfo.name);//class name
            appInfo.setAppName(resolveInfo.loadLabel(packageManager).toString());//app name
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(resolveInfo.activityInfo.packageName, 0);
                appInfo.setInstallTime(packageInfo.firstInstallTime);//install time
                if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0){
                    appInfo.setSystemFlag(0);//is not system app
                }else{
                    appInfo.setSystemFlag(1);//is system app
                }
                ComponentName componentName = new ComponentName(
                        resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
//                Drawable drawable = (Drawable) packageManager.getActivityIcon(componentName);
//                appInfo.setIcon(drawable.getmip());//icon
//                drawable = null;
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            allAppsList.add(appInfo);
        }
        Collections.sort(allAppsList,new Comparator<AppInfo>(){
            public int compare(AppInfo arg0, AppInfo arg1) {
                Long long0 = arg0.getInstallTime();
                Long long1 = arg1.getInstallTime();
                return long1.compareTo(long0);
            }
        });
//		for(AppInfo appInfo : allAppsList){
//			Log.i(TAG, appInfo.toString());
//		}
        return allAppsList;
    }
}
