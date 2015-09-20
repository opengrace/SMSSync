/*
 * Copyright (c) 2010 - 2015 Ushahidi Inc
 * All rights reserved
 * Contact: team@ushahidi.com
 * Website: http://www.ushahidi.com
 * GNU Lesser General Public License Usage
 * This file may be used under the terms of the GNU Lesser
 * General Public License version 3 as published by the Free Software
 * Foundation and appearing in the file LICENSE.LGPL included in the
 * packaging of this file. Please review the following information to
 * ensure the GNU Lesser General Public License version 3 requirements
 * will be met: http://www.gnu.org/licenses/lgpl.html.
 *
 * If you have questions regarding the use of this file, please contact
 * Ushahidi developers at team@ushahidi.com.
 */

package org.addhen.smssync.presentation.view.ui.fragment;

import com.addhen.android.raiburari.presentation.ui.fragment.BaseFragment;

import org.addhen.smssync.R;
import org.addhen.smssync.data.twitter.TwitterClient;
import org.addhen.smssync.presentation.di.component.FilterComponent;
import org.addhen.smssync.presentation.model.FilterModel;
import org.addhen.smssync.presentation.model.WebServiceModel;
import org.addhen.smssync.presentation.presenter.ListFilterPresenter;
import org.addhen.smssync.presentation.util.Utility;
import org.addhen.smssync.presentation.view.filters.ListFilterView;
import org.addhen.smssync.presentation.view.ui.activity.MainActivity;
import org.addhen.smssync.presentation.view.ui.navigation.Launcher;
import org.addhen.smssync.presentation.view.ui.widget.FilterKeywordsView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.widget.LinearLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class FilterFragment extends BaseFragment implements
        ListFilterView {

    @Inject
    ListFilterPresenter mListFilterPresenter;

    @Inject
    Launcher mLauncher;

    @Inject
    TwitterClient mTwitterClient;

    @Bind(R.id.custom_integration_filter_container)
    LinearLayout mFilterViewGroup;

    @Bind(R.id.black_list)
    FilterKeywordsView mBlackListFilterKeywordsView;

    @Bind(R.id.white_list)
    FilterKeywordsView mWhiteListFilterKeywordsView;

    private static FilterFragment mFilterFragment;

    public FilterFragment() {
        super(R.layout.fragment_filter_list, 0);
    }

    public static FilterFragment newInstance() {
        if (mFilterFragment == null) {
            mFilterFragment = new FilterFragment();
        }
        return mFilterFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mListFilterPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mListFilterPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListFilterPresenter.destroy();
    }

    private void initialize(Bundle savedInstanceState) {
        getFilterComponent(FilterComponent.class).inject(this);
        mListFilterPresenter.setView(this);
    }


    @Override
    public void showFilters(List<FilterModel> filterModelList) {
        int whiteListCount = 0;
        int blackListCount = 0;
        if (!Utility.isEmpty(filterModelList)) {
            for (FilterModel filterModel : filterModelList) {
                if (filterModel.status == FilterModel.Status.WHITELIST) {
                    whiteListCount += 1;
                } else {
                    blackListCount += 1;
                }
            }
        }
        initBlackListFilters(blackListCount);
        initWhiteListFilters(whiteListCount);
    }

    @Override
    public void showCustomWebService(List<WebServiceModel> webServiceModels) {
        if (!Utility.isEmpty(webServiceModels)) {
            for (WebServiceModel webServiceModel : webServiceModels) {
                initCustomIntegrationFilters(webServiceModel.getTitle(),
                        R.drawable.ic_web_grey_900_24dp, 0);
            }
        }
    }

    @Override
    public void showError(String s) {
        showSnabackar(getView(), s);
    }

    @Override
    public Context getAppContext() {
        return getContext().getApplicationContext();
    }

    protected <C> C getFilterComponent(Class<C> componentType) {
        return componentType.cast(((MainActivity) getActivity()).getFilterComponent());
    }

    private void initTwitterView() {
        if ((mTwitterClient != null) && (mTwitterClient.getSessionManager().getActiveSession()
                != null)) {
            //// TODO: Look into moving this into a reusable method
            FilterKeywordsView filterKeywordsView = new FilterKeywordsView(
                    getAppContext());

            final SwitchCompat filterKeywordsSwitch = filterKeywordsView
                    .getSwitchCompat();
            filterKeywordsView.setSwitchListener(v -> {
                if (filterKeywordsSwitch.isChecked()) {
                    // TODO: Enable filter
                } else {
                    // TODO: Disable filter
                }
            });

            final AppCompatTextView title = filterKeywordsView.getTitle();
            title.setText(R.string.twitter);
            final Drawable customWebServiceDrawable = ContextCompat.getDrawable(getContext(),
                    R.drawable.ic_twitter_blue_24dp);
            title.setCompoundDrawablesWithIntrinsicBounds(customWebServiceDrawable, null, null,
                    null);
            AppCompatTextView filterKeywordCount = filterKeywordsView
                    .getFilterKeywordCount();
            filterKeywordCount.setText(0);
            filterKeywordCount.setOnClickListener(v -> {
                mLauncher.launchAddKeyword();
            });

            AppCompatTextView filterKeyword = filterKeywordsView.getFilterKeyword();
            filterKeyword.setText(R.string.keywords);
            filterKeyword.setOnClickListener(v -> {
                mLauncher.launchAddKeyword();
            });

            mFilterViewGroup.addView(filterKeywordsView);
        }
    }

    private void initBlackListFilters(int count) {
        final SwitchCompat filterKeywordsSwitch = mBlackListFilterKeywordsView
                .getSwitchCompat();
        mBlackListFilterKeywordsView.setSwitchListener(v -> {
            if (filterKeywordsSwitch.isChecked()) {
                // TODO: Enable filter
            } else {
                // TODO: Disable filter
            }
        });

        AppCompatTextView filterKeywordCount = mBlackListFilterKeywordsView.getFilterKeywordCount();
        filterKeywordCount.setText(count);
        filterKeywordCount.setOnClickListener(v -> {
            // TODO: Launch activity for managing filters
        });

        AppCompatTextView filterKeyword = mBlackListFilterKeywordsView.getFilterKeyword();
        filterKeyword.setText(R.string.keywords);
        filterKeyword.setOnClickListener(v -> {
            // TODO: Launch activity for managing filters
        });
        mBlackListFilterKeywordsView.getFilterKeywordCount().setText(count);
    }

    private void initWhiteListFilters(int count) {
        final SwitchCompat filterKeywordsSwitch = mWhiteListFilterKeywordsView
                .getSwitchCompat();
        mWhiteListFilterKeywordsView.setSwitchListener(v -> {
            if (filterKeywordsSwitch.isChecked()) {
                // TODO: Enable filter
            } else {
                // TODO: Disable filter
            }
        });

        AppCompatTextView filterKeywordCount = mWhiteListFilterKeywordsView.getFilterKeywordCount();
        filterKeywordCount.setText(count);
        filterKeywordCount.setOnClickListener(v -> {
            mLauncher.launchAddPhoneNumber();
        });

        AppCompatTextView filterKeyword = mWhiteListFilterKeywordsView.getFilterKeyword();
        filterKeyword.setText(R.string.keywords);
        filterKeyword.setOnClickListener(v -> {
            mLauncher.launchAddPhoneNumber();
        });

        mWhiteListFilterKeywordsView.getFilterKeywordCount().setText(count);
    }

    private void initCustomIntegrationFilters(String integrationTitle,
            @DrawableRes int integrationDrawableResId, int counter) {
        FilterKeywordsView filterKeywordsView = new FilterKeywordsView(
                getAppContext());

        final SwitchCompat filterKeywordsSwitch = filterKeywordsView
                .getSwitchCompat();
        filterKeywordsView.setSwitchListener(v -> {
            if (filterKeywordsSwitch.isChecked()) {
                // TODO: Enable filter 
            } else {
                // TODO: Disable filter
            }
        });

        final AppCompatTextView title = filterKeywordsView.getTitle();
        title.setText(integrationTitle);
        final Drawable customWebServiceDrawable = ContextCompat.getDrawable(
                getContext(), integrationDrawableResId);
        title.setCompoundDrawablesWithIntrinsicBounds(customWebServiceDrawable, null, null, null);
        AppCompatTextView filterKeywordCount = filterKeywordsView
                .getFilterKeywordCount();
        filterKeywordCount.setText(counter);
        filterKeywordCount.setOnClickListener(v -> {
            mLauncher.launchAddKeyword();
        });

        AppCompatTextView filterKeyword = filterKeywordsView.getFilterKeyword();
        filterKeyword.setText(R.string.keywords);
        filterKeyword.setOnClickListener(v -> {
            mLauncher.launchAddKeyword();
        });
        mFilterViewGroup.addView(filterKeywordsView);
    }
}
