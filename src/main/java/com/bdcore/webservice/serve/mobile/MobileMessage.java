package com.bdcore.webservice.serve.mobile;

import java.util.List;

public class MobileMessage {

	private List<String> mobileNumber;
	
	private String msg;
	
	private String sign;
	
	private String projNo;
	
	private String channelNo;
	
	private String linkId;
	
	private String subCode;


	public String getSubCode() {
		return subCode;
	}

	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getProjNo() {
		return projNo;
	}

	public void setProjNo(String projNo) {
		this.projNo = projNo;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public List<String> getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(List<String> mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(String mobi:this.getMobileNumber()){
			sb.append(mobi+",");
		}
		
		
		
		return "projNo:"+this.projNo+
				",channelNo:"+this.channelNo+
				",subCode:" + this.subCode +
				",mobileNumber:"+sb.toString()+
				",msg:"+this.msg+
				",linkId:"+this.linkId+
				",sign:"+this.sign;
	}
}
