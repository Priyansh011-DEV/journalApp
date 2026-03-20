package net.PORC.journalApp.entity;



import java.util.List;

public class MoodContainer {
    private List<String> keywords;
    private List<String> advice;


    public List<String> getKeywords() { return keywords; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }
    public List<String> getAdvice() { return advice; }
    public void setAdvice(List<String> advice) { this.advice = advice; }
}