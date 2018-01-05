package com.zhoutianchu.framework.intf;

import java.util.List;

/**
 * Created by zhout on 2018/1/5.
 */

public interface PermissionListener {
    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
