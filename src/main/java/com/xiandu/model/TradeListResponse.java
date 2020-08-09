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
public class TradeListResponse {

    private int locked_trade_count;
    private boolean is_end_search_normal_tag;
    private Trades trades;
    private boolean has_next;
    private int total_results;
    public void setLocked_trade_count(int locked_trade_count) {
         this.locked_trade_count = locked_trade_count;
     }
     public int getLocked_trade_count() {
         return locked_trade_count;
     }

    public void setIs_end_search_normal_tag(boolean is_end_search_normal_tag) {
         this.is_end_search_normal_tag = is_end_search_normal_tag;
     }
     public boolean getIs_end_search_normal_tag() {
         return is_end_search_normal_tag;
     }

    public void setTrades(Trades trades) {
         this.trades = trades;
     }
     public Trades getTrades() {
         return trades;
     }

    public void setHas_next(boolean has_next) {
         this.has_next = has_next;
     }
     public boolean getHas_next() {
         return has_next;
     }

    public void setTotal_results(int total_results) {
         this.total_results = total_results;
     }
     public int getTotal_results() {
         return total_results;
     }

}