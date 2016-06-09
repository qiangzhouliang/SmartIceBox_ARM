package com.friendlyarm.user;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Q on 2016-04-11.
 */
public class _User extends BmobUser {
    private BmobFile icon;
    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }
}
