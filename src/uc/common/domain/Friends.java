package uc.common.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Description: 好友分组domain
 * @author wutp 2016年11月6日
 * @version 1.0
 */
public class Friends extends UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fgid;//所属分组
	private Timestamp fdate;//添加日期
	private String remarks;//备注
	
	public Friends() {
		super();
	}
	
	public String getFgid() {
		return fgid;
	}
	public void setFgid(String fgid) {
		this.fgid = fgid;
	}
	public Timestamp getFdate() {
		return fdate;
	}
	public void setFdate(Timestamp fdate) {
		this.fdate = fdate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
