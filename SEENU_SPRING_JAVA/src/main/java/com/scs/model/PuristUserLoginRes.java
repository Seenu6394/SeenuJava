package com.scs.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PuristUserLoginRes {

	  private String status;

	    private String name;

	    private String token_timeout;

	    private String token_type;

	    private String url;

	    private String access_token;
	    
	    private TokenInfo info;

	    public List<Rooms> getRooms() {
			return rooms;
		}

		public void setRooms(List<Rooms> rooms) {
			this.rooms = rooms;
		}

		private List<Rooms> rooms ;
		
	    public TokenInfo getInfo() {
			return info;
		}

		public void setInfo(TokenInfo info) {
			this.info = info;
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

	    public String getToken_timeout ()
	    {
	        return token_timeout;
	    }

	    public void setToken_timeout (String token_timeout)
	    {
	        this.token_timeout = token_timeout;
	    }

	    public String getToken_type ()
	    {
	        return token_type;
	    }

	    public void setToken_type (String token_type)
	    {
	        this.token_type = token_type;
	    }

	    public String getUrl ()
	    {
	        return url;
	    }

	    public void setUrl (String url)
	    {
	        this.url = url;
	    }

	    public String getAccess_token ()
	    {
	        return access_token;
	    }

	    public void setAccess_token (String access_token)
	    {
	        this.access_token = access_token;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [status = "+status+", name = "+name+", token_timeout = "+token_timeout+", token_type = "+token_type+", url = "+url+", access_token = "+access_token+", rooms = "+rooms+"]";
	    }
	}
