package com.xiandu.com.xiandu.service;

import com.xiandu.model.JsonRootBean;
import com.xiandu.model.Order;
import com.xiandu.model.ReportItem;
import com.xiandu.model.Trade;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 8/7/2020.
 */
public class ReportService {

    private static String outputFile = "c://xiandu/output";

    private static final String SHUN_FENG = "顺丰到付";

    private ProductTransformer productTransformer = new ProductTransformer();

    public void execute(JsonRootBean jsonRootBean) {

        List<ReportItem> reportItemList = parseToReportItemList(jsonRootBean);

        productTransformer.execute(reportItemList);

        Date date = new Date();
        String strDateFormat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String dateStr = sdf.format(date);

        String templateFilePath = "c://xiandu/input/report_template.xlsx";

        File templateFile = new File(templateFilePath);

        try(InputStream is = new FileInputStream(templateFile)) {

            File file = new File(outputFile);
            if(!file.exists()){
                file.mkdir();
            }

            try (OutputStream os = new FileOutputStream(outputFile + "/仙都淘宝订单报表_" + dateStr + ".xlsx")) {
                Context context = new Context();
                context.putVar("reportItemList", reportItemList);
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

        for (Trade trade : normalizeTradeList) {
            List<Order> orderList = trade.getOrders().getOrder();

            i++;

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

            if (orderList.stream().filter(order -> order.getTitle().contains(SHUN_FENG)).count() > 0) {
                reportItemList.forEach( reportItem -> reportItem.setLogistic(SHUN_FENG));
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
                normalizedTradeList.addAll(originalTrade.getTrades());
            }

        });

        //order by time
        Collections.reverse(normalizedTradeList);

        return normalizedTradeList;

    }
}
