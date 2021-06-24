/**
  * Copyright 2020 bejson.com 
  */
package com.xiandu.model;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2020-08-07 22:23:38
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Trade {

    private String adjust_fee;
    private String received_payment;
    private String type;
    private String step_paid_fee;
//    private boolean has_buyer_message;
    private String payment;
    private String receiver_country;
    private String buyer_message;
    private Orders orders;
    private Promotion_details promotion_details;
    private String status;
    private String post_fee;
    private Date timeout_action_time;
    private String shipping_type;
    private boolean buyer_rate;
    private boolean has_print_elecface;
    private String receiver_state;
    private boolean has_print_delivery;
    private String receiver_name;
    private String trade_from;
    private String receiver_district;
    private String receiver_town;
    private boolean has_yfx;
    private String receiver_mobile;
    private int seller_flag;
    private String receiver_address;
    private String seller_nick;
    private int num;
    private String tid;
    private String total_fee;
    private Date modified;
    private boolean has_print_logistic;
    private String seller_memo;
    private Date created;
    private String tid_str;
    private Date pay_time;
    private boolean seller_rate;
    private String receiver_city;
    private String buyer_alipay_no;
    private long num_iid;
    private String title;
    private List<Long> hidCids;
    private String seller_flag_string;
    private String receiver_zip;
    private String alipay_no;
    private String cod_status;
    private String credit_card_fee;
    private String pic_path;
    private String buyer_nick;
    private String receiver_phone;
    private List<Trade> trades;
    private String mergeTid;

}