package cc.turbosnail.permission.aop;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import cc.turbosnail.permission.PermissionRequestActivity;
import cc.turbosnail.permission.annotation.PermissionCancel;
import cc.turbosnail.permission.annotation.PermissionDenied;
import cc.turbosnail.permission.annotation.PermissionRequest;
import cc.turbosnail.permission.utils.CoyotePermission;
import cc.turbosnail.permission.utils.PermissionUtil;

/**
 * common/aop/PermissionAsecpt
 * 切面类
 *
 * @author 李儒浩
 * @version V0.1.0
 * @editor 修改人
 * @team TurboSnail
 */
@Aspect
public class PermissionAspect {
    private static final String TAG = "PermissionAspect";

    /**
     * Pointcut 定义切入点
     * MethodSignature @注解 方法访问权限 返回值类型 类名.函数名（参数）
     *
     * @param permissionRequest PermissionRequest
     * @annotation(permissionNeed) 表示我只看注解 相当于是PermissionNeed注解对象
     * 执行完requestPermission ->aroundJoinPoint
     */
    @Pointcut("execution(@cc.turbosnail.permission.annotation.PermissionRequest * *(..)) && @annotation(permissionRequest)")
    public void requestPermission(PermissionRequest permissionRequest) {
    }

    /**
     * 使用PermissionNeed最终执行方法   aroundJoinPoint通知方法
     *
     * @param joinPoint      aop必写参数  存放了连接点和代理对象的一下信息
     * @param permissionRequest permissionRequest
     * @Around("requestPermission(PermissionRequest)") == @Around("execution(@cc.turbosnail.permission.annotation.PermissionNeed * *(..)) && @annotation(permissionNeed)")
     * ProceedingJoinPoint API
     * String toString();         //连接点所在位置的相关信息
     * String toShortString();     //连接点所在位置的简短相关信息
     * String toLongString();     //连接点所在位置的全部相关信息
     * Object getThis();         //返回AOP代理对象，也就是com.sun.proxy.$Proxy18
     * Object getTarget();       //返回目标对象，一般我们都需要它或者（也就是定义方法的接口或类，为什么会是接口呢？这主要是在目标对象本身是动态代理的情况下，例如Mapper。所以返回的是定义方法的对象如aoptest.daoimpl.GoodDaoImpl或com.b.base.BaseMapper<T, E, PK>）
     * Object[] getArgs();       //返回被通知方法参数列表
     * Signature getSignature();  //返回当前连接点签名  其getName()方法返回方法的FQN，如void aoptest.dao.GoodDao.delete()或com.b.base.BaseMapper.insert(T)(需要注意的是，很多时候我们定义了子类继承父类的时候，我们希望拿到基于子类的FQN，这直接可拿不到，要依赖于AopUtils.getTargetClass(point.getTarget())获取原始代理对象，下面会详细讲解)
     * SourceLocation getSourceLocation();//返回连接点方法所在类文件中的位置
     * String getKind();        //连接点类型
     * StaticPart getStaticPart(); //返回连接点静态部分
     */
    @Around("requestPermission(permissionRequest)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, PermissionRequest permissionRequest) {
        final Object obj = joinPoint.getThis();
        Context context = null;
        if (obj instanceof Fragment) {
            context = ((Fragment) obj).getContext();
        } else if (obj instanceof Context) {
            context = (Context) obj;
        }
        if(context == null){
            context = CoyotePermission.getContext();
        }
        if (context == null || permissionRequest == null) {
            Log.e("ContextError:", "Context is empty");
            throw new RuntimeException("Context is empty");
        }
        //申请权限
        PermissionRequestActivity.startPermissionRequest(context, permissionRequest.value(), permissionRequest.requestCode(), new IPermission() {
            @Override
            public void onPermissionGranted() {
                //执行切入点方法也就是执行了加了PermissionNeed注解的方法
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onPermissionDenied(int requestCode) {
                /**
                 * 通过反射注解的方式调用方法
                 * 这里不传Context 是因为如果是Context的话普通类就用不了有限制
                 */
                PermissionUtil.invokeAnnotation(obj, PermissionDenied.class, requestCode);
            }

            @Override
            public void onPermissionCanceled(int requestCode) {
                PermissionUtil.invokeAnnotation(obj, PermissionCancel.class, requestCode);
            }
        });
    }
}
