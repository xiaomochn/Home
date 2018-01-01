package com.xiaomo.funny.home.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xiaomo.funny.home.manager.SQLHelper;

import zuo.biao.library.util.Log;

/**
 * Created by xiaomochn on 22/12/2017.
 */

public class UserDao implements DBDao{
    public static final String TABLE_NAME = "xiaomo_user";
    public static final String COLUMN_ID = "_id";//long类型的id不能自增，primary key autoincrement会出错
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_USERID = "userId";
    public static final String COLUMN_LEVAL = "userleval";// 用户等级 1级最高  越大越低

    private SQLHelper mHelper;

    public UserDao(Context context) {
        this.mHelper = new SQLHelper(context, this);
    }


    public void  addUser(UserBean userBean){

    }

    @Override
    public String getCreatStr() {
        String sql = "CREATE TABLE " + TABLE_NAME+ " (" + COLUMN_ID + " INTEGER primary key autoincrement, "
                + COLUMN_NAME + " text, " + COLUMN_PHONE + " text, " + COLUMN_USERID + " text, " + COLUMN_LEVAL + " INTEGER)";
        return sql;
    }


    /**插入数据
     * @param values
     * @return
     */
    public long insert(UserBean     user ) {
        ContentValues values =new ContentValues();
//        values.put(COLUMN_NAME,user.get());
        values.put(COLUMN_USERID,user.getUserId());
        values.put(COLUMN_LEVAL,user.getUserLeval());

        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            return db.insert(TABLE_NAME, null, mHelper.getCorrectValues(values));
        } catch (Exception e) {
           e.printStackTrace();
        }
        return -1;
    }
    @Override
    public String getTabletName() {
        return TABLE_NAME;
    }

    public static class UserBean {
        String userId;
        String phone;
        String name;
        int userLeval;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUserLeval() {
            return userLeval;
        }

        public void setUserLeval(int userLeval) {
            this.userLeval = userLeval;
        }
    }


}
