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
import java.util.Optional;
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

    }
}
