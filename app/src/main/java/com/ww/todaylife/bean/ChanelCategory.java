package com.ww.todaylife.bean;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class ChanelCategory implements Serializable {
    public String name;
    public String typeCode;
    public boolean isSelected = false;
    public boolean isTitle;

    @Override
    public boolean equals(@Nullable Object obj) {
        ChanelCategory chanelCategory = (ChanelCategory) obj;
        return this.name.equals(chanelCategory.name);
    }

    @Override
    public int hashCode() {
        return (this.name + this.typeCode).hashCode();
    }
}
