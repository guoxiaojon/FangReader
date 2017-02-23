package com.example.jon.fangreader.model.bean;

/**
 * Created by jon on 2017/1/4.
 */

public class DownloadEvent {
    private boolean isFinished;
    private int currentChapter;
    private int totalChapter;
    private String title;

    public DownloadEvent(boolean isFinished, int currentChapter, int totalChapter, String title) {
        this.isFinished = isFinished;
        this.currentChapter = currentChapter;
        this.totalChapter = totalChapter;
        this.title = title;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public int getCurrentChapter() {
        return currentChapter;
    }

    public void setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
    }

    public int getTotalChapter() {
        return totalChapter;
    }

    public void setTotalChapter(int totalChapter) {
        this.totalChapter = totalChapter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
