package cc.turbosnail.permission.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * utils/PermissionUtil
 * 权限申请判断类
 *
 *
 * 危险权限列表及说明
 * android.permission.READ_CALENDAR允许程序读取用户日历数据
 * android.permission.WRITE_CALENDAR允许一个程序写入但不读取用户日历数据
 * android.permission.CAMERA，允许访问摄像头进行拍照
 * android.permission.READ_CONTACTS允许程序读取用户联系人数据
 * android.permission.WRITE_CONTACTS允许程序写入但不读取用户联系人数据
 * android.permission.GET_ACCOUNTS访问一个帐户列表在Accounts Service中
 * android.permission.ACCESS_FINE_LOCATION允许一个程序访问精良位置(如GPS)
 * android.permission.ACCESS_COARSE_LOCATION允许一个程序访问CellID或WiFi热点来获取粗略的位置
 * android.permission.RECORD_AUDIO允许程序录制音频
 * android.permission.CALL_PHONE允许一个程序初始化一个电话拨号不需通过拨号用户界面需要用户确认
 * android.permission.READ_PHONE_STATE 访问电话状态
 * android.permission.READ_CALL_LOG  查看电话日志
 * android.permission.WRITE_CALL_LOG写入电话日志
 * android.permission.ADD_VOICEMAIL  允许应用程序添加系统中的语音邮件
 * android.permission.USE_SIP  允许程序使用SIP视频服务
 * android.permission.PROCESS_OUTGOING_CALLS   允许应用程序监视、修改、忽略拨出的电话
 * android.permission.BODY_SENSORS  允许该应用存取监测您身体状况的传感器所收集的数据，例如您的心率
 * android.permission.SEND_SMS允许程序发送SMS短信
 * android.permission.RECEIVE_SMS允许程序监控一个将收到短信息，记录或处理
 * android.permission.READ_SMS允许程序读取短信息
 * android.permission.RECEIVE_WAP_PUSH允许程序监控将收到WAP PUSH信息
 * android.permission.RECEIVE_MMS允许一个程序监控将收到MMS彩信,记录或处理
 * android.permission.WRITE_EXTERNAL_STORAGE   允许程序写入外部存储，如SD卡上写文件
 * android.permission.READ_EXTERNAL_STORAGE   访问您设备上的照片、媒体内容和文件
 * @author 李儒浩
 * @version V0.1.0
 * @editor 修改人
 * @team TurboSnail
 */
public class PermissionUtil {

    /**
     * 判断所有权限是否都同意，都同意返回true 否则返回false
     *
     * @param context     context
     * @param permissions permissions List
     * @return return true if all permission granted else false
     */
    public static boolean hasSelfPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否有该权限
     *
     * @param context    上下文对象
     * @param permission 权限
     * @return
     */
    public static boolean hasPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    //验证权限是否请求成功
    public static boolean verifyPermissions(int... grantResult) {
        if (grantResult.length == 0) return false;
        for (int result : grantResult) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否显示权限提醒
     *
     * @param activity    Activity
     * @param permissions 权限
     * @return 如果某个权限需要return true
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过反射调用指定方法
     */
    public static void invokeAnnotation(Object object, Class annotationClass, int requestCode) {
        Class<?> clzz = object.getClass();
        Method[] methods = clzz.getDeclaredMethods();
        if (methods.length == 0) {
            return;
        }
        for (Method method : methods) {
            boolean isHasAnnotation = method.isAnnotationPresent(annotationClass);
            if (isHasAnnotation) {
                Class<?>[] parameterType = method.getParameterTypes();
                if (parameterType.length != 1) {
                    throw new RuntimeException("There is only one int parameter");
                }
                method.setAccessible(true);
                try {
                    method.invoke(object, requestCode);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
