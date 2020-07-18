package cc.turbosnail.permissionrequest;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;

import cc.turbosnail.permission.annotation.PermissionRequest;
import cc.turbosnail.permission.utils.CoyotePermission;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CoyotePermission.bind(this);
        new PermissionR();
//        testPermissionRequest();
    }
//    @PermissionRequest(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    public void testPermissionRequest(){
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CoyotePermission.unBinder();
    }
}

//测试普通类
class PermissionR {
    public PermissionR() {
        testPermissionRequest();
    }

    @PermissionRequest(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void testPermissionRequest() {

    }
}