package com.xiandu.service;

import com.xiandu.model.JsonRootBean;
import com.xiandu.model.Order;
import com.xiandu.model.ReportItem;
import com.xiandu.model.Trade;
import com.xiandu.utils.Constants;
import com.xiandu.utils.DateUtils;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 8/7/2020.
 */
public class ReportService {

    private static final String SHUN_FENG = "顺丰到付";

    private static final String WAIT_SELLER_SEND_GOODS = "WAIT_SELLER_SEND_GOODS";

    private ProductTransformer productTransformer = new ProductTransformer();

    public void execute(Map<String, List<JsonRootBean>> jsonRootBeanListMap) {
        Map<String, List<ReportItem>> reportItemMap = new HashMap<>();
        jsonRootBeanListMap.entrySet().stream().forEach( entry -> {
            List<JsonRootBean> jsonRootBeans = entry.getValue().stream()
                    .collect(Collectors.toList());
            List<ReportItem> reportItems = parseToReportItemList(jsonRootBeans);
            productTransformer.execute(reportItems);
            reportItemMap.put(entry.getKey(), reportItems);
        });

        File templateFile = new File(Constants.TEMPLATE_FILE_PATH);
        try(InputStream is = new FileInputStream(templateFile)) {
            File file = new File(Constants.OUTPUT_PATH);
            if(!file.exists()){
                file.mkdir();
            }
            try (OutputStream os = new FileOutputStream(Constants.OUTPUT_PATH + "/仙都淘宝订单报表_" + DateUtils.getTodayIsoFormat() + ".xlsx")) {
                Context context = new Context();
                context.putVar("reportItemList", reportItemMap.get(Constants.MERCHANT_1));
                context.putVar("reportItemList1", reportItemMap.get(Constants.MERCHANT_2));
                JxlsHelper.getInstance().processTemplate(is, os, context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected List<ReportItem> parseToReportItemList (List<JsonRootBean> jsonRootBeanList) {
        int orderId = 0;
        List<ReportItem> reportItemList = new ArrayList<>();
        for (JsonRootBean jsonRootBean : jsonRootBeanList) {
            if(jsonRootBean.getBody().getTradeListResponse().getTrades() == null) {
                return new ArrayList<>();
            }
            List<Trade> tradeList = jsonRootBean.getBody().getTradeListResponse().getTrades().getTrade();
            List<Trade> normalizeTradeList = normalizeTradeList(tradeList);
            String lastMergeTid = "";
            for (Trade trade : normalizeTradeList) {
                List<Order> orderList = trade.getOrders().getOrder();
                orderList.removeIf( targetOrder -> !targetOrder.getStatus().equalsIgnoreCase(WAIT_SELLER_SEND_GOODS));
                if (trade.getMergeTid() == null || !trade.getMergeTid().equalsIgnoreCase(lastMergeTid)) {
                    orderId++;
                }
                lastMergeTid = trade.getMergeTid();
                for(Order order : orderList) {
                    ReportItem reportItem = new ReportItem();
                    reportItem.setOrderId(orderId);
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
                orderList.removeIf(matchedOrder -> matchedOrder.getTitle().contains(SHUN_FENG));
            }
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
//        Collections.reverse(normalizedTradeList);

        return normalizedTradeList;

    }
}
