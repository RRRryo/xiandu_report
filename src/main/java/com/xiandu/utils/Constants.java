package com.xiandu.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Administrator on 8/10/2020.
 */
public class Constants {

    public static final String MERCHANT_1 = "MERCHANT_1";
    public static final String MERCHANT_2 = "MERCHANT_2";
    public static final String MERCHANT_3 = "MERCHANT_3";
    public static final String MERCHANT_4 = "MERCHANT_4";

    public static final String INPUT_FILE_NAME_1_PREFIX = "trade_1";
    public static final String INPUT_FILE_NAME_2_PREFIX = "trade_2";
    public static final String INPUT_FILE_NAME_4_PREFIX = "trade_4";

    public static final Map<String, String> JSON_MERCHANT_MAP = ImmutableMap.of(
            INPUT_FILE_NAME_1_PREFIX, MERCHANT_1,
            INPUT_FILE_NAME_2_PREFIX, MERCHANT_2,
            INPUT_FILE_NAME_4_PREFIX, MERCHANT_4
    );

    public static String ROOT_PATH = "c://xiandu";
    public static String INPUT_PATH = ROOT_PATH + "/input/";

    public static final String PRODUCT_META_FILE = ROOT_PATH + "/config/product_meta.json";
    public static final String SKU_META_FILE = ROOT_PATH + "/config/sku_meta.json";
    public static final String TEMPLATE_FILE_PATH = ROOT_PATH + "/config/report_template.xlsx";
    public static final String OUTPUT_PATH = ROOT_PATH + "/output";
    public static final String ARCHIVE_PATH = ROOT_PATH + "/archive/";
    public static final String COMPOSITE_STR_PATTERN = "爆款组合";
}
