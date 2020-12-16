package com.xiandu.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiandu.model.Product;
import com.xiandu.model.ReportItem;
import com.xiandu.model.Sku;
import com.xiandu.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by Administrator on 8/9/2020.
 */
public class ProductTransformer {

    public void execute(List<ReportItem> reportItemList) {


        Path productFile = new File(Constants.PRODUCT_META_FILE).toPath();

        Path skuFile = new File(Constants.SKU_META_FILE).toPath();

        List<String> productStrList = null;
        List<String> skuStrList = null;
        try {
            productStrList = Files.readAllLines(productFile);
            skuStrList = Files.readAllLines(skuFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String productStr = StringUtils.join(productStrList, "");
        String skuStr = StringUtils.join(skuStrList, "");

        Gson gsonProduct = new Gson();

        Gson skuProduct = new Gson();

        List<Product> productList = gsonProduct.fromJson(productStr, new TypeToken<List<Product>>(){}.getType());

        List<Sku> skuList = skuProduct.fromJson(skuStr, new TypeToken<List<Sku>>(){}.getType());

        for (ReportItem reportItem : reportItemList) {

            Optional<Product> product = productList.stream()
                    .filter(targetProduct ->
                            reportItem.getProductName().contains(targetProduct.getProduct_long_name()))
                    .findFirst();

            if (product.isPresent()) {
                reportItem.setProductShortName(product.get().getProduct_short_name());
            }

            if (reportItem.getSkuName() != null) {
                Optional<Sku> sku = skuList.stream()
                        .filter(targetSku ->
                                reportItem.getSkuName().contains(targetSku.getSku_properties_name()))
                        .findFirst();

                if (sku.isPresent()) {
                    String skuShortName = sku.get().getSku_short_name();
                    int skuRatio = sku.get().getSku_ratio();

                    reportItem.setSkuShortName(skuShortName);
                    reportItem.setSkuRatio(skuRatio);
                }
            }
            reportItem.setConvQty(reportItem.getOriginalQty() * reportItem.getSkuRatio());

        }
        splitBySkuShortName(reportItemList);
    }

    private void splitBySkuShortName(List<ReportItem> reportItemList) {

        List<ReportItem> splittedList = new ArrayList<>();

        for (ReportItem reportItem : reportItemList) {

            String skuShortName = reportItem.getSkuShortName();

            if (skuShortName != null) {
                List<String> subSkuShortNameList = Arrays.asList(skuShortName.split("\\|"));

                for (String subSkuShortName : subSkuShortNameList) {

                    ReportItem ri = new ReportItem(reportItem);
                    if (reportItem.getProductName().contains(Constants.COMPOSITE_STR_PATTERN)) {
                        //composite products

                        String[] array = subSkuShortName.split("\\*");
                        ri.setSkuShortName(array[0]);
                        int ratio = Integer.parseInt(array[1]);
                        ri.setConvQty(ri.getOriginalQty() * ratio);
                        ri.setProductShortName(StringUtils.EMPTY);
                    }

                    splittedList.add(ri);
                }

            } else {
                splittedList.add(reportItem);
            }
        }

        reportItemList.clear();
        reportItemList.addAll(splittedList);

    }

}
