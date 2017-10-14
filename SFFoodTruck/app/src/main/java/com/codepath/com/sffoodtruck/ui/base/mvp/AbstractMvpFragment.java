package com.codepath.com.sffoodtruck.ui.base.mvp;


import com.hannesdorfmann.mosby3.mvp.MvpFragment;

/**
 * Created by robl2e on 10/9/17.
 */

public abstract class AbstractMvpFragment<V extends MvpBaseView, P extends MvpBasePresenter<V>>
        extends MvpFragment<V, P> {
}
