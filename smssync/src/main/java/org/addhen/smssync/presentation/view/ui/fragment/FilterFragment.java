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

import com.addhen.android.raiburari.presentation.ui.fragment.BaseRecyclerViewFragment;

import org.addhen.smssync.R;
import org.addhen.smssync.presentation.di.component.FilterComponent;
import org.addhen.smssync.presentation.model.FilterModel;
import org.addhen.smssync.presentation.presenter.ListFilterPresenter;
import org.addhen.smssync.presentation.view.filters.ListFilterView;
import org.addhen.smssync.presentation.view.ui.activity.MainActivity;
import org.addhen.smssync.presentation.view.ui.adapter.FilterAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class FilterFragment extends BaseRecyclerViewFragment<FilterModel, FilterAdapter> implements
        ListFilterView {

    private static final String ARGUMENT_KEY_FILTER_STATUS
            = "org.addhen.smssync.presentation.view.ui.fragment.ARGUMENT_FILTER_STATUS";

    private static final String BUNDLE_STATE_FILTER_STATUS
            = "org.addhen.smssync.presentation.view.ui.fragment.BUNDLE_STATE_FILTER_STATUS";

    @Inject
    ListFilterPresenter mListFilterPresenter;


    FilterAdapter mFilterAdapter;

    @Bind(R.id.keyword_filters_container)
    ViewGroup mTwitterFilterViewGroup;

    public FilterFragment() {
        super(FilterAdapter.class, R.layout.fragment_filter_list, 0);
    }

    public static FilterFragment newInstance(FilterModel.Status status) {
        FilterFragment filterFragment = new FilterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGUMENT_KEY_FILTER_STATUS, status);
        filterFragment.setArguments(bundle);
        return filterFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize(savedInstanceState);
    }

    private void initialize(Bundle savedInstanceState) {
        getFilterComponent(FilterComponent.class).inject(this);
        mListFilterPresenter.setView(this);
        final FilterModel.Status status;
        if (savedInstanceState == null) {
            status = (FilterModel.Status) getArguments().getSerializable(
                    ARGUMENT_KEY_FILTER_STATUS);
        } else {
            status = (FilterModel.Status) savedInstanceState
                    .getSerializable(BUNDLE_STATE_FILTER_STATUS);
        }
        mListFilterPresenter.loadFilters(status);
    }

    @Override
    public void showFilters(List<FilterModel> filterModelList) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String s) {

    }

    @Override
    public Context getAppContext() {
        return null;
    }

    protected <C> C getFilterComponent(Class<C> componentType) {
        return componentType.cast(((MainActivity) getActivity()).getFilterComponent());
    }
}
