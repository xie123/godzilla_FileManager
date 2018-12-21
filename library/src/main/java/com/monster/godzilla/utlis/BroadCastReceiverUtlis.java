package com.monster.godzilla.utlis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
 * @date 2018/11/20/020
 */
public class BroadCastReceiverUtlis {


   public static void registerReceiver(@NonNull Context context, @Nullable BroadcastReceiver receiver, @NonNull IntentFilter filter) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(receiver);
        Preconditions.checkNotNull(filter);
        try {
            context.registerReceiver(receiver, filter);
        } catch (Exception ignored) {
        }
    }

    public static void unregisterReceiver(@NonNull Context context, @Nullable BroadcastReceiver receiver) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(receiver);
        try {
            context.unregisterReceiver(receiver);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
