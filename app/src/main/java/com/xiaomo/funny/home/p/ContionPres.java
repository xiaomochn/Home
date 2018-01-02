package com.xiaomo.funny.home.p;

import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.thread.EventThread;
import com.xiaomo.funny.home.application.MyApp;
import com.xiaomo.funny.home.model.ContionPModel;
import com.xiaomo.funny.home.model.Event;
import com.xiaomo.funny.home.v.IContionView;

import org.w3c.dom.Text;

/**
 * Created by xiaomochn on 02/01/2018.
 */

public class ContionPres implements IContionPre {
    private IContionView mIContionView;

    public ContionPres(IContionView mIContionView) {
        this.mIContionView = mIContionView;
        MyApp.getBus().register(this);
    }

    @Override
    public void setView() {

    }

    @Override
    public void onDestory() {
        MyApp.getBus().unregister(this);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD
    )
    public void eatMore(ContionPModel event) {
        TextView textView = mIContionView.getStatusTextView();
        textView.setText(textView.getText()+event.getCommond());
    }

}
