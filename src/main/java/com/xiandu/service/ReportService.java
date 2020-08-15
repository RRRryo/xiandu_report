package com.xiandu.service;

import com.xiandu.model.JsonRootBean;
import com.xiandu.model.Order;
import com.xiandu.model.ReportItem;
import com.xiandu.model.Trade;
import com.xiandu.utils.Constants;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 8/7/2020.
 */
public class ReportService {



    private static final String SHUN_FENG = "顺丰到付";

    private static final String WAIT_SELLER_SEND_GOODS = "WAIT_SELLER_SEND_GOODS";

    private ProductTransformer productTransformer = new ProductTransformer();

    public void execute(List<JsonRootBean> jsonRootBeanList) {

        List<List<ReportItem>> reportItemsList = new ArrayList<>();

        for (JsonRootBean jsonRootBean : jsonRootBeanList) {
            List<ReportItem> reportItems = parseToReportItemList(jsonRootBean);

            productTransformer.execute(reportItems);

            reportItemsList.add(reportItems);
        }

        Date date = new Date();
        String strDateFormat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String dateStr = sdf.format(date);

        File templateFile = new File(Constants.TEMPLATE_FILE_PATH);

        try(InputStream is = new FileInputStream(templateFile)) {

            File file = new File(Constants.OUTPUT_FILE);
            if(!file.exists()){
                file.mkdir();
            }

            try (OutputStream os = new FileOutputStream(Constants.OUTPUT_FILE + "/仙都淘宝订单报表_" + dateStr + ".xlsx")) {
                Context context = new Context();
                context.putVar("reportItemList", reportItemsList.get(0));
                context.putVar("reportItemList1", reportItemsList.get(1));
                JxlsHelper.getInstance().processTemplate(is, os, context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    protected List<ReportItem> parseToReportItemList (JsonRootBean jsonRootBean) {
        List<Trade> tradeList = jsonRootBean.getBody().getTradeListResponse().getTrades().getTrade();

        List<Trade> normalizeTradeList = normalizeTradeList(tradeList);

        List<ReportItem> reportItemList = new ArrayList<>();

        int i = 0;

        String lastMergeTid = "";

        for (Trade trade : normalizeTradeList) {
            List<Order> orderList = trade.getOrders().getOrder();

            orderList.removeIf( targetOrder -> !targetOrder.getStatus().equalsIgnoreCase(WAIT_SELLER_SEND_GOODS));


            if (trade.getMergeTid() == null || !trade.getMergeTid().equalsIgnoreCase(lastMergeTid)) {
                i++;
            }

            lastMergeTid = trade.getMergeTid();

            for(Order order : orderList) {
                ReportItem reportItem = new ReportItem();

                reportItem.setOrderId(i);
                reportItem.setReceiverName(trade.getReceiver_name());
                reportItem.setReceiverState(trade.getReceiver_state());
                reportItem.setReceiverCity(trade.getReceiver_city());
                reportItem.setReceiverDistrict(trade.getReceiver_district());

                reportItem.setProductName(order.getTitle());
                reportItem.setSkuName(order.getSku_properties_name());
                reportItem.setSkuId(order.getSku_id());
                reportItem.setOriginalQty(order.getNum());

                reportItem.setSellerMemo(trade.getSeller_memo());

                reportItemList.add(reportItem);
            }

            List<Integer> shunFengOrderIdList = reportItemList
                    .stream()
                    .filter(reportItem -> reportItem.getProductName().contains(SHUN_FENG))
                    .map(ReportItem::getOrderId)
                    .collect(Collectors.toList());


            if (shunFengOrderIdList != null) {
                reportItemList.stream()
                        .filter(item -> shunFengOrderIdList.contains(item.getOrderId()))
                        .forEach(item -> item.setLogistic(SHUN_FENG));

            }


            orderList.removeIf(matchedOrder -> matchedOrder.getTitle().contains(SHUN_FENG));

        }

        return reportItemList;

    }

    protected List<Trade> normalizeTradeList (List<Trade> originalTradeList) {

        List<Trade> normalizedTradeList = new ArrayList<>();

        originalTradeList.forEach(originalTrade -> {

            if (originalTrade.getTrades() == null){
                normalizedTradeList.add(originalTrade);
            } else {
                originalTrade.getTrades().forEach(trade -> trade.setMergeTid(originalTrade.getMergeTid()));
                normalizedTradeList.addAll(originalTrade.getTrades());
            }

        });

        //order by time
        Collections.reverse(normalizedTradeList);

        return normalizedTradeList;

    }
}
