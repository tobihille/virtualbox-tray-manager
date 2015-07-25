package virtualboxtraymanager;

import java.util.Date;
import java.util.GregorianCalendar;

public class SimpleVmCron {

  private int minute, hour, day, month, year;
  private String action, identifier;

  public SimpleVmCron(int minute, int hour, int day, int month, int year, String action, String identifier) {
    this.minute = minute;
    this.hour = hour;
    this.day = day;
    this.month = month;
    this.year = year;
    this.action = action;
    this.identifier = identifier;
  }

  public String getAction() {
    return action;
  }

  public int getDay() {
    return day;
  }

  public int getHour() {
    return hour;
  }

  public String getIdentifier() {
    return identifier;
  }

  public int getMinute() {
    return minute;
  }

  public int getMonth() {
    return month;
  }

  public int getYear() {
    return year;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public void setHour(int hour) {
    this.hour = hour;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public void setMinute(int minute) {
    this.minute = minute;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public void setYear(int year) {
    this.year = year;
  }
  
  public void calculateCron()
  {
    Date now = new Date();
    
    
  }
  
}
