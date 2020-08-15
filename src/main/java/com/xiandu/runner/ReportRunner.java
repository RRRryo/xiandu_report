package com.xiandu.runner;

import com.google.gson.Gson;
import com.xiandu.factory.XianduGsonFactory;
import com.xiandu.service.ReportService;
import com.xiandu.model.JsonRootBean;
import com.xiandu.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 8/7/2020.
 */
public class ReportRunner {

    public static void main(String[] args) {

        try {

            ReportService reportService = new ReportService();

            List<JsonRootBean> jsonRootBeanList = new ArrayList<>();

            for (int i=0; i< 2; i++) {

                String filePath = i == 0 ? Constants.INPUT_FILE_1 : Constants.INPUT_FILE_2;

                File file = new File(filePath);

                Path path = file.toPath();

                List<String> jsonStrList = Files.readAllLines(path);
                String jsonStr = StringUtils.join(jsonStrList, "");

                Gson gson = XianduGsonFactory.getXianduGson();

                jsonRootBeanList.add(gson.fromJson(jsonStr, JsonRootBean.class));

            }

            reportService.execute(jsonRootBeanList);
            
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
