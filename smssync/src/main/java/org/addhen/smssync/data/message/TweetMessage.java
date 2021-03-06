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

package org.addhen.smssync.data.message;

import org.addhen.smssync.R;
import org.addhen.smssync.data.PrefsFactory;
import org.addhen.smssync.data.cache.FileManager;
import org.addhen.smssync.data.database.FilterDatabaseHelper;
import org.addhen.smssync.data.database.MessageDatabaseHelper;
import org.addhen.smssync.data.database.WebServiceDatabaseHelper;
import org.addhen.smssync.data.entity.Filter;
import org.addhen.smssync.data.entity.Message;
import org.addhen.smssync.data.entity.WebService;
import org.addhen.smssync.data.twitter.TwitterClient;
import org.addhen.smssync.data.util.Logger;
import org.addhen.smssync.data.util.Utility;
import org.addhen.smssync.smslib.sms.ProcessSms;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import twitter4j.Status;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
@Singleton
public class TweetMessage extends ProcessMessage {

    private static final String TAG = TweetMessage.class
            .getSimpleName();

    private TwitterClient mTwitterClient;

    @Inject
    public TweetMessage(Context context, PrefsFactory prefsFactory,
            TwitterClient twitterClient,
            MessageDatabaseHelper messageDatabaseHelper,
            WebServiceDatabaseHelper webServiceDatabaseHelper,
            FilterDatabaseHelper filterDatabaseHelper,
            ProcessSms processSms,
            FileManager fileManager
    ) {
        super(context, prefsFactory, messageDatabaseHelper, webServiceDatabaseHelper,
                filterDatabaseHelper, processSms, fileManager);
        mTwitterClient = twitterClient;
    }

    /**
     * Processes the incoming SMS to figure out how to exactly route the message. If it fails to be
     * synced online, cache it and queue it up for the scheduler to process it.
     *
     * @param message The sms to be routed
     * @return boolean
     */
    public boolean routeSms(Message message, List<String> keywords) {
        Logger.log(TAG, "routeSms uuid: " + message.toString());
        // Double check if SMSsync service is running
        if (!mPrefsFactory.serviceEnabled().get()) {
            return false;
        }

        // Send auto response from phone not server
        if (mPrefsFactory.enableReply().get()) {
            // send auto response as SMS to user's phone
            logActivities(R.string.auto_response_sent);
            Message msg = new Message();
            msg.messageBody = mPrefsFactory.reply().get();
            msg.messageFrom = message.messageFrom;
            msg.messageType = message.messageType;
            mProcessSms.sendSms(map(msg), false);
        }
        if (Utility.isConnected(mContext)) {
            List<Filter> filters = mFilterDatabaseHelper.getFilters();
            // Process if white-listing is enabled
            if (mPrefsFactory.enableWhitelist().get()) {
                for (Filter filter : filters) {
                    if (filter.phoneNumber.equals(message.messageFrom)) {
                        if (tweetMessage(message, keywords)) {
                            deleteFromSmsInbox(message);
                        } else {
                            savePendingMessage(message);
                        }
                    }
                }
            }

            // Process blacklist
            if (mPrefsFactory.enableBlacklist().get()) {
                for (Filter filter : filters) {

                    if (filter.phoneNumber.equals(message.messageFrom)) {
                        Logger.log("message",
                                " from:" + message.messageFrom + " filter:"
                                        + filter.phoneNumber);
                        return false;
                    } else {
                        if (tweetMessage(message, keywords)) {
                            deleteFromSmsInbox(message);
                        } else {
                            savePendingMessage(message);
                        }
                    }

                }
            } else {
                if (tweetMessage(message, keywords)) {
                    deleteFromSmsInbox(message);
                } else {
                    savePendingMessage(message);
                }
            }

            return true;
        }

        // There is no internet save message
        savePendingMessage(message);
        return false;
    }

    /**
     * Sync pending messages to the configured sync URL.
     *
     * @param uuid The message uuid
     */
    public boolean syncPendingMessages(final String uuid) {
        Logger.log(TAG, "syncPendingMessages: push pending messages to the Sync URL" + uuid);
        boolean status = false;
        // check if it should sync by id
        if (!TextUtils.isEmpty(uuid)) {
            final Message message = mMessageDatabaseHelper.fetchMessageByUuid(uuid);
            List<Message> messages = new ArrayList<Message>();
            messages.add(message);
            // TODO: Get the message keyword
            status = tweetMessage(messages, null);
        } else {
            final List<Message> messages = mMessageDatabaseHelper
                    .fetchMessage(Message.Type.PENDING);
            if (messages != null && messages.size() > 0) {
                for (Message message : messages) {
                    status = tweetMessage(messages, null);
                }
            }
        }

        return status;
    }

    public boolean tweetMessage(List<Message> messages, List<String> keywords) {
        Logger.log(TAG, "tweetMessages");
        List<WebService> webServiceList = mWebServiceDatabaseHelper.listWebServices();
        List<Filter> filters = mFilterDatabaseHelper.getFilters();
        for (WebService webService : webServiceList) {
            // Process if white-listing is enabled
            if (mPrefsFactory.enableWhitelist().get()) {
                for (Filter filter : filters) {
                    for (Message message : messages) {
                        if (filter.phoneNumber.equals(message.messageFrom)) {
                            if (tweetMessage(message, keywords)) {
                                postToSentBox(message);
                            }
                        }
                    }
                }
            }

            if (mPrefsFactory.enableBlacklist().get()) {
                for (Filter filter : filters) {
                    for (Message msg : messages) {
                        if (!filter.phoneNumber.equals(msg.messageFrom)) {
                            if (tweetMessage(msg, keywords)) {
                                postToSentBox(msg);
                            }
                        }
                    }
                }
            } else {
                for (Message messg : messages) {
                    if (tweetMessage(messg, keywords)) {
                        postToSentBox(messg);
                    }
                }
            }
        }
        return true;
    }

    private boolean tweetMessage(Message message, List<String> keywords) {
        // Process filter text (keyword or RegEx)
        if (!Utility.isEmpty(keywords)) {
            if (filterByKeywords(message.messageBody, keywords) || filterByRegex(
                    message.messageBody, keywords)) {
                return tweetMessage(message);
            }
        } else {
            return tweetMessage(message);
        }
        return false;
    }

    private boolean tweetMessage(Message message) {
        boolean posted = false;
        if (message.messageType == Message.Type.PENDING) {
            Logger.log(TAG, "Process message with keyword filtering enabled " + message);
            // Post to Twitter as well.
            Status status = mTwitterClient.tweet(message.messageBody);
            posted = status != null;

        } else {
            posted = sendTaskSms(message);
        }
        if (!posted) {
            processRetries(message);
        }
        return false;
    }

    public static class Builder {

        private Context mContext;

        private PrefsFactory mPrefsFactory;

        private TwitterClient mTwitterApp;

        private MessageDatabaseHelper mMessageDatabaseHelper;

        private WebServiceDatabaseHelper mWebServiceDatabaseHelper;

        private FilterDatabaseHelper mFilterDatabaseHelper;

        private ProcessSms mProcessSms;

        private FileManager mFileManager;

        public Builder setContext(Context context) {
            mContext = context;
            return this;
        }

        public Builder setPrefsFactory(PrefsFactory prefsFactory) {
            mPrefsFactory = prefsFactory;
            return this;
        }

        public Builder setTwitterApp(TwitterClient twitterApp) {
            mTwitterApp = twitterApp;
            return this;
        }

        public Builder setMessageDatabaseHelper(
                MessageDatabaseHelper messageDatabaseHelper) {
            mMessageDatabaseHelper = messageDatabaseHelper;
            return this;
        }

        public Builder setWebServiceDatabaseHelper(
                WebServiceDatabaseHelper webServiceDatabaseHelper) {
            mWebServiceDatabaseHelper = webServiceDatabaseHelper;
            return this;
        }

        public Builder setFilterDatabaseHelper(FilterDatabaseHelper filterDatabaseHelper) {
            mFilterDatabaseHelper = filterDatabaseHelper;
            return this;
        }

        public Builder setProcessSms(ProcessSms processSms) {
            mProcessSms = processSms;
            return this;
        }

        public Builder setFileManager(FileManager fileManager) {
            mFileManager = fileManager;
            return this;
        }

        public TweetMessage build() {
            return new TweetMessage(mContext, mPrefsFactory, mTwitterApp, mMessageDatabaseHelper,
                    mWebServiceDatabaseHelper, mFilterDatabaseHelper, mProcessSms, mFileManager);
        }
    }
}
