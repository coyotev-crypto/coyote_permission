package cc.turbosnail.permission;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import cc.turbosnail.permission.aop.IPermission;
import cc.turbosnail.permission.utils.PermissionUtil;

/**
 * permission/PermissionRequestActivity
 * 权限申请的Activity
 *
 * @author 李儒浩
 * @version V0.1.0
 * @editor 修改人
 * @team TurboSnail
 */
public class PermissionRequestActivity extends Activity {
    private static IPermission mIPermission;
    private static final String PERMISSION = "permission";
    private static final String REQUEST_CODE = "request_code";
    private static final String STATUS_BAR_COLOR = "status_bar_color";
    //开启权限请求
    public static void  startPermissionRequest(Context context, String[] permissions, int requestCode,
                                               IPermission iPermission){
        mIPermission = iPermission;
        Intent intent = new Intent(context, PermissionRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);  //设置为隐藏的Activity
        Bundle bundle = new Bundle();
        bundle.putStringArray(PERMISSION,permissions);
        bundle.putInt(REQUEST_CODE,requestCode);
        if(context instanceof Activity){
            Activity activity = (Activity) context;
            Window window = activity.getWindow();
            int statusBarColor = window.getStatusBarColor(); //拿到标题定义的状态栏颜色
            bundle.putInt(STATUS_BAR_COLOR,statusBarColor);
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
        //屏蔽动画
        if(context instanceof Activity){
            ((Activity) context).overridePendingTransition(0,0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            finish();
            return;
        }
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(bundle.getInt(STATUS_BAR_COLOR)); //设置顶部状态栏的颜色

        String[] permissions = bundle.getStringArray(PERMISSION);
        int requestCode = bundle.getInt(REQUEST_CODE);
        if(permissions == null || mIPermission == null){
            finish();
            overridePendingTransition(0,0);
            return;
        }
        requestPermission(permissions,requestCode);
    }

    private void requestPermission(String[] permissions, int requestCode) {
        if(PermissionUtil.hasSelfPermissions(this,permissions)){
            //权限已经有了
            mIPermission.onPermissionGranted();
            finish();
            overridePendingTransition(0,0);
        }else {
            //没权限,请求权限
            ActivityCompat.requestPermissions(this,permissions,requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PermissionUtil.verifyPermissions(grantResults)){
            mIPermission.onPermissionGranted(); //申请成功
        }else {
            if(PermissionUtil.shouldShowRequestPermissionRationale(this,permissions)){
                mIPermission.onPermissionCanceled(requestCode); //用户取消
            }else {
                mIPermission.onPermissionDenied(requestCode); //用户拒绝
            }
        }
        finish();
        overridePendingTransition(0,0);
    }
}
