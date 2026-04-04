package com.hospital.interfaces;

public interface Notifiable {

    String getNotificationEmail();

    String getNotificationName();

    boolean isNotificationsEnabled();
}