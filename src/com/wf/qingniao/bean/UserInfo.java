package com.wf.qingniao.bean;

public class UserInfo {
	private String userid;//ç¨æ·id
	private String phoneUser;//ææºå?
	private String phonePasswd;//ææºç¨æ·å¯ç 
	private String usernum;//ææºå·ç»åï¼åä¸ä¸ªæä½åä¸é¢æå¤ä¸ªææºå·ç¨æ·ï¼?
	private int currnum;//è½ä¸è½½çæ°é
	private int storynum;//ä¸ä¸æ¬¡ä¸è½½çæ°é
	private int totalnum;//æ»å±ä¸è½½çæ°é?
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
