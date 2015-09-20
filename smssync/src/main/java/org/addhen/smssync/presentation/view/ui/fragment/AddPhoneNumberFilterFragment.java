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

import android.os.Bundle;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class AddPhoneNumberFilterFragment extends BaseFragment {

    private static AddPhoneNumberFilterFragment mAddPhoneNumberFilterFragment;

    /**
     * BaseFragment
     */
    public AddPhoneNumberFilterFragment() {
        super(R.layout.filter_phone_numbers, 0);
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
}
