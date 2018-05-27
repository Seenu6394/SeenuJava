package com.scs.model;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterPuristUser {

	 private String username;

	    private String email;

	    private String name;

	    private String password;

	    public String getUsername ()
	    {
	        return username;
	    }

	    public void setUsername (String username)
	    {
	        this.username = username;
	    }

	    public String getEmail ()
	    {
	        return email;
	    }

	    public void setEmail (String email)
	    {
	        this.email = email;
	    }

	    public String getName ()
	    {
	        return name;
	    }

	    public void setName (String name)
	    {
	        this.name = name;
	    }

	    public String getPassword ()
	    {
	        return password;
	    }

	    public void setPassword (String password)
	    {
	        this.password = password;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [username = "+username+", email = "+email+", name = "+name+", password = "+password+"]";
	    }
	}
				
				