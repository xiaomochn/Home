package com.xiaomo.funny.home.manager;

import com.xiaomo.funny.home.application.MyApp;

/**
 * Created by xiaomochn on 22/12/2017.
 */

public class EventManager {
    private static EventManager instance;

    private EventManager(MyApp instance) {
    }

    public static EventManager getInstance() {
        if (instance == null) {
            synchronized (EventManager.class) {
                if (instance == null) {
                    instance = new EventManager(MyApp.getInstance());
                }
            }
        }
        return instance;
    }

}
