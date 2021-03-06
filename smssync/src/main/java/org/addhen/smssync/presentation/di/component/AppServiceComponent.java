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

package org.addhen.smssync.presentation.di.component;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */

import com.addhen.android.raiburari.presentation.di.qualifier.ActivityScope;

import org.addhen.smssync.presentation.di.module.ServiceModule;
import org.addhen.smssync.presentation.presenter.message.UpdateMessagePresenter;
import org.addhen.smssync.presentation.service.BaseWakefulIntentService;
import org.addhen.smssync.presentation.service.SmsReceiverService;

import dagger.Component;

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {
        ServiceModule.class})
public interface AppServiceComponent {

    void inject(SmsReceiverService smsReceiverService);

    void inject(BaseWakefulIntentService baseWakefulIntentService);

    UpdateMessagePresenter updateMessagePresenter();
}
