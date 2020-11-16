package com.xiandu.model;


/**
 * Created by Administrator on 8/8/2020.
 */
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public double getOriginalQty() {
        return originalQty;
    }

    public void setOriginalQty(double originalQty) {
        this.originalQty = originalQty;
    }

    public String getLogistic() {
        return logistic;
    }

    public void setLogistic(String logistic) {
        this.logistic = logistic;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public int getSkuRatio() {
        return skuRatio;
    }

    public void setSkuRatio(int skuRatio) {
        this.skuRatio = skuRatio;
    }

    public String getSkuShortName() {
        return skuShortName;
    }

    public void setSkuShortName(String skuShortName) {
        this.skuShortName = skuShortName;
    }

    public String getProductShortName() {
        return productShortName;
    }

    public void setProductShortName(String productShortName) {
        this.productShortName = productShortName;
    }

    public double getConvQty() {
        return convQty;
    }

    public void setConvQty(double convQty) {
        this.convQty = convQty;
    }

    public String getSellerMemo() {
        return sellerMemo;
    }

    public void setSellerMemo(String sellerMemo) {
        this.sellerMemo = sellerMemo;
    }

    public ReportItem() {
    }

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
