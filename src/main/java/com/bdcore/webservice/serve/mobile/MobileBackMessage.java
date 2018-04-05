package com.bdcore.webservice.serve.mobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import ch.ethz.ssh2.crypto.digest.Digest;
import ch.ethz.ssh2.crypto.digest.MD5;

public class MobileBackMessage {

	
	private int count = 0;
	
	List<Msg> msgs = new ArrayList<Msg>();

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Msg> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<Msg> msgs) {
		this.msgs = msgs;
	}
	
	
	public static class Msg{
		
		String backTime;
		
		String mobileNum;
		String content;
		
		public Msg(String a1,String a2,String a3){
			this.mobileNum = a1;
			this.content = a2;
			this.backTime = a3;
		}
		
		public String getBackTime() {
			return backTime;
		}

		public void setBackTime(String backTime) {
			this.backTime = backTime;
		}

		public String getMobileNum() {
			return mobileNum;
		}
		public void setMobileNum(String mobileNum) {
			this.mobileNum = mobileNum;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		
		public String getMd5(){
			return DigestUtils.md5Hex(this.mobileNum+this.content+this.backTime);
		}
		
		public String toString(){
			return getMd5()+"|"+backTime+"|"+mobileNum+"|"+content;
		}
		
	}
}


