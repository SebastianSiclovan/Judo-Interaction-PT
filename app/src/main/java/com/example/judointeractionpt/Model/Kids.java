package com.example.judointeractionpt.Model;

import java.util.HashMap;

public class Kids {

    String name;
    String belt_type;
    String current_week;
    String number_presentWeek;
    String number_sessions;
    String qualification;
    String code;

    public Kids()
    {

    }

    public Kids(String name, String belt_type, String current_week, String number_presentWeek, String number_sessions, String qualification, String code)
    {
        this.name = name;
        this.belt_type = belt_type;
        this.current_week = current_week;
        this.number_sessions = number_sessions;
        this.number_presentWeek = number_presentWeek;
        this.qualification = qualification;
        this.code = code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
    public String getCode()
    {
        return code;
    }


    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return  name;
    }

    public void setBelt_type(String belt_type)
    {
        this.belt_type = belt_type;
    }
    public String getBelt_type()
    {
        return  belt_type;
    }

    public void setCurrent_week(String current_week)
    {
        this.current_week = current_week;
    }
    public String getCurrent_week()
    {
        return  current_week;
    }

    public void setNumber_sessions(String number_sessions)
    {
        this.number_sessions = number_sessions;
    }
    public String getNumber_sessions()
    {
        return  number_sessions;
    }

    public void setNumber_presentWeek(String number_presentWeek)
    {
        this.number_presentWeek = number_presentWeek;
    }
    public String getNumber_presentWeek()
    {
        return  number_presentWeek;
    }

    public void setQualification(String qualification)
    {
        this.qualification = qualification;
    }
    public String getQualification()
    {
        return  qualification;
    }






}

