package com.wf.qingniao.bean;

public class UserInfo {
	private String userid;//用户id
	private String phoneUser;//手机�?
	private String phonePasswd;//手机用户密码
	private String usernum;//手机号组名（同一个操作员下面有多个手机号用户�?
	private int currnum;//能下载的数量
	private int storynum;//上一次下载的数量
	private int totalnum;//总共下载的数�?
	public String getPhoneUser() {
		return phoneUser;
	}
	public void setPhoneUser(String phoneUser) {
		this.phoneUser = phoneUser;
	}
	public String getPhonePasswd() {
		return phonePasswd;
	}
	public void setPhonePasswd(String phonePasswd) {
		this.phonePasswd = phonePasswd;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsernum() {
		return usernum;
	}
	public void setUsernum(String usernum) {
		this.usernum = usernum;
	}
	public int getCurrnum() {
		return currnum;
	}
	public void setCurrnum(int currnum) {
		this.currnum = currnum;
	}
	public int getStorynum() {
		return storynum;
	}
	public void setStorynum(int storynum) {
		this.storynum = storynum;
	}
	public int getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}
}
