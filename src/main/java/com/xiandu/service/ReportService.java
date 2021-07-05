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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 8/7/2020.
 */
public class ReportService {

    private static final String SHUN_FENG = "顺丰到付";
    private static final String WAIT_SELLER_SEND_GOODS = "WAIT_SELLER_SEND_GOODS";
    private final ProductTransformer productTransformer = new ProductTransformer();

    public void execute(Map<String, List<JsonRootBean>> jsonRootBeanListMap) {
        Map<String, List<ReportItem>> reportItemMap = new HashMap<>();
        jsonRootBeanListMap.forEach((merchantNb, value) -> {
            List<JsonRootBean> jsonRootBeans = new ArrayList<>(value);
            List<ReportItem> reportItems = parseToReportItemList(merchantNb, jsonRootBeans);
            productTransformer.execute(reportItems);
            reportItemMap.put(merchantNb, reportItems);
        });
        File templateFile = new File(Constants.TEMPLATE_FILE_PATH);
        try (InputStream is = new FileInputStream(templateFile)) {
            File file = new File(Constants.OUTPUT_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            try (OutputStream os = new FileOutputStream(Constants.OUTPUT_PATH + "/仙都淘宝订单报表_" + DateUtils.getTodayIsoFormat() + ".xlsx")) {
                Context context = new Context();
                context.putVar("reportItemList1", reportItemMap.get(Constants.MERCHANT_1));
                context.putVar("reportItemList2", reportItemMap.get(Constants.MERCHANT_2));
                context.putVar("reportItemList4", reportItemMap.get(Constants.MERCHANT_4));
                JxlsHelper.getInstance().processTemplate(is, os, context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param merchantNb
     * @param jsonRootBeanList
     * @return
     */
    protected List<ReportItem> parseToReportItemList(String merchantNb, List<JsonRootBean> jsonRootBeanList) {
        int orderId = 0;
        List<ReportItem> reportItemList = new ArrayList<>();
        for (JsonRootBean jsonRootBean : jsonRootBeanList) {
            if (jsonRootBean.getBody().getTradeListResponse().getTrades() == null) {
                return new ArrayList<>();
            }
            List<Trade> tradeList = jsonRootBean.getBody().getTradeListResponse().getTrades().getTrade();
            List<Trade> normalizeTradeList = normalizeTradeList(tradeList);
            //sort for MERCHANT_4 only
            if (Constants.MERCHANT_4.equals(merchantNb)) {
                normalizeTradeList.sort((trade1, trade2) -> {
                    String tradeId1 = trade1.getReceiver_address() + "::" + trade1.getReceiver_name() + "::" + trade1.getReceiver_mobile();
                    String tradeId2 = trade2.getReceiver_address() + "::" + trade2.getReceiver_name() + "::" + trade2.getReceiver_mobile();
                    return tradeId1.compareTo(tradeId2);
                });
            }
            String lastMergeTid = "";
            for (Trade trade : normalizeTradeList) {
                List<Order> orderList = trade.getOrders().getOrder();
                orderList.removeIf(targetOrder -> !targetOrder.getStatus().equalsIgnoreCase(WAIT_SELLER_SEND_GOODS));
                if (!Constants.MERCHANT_3.equals(merchantNb)) {
                    String currentId = trade.getReceiver_address() + "::" + trade.getReceiver_name() + "::" + trade.getReceiver_mobile();
                    if (!lastMergeTid.equals(currentId)) {
                        orderId++;
                    }
                    lastMergeTid = currentId;
                } else {
                    if (trade.getMergeTid() == null || !trade.getMergeTid().equalsIgnoreCase(lastMergeTid)) {
                        orderId++;
                    }
                    lastMergeTid = trade.getMergeTid();
                }
                for (Order order : orderList) {
                    ReportItem reportItem = new ReportItem();
                    reportItem.setOrderId(orderId);
                    fillReceiverName(reportItem, trade, merchantNb);
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
        reportItemList.stream()
                .filter(item -> shunFengOrderIdList.contains(item.getOrderId()))
                .forEach(item -> item.setLogistic(SHUN_FENG));
        return reportItemList;
    }

    private void fillReceiverName(ReportItem reportItem, Trade trade, String merchantId) {
        switch (merchantId) {
            case Constants.MERCHANT_1:
            case Constants.MERCHANT_2:
                reportItem.setReceiverName(trade.getReceiver_name() + trade.getReceiver_mobile().substring(trade.getReceiver_mobile().length() - 4));
                break;
            case Constants.MERCHANT_4:
                reportItem.setReceiverName(trade.getReceiver_name() + trade.getReceiver_mobile().substring(trade.getReceiver_mobile().length() - 2));
                break;
            default:
                reportItem.setReceiverName(trade.getReceiver_name());
                break;
        }
    }

    protected List<Trade> normalizeTradeList(List<Trade> originalTradeList) {
        List<Trade> normalizedTradeList = new ArrayList<>();
        originalTradeList.forEach(originalTrade -> {
            if (originalTrade.getTrades() == null) {
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
