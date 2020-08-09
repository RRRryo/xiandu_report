package com.xiandu.com.xiandu.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiandu.model.Product;
import com.xiandu.model.ReportItem;
import com.xiandu.model.Sku;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 8/9/2020.
 */
public class ProductTransformer {

    public void execute(List<ReportItem> reportItemList) {

        String productFileName = "c://xiandu/input/product_meta.json";

        String skuFileName = "c://xiandu/input/sku_meta.json";

        Path productFile = new File(productFileName).toPath();

        Path skuFile = new File(skuFileName).toPath();


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

        Map<String, String> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getProduct_long_name, Product::getProduct_short_name));

        Map<String, Sku> skuMap = skuList.stream().collect(Collectors.toMap(Sku::getSku_properties_name, sku -> sku));


        for (ReportItem reportItem : reportItemList) {

            String productShortName = productMap.get(reportItem.getProductName());

            reportItem.setProductShortName(productShortName);

            Sku sku = skuMap.get(reportItem.getSkuName());
            if (sku != null) {
                String skuShortName = sku.getSku_short_name();
                int skuRatio = sku.getSku_ratio();

                reportItem.setSkuShortName(skuShortName);
                reportItem.setSkuRatio(skuRatio);
            }
            reportItem.setConvQty(reportItem.getOriginalQty() * reportItem.getSkuRatio());

        }

    }
}
