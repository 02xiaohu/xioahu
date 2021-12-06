package com.mybatis;

import java.io.Serializable;

public class Article implements Serializable {
	private int id;
    private int userid;
    private String title;
    private String content;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String toString()
	{return "id="+id+" "+"userid="+userid+" "+"title="+title+" "+"content="+content;}
	

}
