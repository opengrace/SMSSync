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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class FilterKeywords extends LinearLayout {

    private static final int DEFAULT_TEXT_SIZE = 16;

    private TextView mFilterKeyword;

    private TextView mFilterKeywordCount;

    private int mKeywordDrawablePadding = 24;

    private int mKeywordCounterDrawablePadding = 16;

    private int mKeywordTextColor = android.R.color.black;

    private int mKeywordCounterTextColor = android.R.color.black;

    private float mKeywordCounterTextSize;

    private float mKeywordTextSize;

    private Drawable mKeywordIcon;

    private CharSequence mKeywordText;


    public FilterKeywords(Context context) {
        super(context);
        initViews();
    }

    public FilterKeywords(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initViews();
    }

    private void initViews() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.filter_keywords, this);
        mFilterKeyword = (TextView) rootView.findViewById(R.id.filter_keyword);
        mFilterKeywordCount = (TextView) rootView.findViewById(R.id.filter_keyword_count);
        mFilterKeyword.setCompoundDrawablePadding(mKeywordDrawablePadding);
        mFilterKeyword.setTextSize(mKeywordTextSize);
        mFilterKeyword.setTextColor(mKeywordTextColor);
        mFilterKeywordCount.setCompoundDrawablePadding(mKeywordCounterDrawablePadding);
        mFilterKeywordCount.setTextSize(mKeywordCounterTextSize);
        mFilterKeywordCount.setTextColor(mKeywordCounterTextColor);
        mFilterKeyword.setText(mKeywordText);
        mFilterKeyword.setCompoundDrawablesWithIntrinsicBounds(mKeywordIcon, null, null, null);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray attributesArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.FilterKeywords);
        try {
            mKeywordDrawablePadding = (int) attributesArray
                    .getDimension(R.styleable.FilterKeywords_keywordDrawablePadding,
                            dipToPixels(mKeywordDrawablePadding));
            mKeywordCounterDrawablePadding = (int) attributesArray
                    .getDimension(R.styleable.FilterKeywords_keywordCounterDrawablePadding,
                            dipToPixels(mKeywordCounterDrawablePadding));
            mKeywordTextColor = attributesArray
                    .getColor(R.styleable.FilterKeywords_keywordTextColor,
                            getResources().getColor(mKeywordTextColor));
            mKeywordCounterTextColor = attributesArray
                    .getColor(R.styleable.FilterKeywords_keywordCounterTextColor,
                            getResources().getColor(mKeywordCounterTextColor));
            mKeywordTextSize = attributesArray
                    .getDimension(R.styleable.FilterKeywords_keywordTextSize, DEFAULT_TEXT_SIZE);

            mKeywordCounterTextSize = attributesArray
                    .getDimension(R.styleable.FilterKeywords_keywordTextSize, DEFAULT_TEXT_SIZE);
            mKeywordText = attributesArray.getText(R.styleable.FilterKeywords_keywordText);
            mKeywordIcon = attributesArray.getDrawable(R.styleable.FilterKeywords_keywordIcon);
        } finally {
            attributesArray.recycle();
        }

    }

    private int dipToPixels(float dipValue) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public TextView getFilterKeyword() {
        return mFilterKeyword;
    }

    public TextView getFilterKeywordCount() {
        return mFilterKeywordCount;
    }

    public CharSequence getText() {
        return mKeywordText;
    }
}
