package cc.turbosnail.permission.aop;

/**
 * aop/IPermission
 * 权限请求结果回调接口
 *
 * @author 李儒浩
 * @version V0.1.0
 * @editor 修改人
 * @team TurboSnail
 */
public interface IPermission {
    //同意权限
    void onPermissionGranted();
    //拒绝权限且不在提示
    void onPermissionDenied(int requestCode);
    //取消权限申请
    void onPermissionCanceled(int requestCode);
}
