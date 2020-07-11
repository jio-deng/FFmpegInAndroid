package com.zhihu.matisse.listener;

public abstract class TrackStatManager {

    public interface PageName {
        // 媒体文件页
        String MEDIA_PAGE = "media_page";
    }


    public interface EventName {
        // 点击拍照
        String MEDIA_SHOOTING_CLICK = "media_shooting_click";
        // 点击视频
        String MEDIA_VIDEO_CLICK = "media_video_click";
    }

    public abstract void trackActivityPageStart(String pageName);

    public abstract void trackActivityPageEnd(String pageName);

    public abstract void trackEvent(String eventName);
}
