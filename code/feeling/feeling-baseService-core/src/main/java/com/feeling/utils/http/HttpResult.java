package com.feeling.utils.http;

import java.util.Arrays;

import org.apache.http.Header;




/**
 * @author dutao
 *
 */
public class HttpResult {

	private int status;
	private String body;
	// 有时候请求获得的内容是二进制，比如文件、图片，如果通过string转化会出问题
	private byte[] bodyByte;
	private Header[] headers;
	private long exeUseTime;//执行消耗时间
	private String url;
	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getExeUseTime() {
        return exeUseTime;
    }

    public void setExeUseTime(long exeUseTime) {
        this.exeUseTime = exeUseTime;
    }

    public byte[] getBodyByte() {
		return bodyByte;
	}

	public void setBodyByte(byte[] bodyByte) {
		this.bodyByte = bodyByte;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	@Override
	public String toString() {
	    StringBuilder sbr = new StringBuilder();
	    sbr.append("HttpResult [status= ").append(status)
	        .append(", body=").append(body).append(", headers=")
	        .append(Arrays.toString(headers)).append(",useTime=")
	        .append(exeUseTime).append("ms,url=").append(url).append("]");
	    
		return sbr.toString();
	}

}
