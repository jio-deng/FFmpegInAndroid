package com.dzm.ffmpeg.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description test for Room
 * @date 2020/3/15 10:09
 */
@Entity
public class UserDao {
//    String tableName() {
//        return "Rename-Dao";
//    }

    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;
}
