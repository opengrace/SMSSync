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

package org.addhen.smssync.presentation.service;

import org.addhen.smssync.data.cache.FileManager;
import org.addhen.smssync.data.message.ProcessMessageResult;

import android.content.Intent;

import javax.inject.Inject;

/**
 * @author Henry Addo
 */
public class MessageResultsService extends BaseWakefulIntentService {

    private static final String CLASS_TAG = MessageResultsService.class.getSimpleName();

    @Inject
    ProcessMessageResult mProcessMessageResult;

    @Inject
    FileManager mFileManager;

    public MessageResultsService() {
        super(CLASS_TAG);
    }

    public void onCreate() {
        super.onCreate();
        getComponent().inject(this);
    }

    @Override
    public void executeTask(Intent intent) {
        log("checking scheduled message result services");
        // TODO: Move the hard coded string to a string resource
        mFileManager.appendAndClose("Checking scheduled message result services");
        mProcessMessageResult.processMessageResult();
    }
}
