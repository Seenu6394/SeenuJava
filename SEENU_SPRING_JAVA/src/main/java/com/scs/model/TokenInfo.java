package com.scs.model;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenInfo {


	    private String username;

	    private String ticketing_enabled;

	    private String email;

	    private String chat_enabled;

	    private String name;

	    private String type;

	    public String getUsername ()
	    {
	        return username;
	    }

	    public void setUsername (String username)
	    {
	        this.username = username;
	    }

	    public String getTicketing_enabled ()
	    {
	        return ticketing_enabled;
	    }

	    public void setTicketing_enabled (String ticketing_enabled)
	    {
	        this.ticketing_enabled = ticketing_enabled;
	    }

	    public String getEmail ()
	    {
	        return email;
	    }

	    public void setEmail (String email)
	    {
	        this.email = email;
	    }

	    public String getChat_enabled ()
	    {
	        return chat_enabled;
	    }

	    public void setChat_enabled (String chat_enabled)
	    {
	        this.chat_enabled = chat_enabled;
	    }

	    public String getName ()
	    {
	        return name;
	    }

	    public void setName (String name)
	    {
	        this.name = name;
	    }

	    public String getType ()
	    {
	        return type;
	    }

	    public void setType (String type)
	    {
	        this.type = type;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [username = "+username+", ticketing_enabled = "+ticketing_enabled+", email = "+email+", chat_enabled = "+chat_enabled+", name = "+name+", type = "+type+"]";
	    }
	}