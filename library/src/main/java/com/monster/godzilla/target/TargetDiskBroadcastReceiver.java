package com.monster.godzilla.target;

import com.monster.godzilla.interfaces.XFunc1;
import com.monster.godzilla.manager.Request;

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
 * @date 2018/11/22/022
 */
public class TargetDiskBroadcastReceiver implements Target.RequestBroadcastReceiverDiskTargetImpl,Target{
    XFunc1<Collection<String>> xFunc1;
    Request request;
     TargetDiskBroadcastReceiver(RequestBroadcastReceiverDiskTargetImpl xFunc1) {
         this.xFunc1=xFunc1;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        if(request!=null){
            request.clear();
        }
        xFunc1=null;
    }

    @Override
    public void setRequest(Request request) {
        this.request=request;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public void call(Collection<String> diskAddPath) {
        if(xFunc1!=null){
            xFunc1.call(diskAddPath);
        }
    }

}
