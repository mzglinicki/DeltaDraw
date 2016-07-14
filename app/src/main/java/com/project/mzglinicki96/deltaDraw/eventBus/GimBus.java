package com.project.mzglinicki96.deltaDraw.eventBus;

import com.pgssoft.gimbus.EventBus;

/**
 * Created by mzglinicki.96 on 25.03.2016.
 */
public class GimBus {

    private static EventBus gimBus = null;

    private GimBus() {
    }

    public static EventBus getInstance() {

        if (gimBus == null) {
            gimBus = new EventBus();
        }
        return gimBus;
    }
}