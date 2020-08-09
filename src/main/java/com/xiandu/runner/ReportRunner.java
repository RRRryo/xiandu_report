package com.xiandu.runner;

import com.google.gson.Gson;
import com.xiandu.com.xiandu.factory.XianduGsonFactory;
import com.xiandu.com.xiandu.service.ReportService;
import com.xiandu.model.JsonRootBean;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by Administrator on 8/7/2020.
 */
public class ReportRunner {

    public static void main(String[] args) {

        try {

            ReportService reportService = new ReportService();

            String inputFile = "c://xiandu/input/trade.json";

            File file = new File(inputFile);

            Path path = file.toPath();

            List<String> jsonStrList = Files.readAllLines(path);
            String jsonStr = StringUtils.join(jsonStrList, "");

            Gson gson = XianduGsonFactory.getXianduGson();

            JsonRootBean jsonRootBean = gson.fromJson(jsonStr, JsonRootBean.class);

            reportService.execute(jsonRootBean);
            
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
