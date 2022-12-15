# ATTO - A Simple Launcher     &ensp;    [![Generic badge](https://img.shields.io/badge/version-%201.0.7-yellowgreen?style=for-the-badge&logo=android)]()
Atto is a custom launcher app for users who wants to organize apps 
easily and manages phone usage time 

[<img width="200" src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png"/>](https://play.google.com/store/apps/details?id=com.chihwhsu.atto)


## Add a Label for Apps

  * Classify apps into groups with label automatically
  * Several wallpapers for choose

<img width="24%" src="screenshot/setting1.png"/> <img width="24%" src="screenshot/setting2.png"/> <img width="24%" src="screenshot/setting3.png"/> <img width="24%" src="screenshot/setting4.png"/>


&nbsp;
## Simple style Desktop

  * Quick navigate slide drawer 
  * Highlight app background with different color
  * Support Notification and widgets

<img width="24%" src="screenshot/desktop1.png"/> <img width="24%" src="screenshot/desktop2.png"/> <img width="24%" src="screenshot/desktop3.png"/> <img width="24%" src="screenshot/desktop5.png"/>


&nbsp;
## Control Usage Time
  * Know how long the app you use quikly
  * Limit usage time by yourself or Pomodoro mode

<img width="24%" src="screenshot/limit1.png"/> <img width="24%" src="screenshot/limit2.png"/> <img width="24%" src="screenshot/limit3.png"/> <img width="24%" src="screenshot/limit4.png"/>
 

&nbsp;
## Set Reminder
  * Support Alarm , Todo list and POMODORO

<img width="24%" src="screenshot/clock1.png"/> <img width="24%" src="screenshot/clock3.png"/> <img width="24%" src="screenshot/clock4.png"/> <img width="24%" src="screenshot/clock5.png"/>


&nbsp;
## Technical Highlights

* Structured code with **MVVM** pattern to reduce coupling and improve readability
* Obtained system data and stored in Room as local data and synced to **Firebase** when user login.
* Implemented **Repository Pattern** to handle different data sources and organize Model layer and 
  keep the functions easily to use
* Calculated apps usage time by **UsageStatsManager** and displayed on UI, based on this statistic 
  developed a usage limit feature and combined it with Pomodoro
* Used **WorkManager** to reset uage limit timer every midnight
* Used **AlarmManager** to remind user in correct time
* Implemented **NotificationListenerService** to receive the device notification messages 
* Used **WidgetManager** to support system widget
* Added **GestureDetector** to achieve more ways to UI control
* Designed special drawer by using **MotionLayout**
* Showed user statistic in visualize by williamchart
* Supported multiple time zone feature    

  

&nbsp;
## Version History

| Version |   Description   |
| :--: | :------------------------------|
| 1.0.7 | Fix drawer slide and viewpager conflict bug          |
| 1.0.5 | Release version          |
| 1.0.3 | Implemented alarm and usage features         |
| 1.0.2 | Basic tutorial and setting feature         |
| 1.0.1 | Initial version           |


&nbsp;
## Requirements

* Android SDK 26
* Gradle 7.2

&nbsp;
## Contact
<p>E-Mail : chihweih27@gmail.com 
<br>Linkedin : www.linkedin.com/in/chih-hsu</p>


