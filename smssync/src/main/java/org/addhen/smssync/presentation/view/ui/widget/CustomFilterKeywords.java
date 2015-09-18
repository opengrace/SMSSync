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

package org.addhen.smssync.presentation.view.ui.widget;

import org.addhen.smssync.R;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class CustomFilterKeywords extends LinearLayout {

    private TextView mTitleTextView;

    private SwitchCompat mSwitchCompat;

    private FilterKeywords mFilterKeywords;

    public CustomFilterKeywords(Context context) {
        super(context);
        initView();
    }

    public CustomFilterKeywords(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.custom_web_integration_filters, this);
        mTitleTextView = (TextView) rootView.findViewById(R.id.custom_web_service_filter);
        mSwitchCompat = (SwitchCompat) rootView
                .findViewById(R.id.custom_filter_keyword_custom_switch);
        mFilterKeywords = (FilterKeywords) rootView.findViewById(R.id.filter_keywords_custom);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public TextView getTitle() {
        return mTitleTextView;
    }

    public SwitchCompat getSwitchCompat() {
        return mSwitchCompat;
    }

    public FilterKeywords getFilterKeywords() {
        return mFilterKeywords;
    }
}
