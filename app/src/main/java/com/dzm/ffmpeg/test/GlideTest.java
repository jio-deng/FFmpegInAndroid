package com.dzm.ffmpeg.test;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description Glide Test
 * @date 2020/5/10 12:03
 */
public class GlideTest {
    private String url = "http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=tupian&step_word=&hs=0&pn=18&spn=0&di=94160&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=2&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=1141259048%2C554497535&os=933218164%2C348838775&simid=3512635485%2C227490761&adpicid=0&lpn=0&ln=1728&fr=&fmq=1589083537834_R&fm=&ic=undefined&s=undefined&hd=undefined&latest=undefined&copyright=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fa1.att.hudong.com%2F05%2F00%2F01300000194285122188000535877.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fp7rtwg_z%26e3Bkwthj_z%26e3Bv54AzdH3Ftrw1AzdH3Fw8_ac_aa_a8naaaaa8l9dbc8dd8bbaaacncb00_3r2_z%26e3Bip4s&gsm=13&rpstart=0&rpnum=0&islist=&querylist=&force=undefined";

    public void getPic(ImageView view) {
        GlideApp.with(view.getContext())
                .load(url)
                .centerCrop()
                .into(view);

        Glide.with(view.getContext())
                .load(url)
                .into(view);
    }
}
