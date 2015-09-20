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

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddPhoneNumberFilterFragment extends BaseFragment {

    private static AddPhoneNumberFilterFragment mAddPhoneNumberFilterFragment;

    /**
     * BaseFragment
     */
    public AddPhoneNumberFilterFragment() {
        super(R.layout.fragment_add_phone_numbers, 0);
    }

    public static AddPhoneNumberFilterFragment newInstance() {
        if (mAddPhoneNumberFilterFragment == null) {
            mAddPhoneNumberFilterFragment = new AddPhoneNumberFilterFragment();
        }
        return mAddPhoneNumberFilterFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick(R.id.add_white_list_numbers_btn)
    void onWhiteListClicked() {
        showDialog(R.string.whitelist, true);
    }

    @OnClick(R.id.add_black_list_numbers_btn)
    void onBlackListClicked() {
        showDialog(R.string.blacklist, false);
    }

    private void showDialog(@StringRes int titleResId, boolean isWhiteList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_keyword, null);
        AppCompatTextView title = ButterKnife.findById(view, R.id.add_filter_title);
        title.setText(titleResId);
        EditText keywordEditText = ButterKnife.findById(view, R.id.add_keyword_text);
        keywordEditText.setHint(R.string.add_phone_number);
        builder.setView(view).setPositiveButton(R.string.add,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        showSnabackar(getView(), keywordEditText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();

    }
}
