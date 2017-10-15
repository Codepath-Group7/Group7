package com.codepath.com.sffoodtruck.ui.base.mvp;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

/**
 * Created by robl2e on 10/9/17.
 */

public abstract class AbstractMvpActivity<V extends MvpBaseView, P extends MvpBasePresenter<V>> extends MvpActivity<V,P> {

}
