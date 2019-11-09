package com.ssarl.sbtformanager.Object;

public class AverageGraphitem {
    private String title;
    private int seekBar;

    public AverageGraphitem(String title, int seekBar) {
        this.title = title;
        this.seekBar = seekBar;
    }

    public int getSeekBar() {
        return seekBar;
    }

    public String getTitle() {
        return title;
    }

    public void setSeekBar(int seekBar) {
        this.seekBar = seekBar;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
