package com.xiandu.com.xiandu.service;

import com.xiandu.model.JsonRootBean;
import com.xiandu.model.Order;
import com.xiandu.model.ReportItem;
import com.xiandu.model.Trade;
import org.jxls.util.JxlsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 8/7/2020.
 */
public class ReportService {

    public void execute(JsonRootBean jsonRootBean) {

        List<ReportItem> reportItemList = parseToReportItemList(jsonRootBean);


        JxlsHelper.getInstance().processTemplate(is, os, context);






    }

    protected List<ReportItem> parseToReportItemList (JsonRootBean jsonRootBean) {
        List<Trade> tradeList = jsonRootBean.getBody().getTradeListResponse().getTrades().getTrade();

        List<ReportItem> reportItemList = new ArrayList<>();

        tradeList.forEach( trade -> {

            List<Order> orderList = trade.getOrders().getOrder();

            orderList.forEach( order -> {
                ReportItem reportItem = new ReportItem();

                reportItem.setReceiverName(trade.getReceiver_name());
                reportItem.setReceiverState(trade.getReceiver_state());
                reportItem.setReceiverCity(trade.getReceiver_city());
                reportItem.setReceiverDistrict(trade.getReceiver_district());

                reportItem.setProductName(order.getTitle());
                reportItem.setProductQty(order.getNum());

                reportItemList.add(reportItem);
            });


        });

        return reportItemList;

    }
}
