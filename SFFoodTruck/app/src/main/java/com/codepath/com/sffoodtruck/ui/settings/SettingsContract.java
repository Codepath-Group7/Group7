package com.codepath.com.sffoodtruck.ui.settings;

/**
 * Created by saip92 on 10/14/2017.
 */

public interface SettingsContract {

    interface View{
        void showPlacePicker();
    }


    interface Presenter{
        void loadMap();
    }
}
