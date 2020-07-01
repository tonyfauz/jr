package com.ls.jr.common;

import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(Utils.NAME)
public class Utils {
    public static final String NAME = "jr_Utils";


/*public Notifications.NotificationBuilder createNotification(Notifications.NotificationType notificationType, String notificationCaption, String notificationDescription, Integer notificationHideDelayMs) {
        Notifications notifications = AppBeans.get(Notifications.class);

        return notifications.create()
                .withType(notificationType)
                .withCaption(notificationCaption)
                .withDescription(notificationDescription)
                .withHideDelayMs(notificationHideDelayMs);
    }*/
}