package com.tanlong.exercise.model.entity;

/**
 * 菜单选项
 * Created by 龙 on 2016/6/28.
 */
public class IMenuItem {

    private String name;
    private int menuType;// 滑动菜单类型, 有0和1两种状态

    public IMenuItem(String name, int menuType) {
        this.name = name;
        this.menuType = menuType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMenuType() {
        return menuType;
    }

    public void setMenuType(int menuType) {
        this.menuType = menuType;
    }
}
