package com.scs.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rooms
{
    private String id;

    private String status;

    private String name;

    private String agent;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getAgent ()
    {
        return agent;
    }

    public void setAgent (String agent)
    {
        this.agent = agent;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", status = "+status+", name = "+name+", agent = "+agent+"]";
    }
}