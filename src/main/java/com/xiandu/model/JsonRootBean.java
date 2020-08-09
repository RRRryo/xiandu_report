/**
  * Copyright 2020 bejson.com 
  */
package com.xiandu.model;

/**
 * Auto-generated: 2020-08-07 22:23:38
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private Body body;
    private int code;
    private String message;
    private String requestId;
    public void setBody(Body body) {
         this.body = body;
     }
     public Body getBody() {
         return body;
     }

    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setMessage(String message) {
         this.message = message;
     }
     public String getMessage() {
         return message;
     }

    public void setRequestId(String requestId) {
         this.requestId = requestId;
     }
     public String getRequestId() {
         return requestId;
     }

}