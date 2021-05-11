package com.xiandu.model;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Administrator on 8/8/2020.
 */
@Data
@NoArgsConstructor
public class ReportItem {

    private int orderId;
    private String receiverName;
    private String receiverState;
    private String receiverCity;
    private String receiverDistrict;
    private String productName;
    private String productShortName;
    private String skuId;
    private String skuName;
    private String skuShortName;
    private int skuRatio = 1;
    private double originalQty;
    private double convQty;
    private String logistic = "默认";
    private String sellerMemo;

    public ReportItem(ReportItem reportItem) {
        this.orderId = reportItem.orderId;
        this.receiverName = reportItem.receiverName;
        this.receiverState = reportItem.receiverState;
        this.receiverCity = reportItem.receiverCity;
        this.receiverDistrict = reportItem.receiverDistrict;
        this.productName = reportItem.productName;
        this.productShortName = reportItem.productShortName;
        this.skuId = reportItem.skuId;
        this.skuName = reportItem.skuName;
        this.skuShortName = reportItem.skuShortName;
        this.skuRatio = reportItem.skuRatio;
        this.originalQty = reportItem.originalQty;
        this.convQty = reportItem.convQty;
        this.logistic = reportItem.logistic;
        this.sellerMemo = reportItem.sellerMemo;
    }
}
