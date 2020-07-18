package cc.turbosnail.permission.utils;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * utils/CoyotePermission
 * 让普通类拿到当前的上下文对象
 *
 * @author 李儒浩
 * @version V0.1.0
 * @editor 修改人
 * @team TurboSnail
 */
public class CoyotePermission {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private CoyotePermission(){

    }
    public static void bind(Context context){
        if(mContext == context){
            return;
        }
        mContext = context;
    }
    public static void unBinder(){
        if (mContext == null) throw new IllegalStateException("Bindings already cleared.");
        mContext = null;
    }
    public static Context getContext(){
        return mContext;
    }
}
