package cc.turbosnail.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * common/annotation/PermissionNeed
 * 是否需要我们的权限
 * @author 李儒浩
 * @version V0.1.0
 * @editor 修改人
 * @team TurboSnail
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionRequest {
    String[] value();     //权限数组
    int requestCode() default 0; //是否已经请求
}
