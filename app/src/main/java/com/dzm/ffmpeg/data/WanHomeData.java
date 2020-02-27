package com.dzm.ffmpeg.data;

import java.util.List;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description wan android home page data
 * @date 2020/2/27 16:03
 */
public class WanHomeData {
    public int curPage;
    public int offset;
    public boolean over;
    public int pageCount;
    public int size;
    public int total;
    public List<DatasBean> datas;

    public static class DatasBean {
        /**
         * apkLink :
         * audit : 1
         * author : code小生
         * canEdit : false
         * chapterId : 414
         * chapterName : code小生
         * collect : false
         * courseId : 13
         * desc :
         * descMd :
         * envelopePic :
         * fresh : false
         * id : 12087
         * link : https://mp.weixin.qq.com/s/NVLEw1DdRFvoFRxN2LhZ7Q
         * niceDate : 2020-02-24 00:00
         * niceShareDate : 20小时前
         * origin :
         * prefix :
         * projectLink :
         * publishTime : 1582473600000
         * selfVisible : 0
         * shareDate : 1582718116000
         * shareUser :
         * superChapterId : 408
         * superChapterName : 公众号
         * tags : [{"name":"公众号","url":"/wxarticle/list/414/1"}]
         * title : Android 从 MVP 到 MVVM
         * type : 0
         * userId : -1
         * visible : 1
         * zan : 0
         */

        public String apkLink;
        public int audit;
        public String author;
        public boolean canEdit;
        public int chapterId;
        public String chapterName;
        public boolean collect;
        public int courseId;
        public String desc;
        public String descMd;
        public String envelopePic;
        public boolean fresh;
        public int id;
        public String link;
        public String niceDate;
        public String niceShareDate;
        public String origin;
        public String prefix;
        public String projectLink;
        public long publishTime;
        public int selfVisible;
        public long shareDate;
        public String shareUser;
        public int superChapterId;
        public String superChapterName;
        public String title;
        public int type;
        public int userId;
        public int visible;
        public int zan;
        public List<WanTag> tags;
    }
}
