package com.monster.godzilla.manager.receiver;

import android.content.Context;

import com.monster.godzilla.manager.RequestTracker;

import java.util.Collection;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>无</ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/11/21/021
 */
public class MountMonitorFactory {
    public MountMonitor build(Context context, MountMonitor.MountListener listener) {

//        final int res = context.checkCallingOrSelfPermission(Intent.ACTION_MEDIA_MOUNTED);
//        final int res2 = context.checkCallingOrSelfPermission(Intent.ACTION_MEDIA_REMOVED);
//        final int res3 = context.checkCallingOrSelfPermission( Intent.ACTION_MEDIA_EJECT);
//        final boolean hasPermission = res == PackageManager.PERMISSION_GRANTED;
//        final boolean hasPermission2 = res2 == PackageManager.PERMISSION_GRANTED;
//        final boolean hasPermission3 = res3 == PackageManager.PERMISSION_GRANTED;
//        if (hasPermission||hasPermission2||hasPermission3) {
//            return new DefaultMountMonitor(context, listener);
//        } else {
//            return new NullMountMonitor();
//        }


        return new DefaultMountMonitor(context, listener);
    }

    public static class RequestManagerMountListener implements MountMonitor.MountListener {
        private final RequestTracker requestTracker;
        public RequestManagerMountListener(RequestTracker requestTracker) {
            this.requestTracker = requestTracker;
        }


        @Override
        public void postChangeAfter(Collection<String> path) {
        }
    }
}
