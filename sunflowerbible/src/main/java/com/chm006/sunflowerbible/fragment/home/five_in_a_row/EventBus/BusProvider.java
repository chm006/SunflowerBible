package com.chm006.sunflowerbible.fragment.home.five_in_a_row.EventBus;

import com.squareup.otto.Bus;

/**
 * Created by Xuf on 2016/1/23.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();

    private BusProvider(){

    }

    public static Bus getInstance(){
        return BUS;
    }
}
