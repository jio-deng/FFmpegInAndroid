package com.dzm.ffmpeg.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description car
 * @date 2020/4/3 11:54
 */
public class Car {
    public String name;
    public String country;
    public String image;
    public int level;

    @SerializedName("car")
    public List<Car> cars;
}
