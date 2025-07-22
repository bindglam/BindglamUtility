package com.bindglam.utility.gui.component;

public abstract class OffsetComponent extends UIComponent {
    private int offset = 0;

    public void setOffset(int value) {
        this.offset = value;
    }

    public int getOffset() {
        return offset;
    }
}
