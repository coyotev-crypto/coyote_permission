# coyote_permission
android 权限申请框架
Add it in your root build.gradle at the end of repositories:

	dependencies {
		...
		classpath "com.android.tools.build:gradle:3.5.2"
		classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.6'
	}
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. 添加dependency

	apply plugin: 'android-aspectjx'  //在顶部添加
	
	dependencies {
	        implementation 'com.github.lrhcoyote:coyote_permission:version'
	}

使用方法：
	注解说明
	PermissionRequest 请求权限注解 value 需要请求的权限支持多权限请求  requestCode 请求码
	PermissionCancel  用户取消  
	PermissionDenied  用户拒绝
在Activity或Fragment中使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionRequest();
    }
    //在某个操作在运行时需要权限在去申请
    @PermissionRequest(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void methodName(){}
    //用户取消之后的操作
    @PermissionCancel
    public void methodName(){}
    
在普通类中使用这里使用Activity来示例Fragment也一样：

   public class MainActivity extends AppCompatActivity {
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CoyotePermission.bind(this);
        new PermissionRequests().permissionRequest();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CoyotePermission.unBinder();
    }
    
 }


 class PermissionRequests{
 
    public PermissionRequests(){}
    @PermissionRequest(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void permissionRequest(){}
    
 }
    
