package com.xiandu.runner;

import com.google.gson.Gson;
import com.xiandu.factory.XianduGsonFactory;
import com.xiandu.service.ArchiveService;
import com.xiandu.service.ReportService;
import com.xiandu.model.JsonRootBean;
import com.xiandu.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 8/7/2020.
 */
public class ReportRunner {

    private ReportService reportService = new ReportService();
    private ArchiveService archiveService = new ArchiveService();

    public void execute() {
        Map<String, List<JsonRootBean>> jsonMerchantMap = new HashMap<>();

        try {
            Path dirPath = Paths.get(Constants.INPUT_PATH);
            List<Path> filePaths = Files.list(dirPath).collect(Collectors.toList());
            for (Path filePath : filePaths) {
                List<String> jsonStrList = Files.readAllLines(filePath);
                String jsonStr = StringUtils.join(jsonStrList, StringUtils.EMPTY);
                Gson gson = XianduGsonFactory.getXianduGson();
                String merchantKey = "";
                if (filePath.getFileName().toString().startsWith(Constants.INPUT_FILE_NAME_1_PREFIX)) {
                    merchantKey = Constants.MERCHANT_1;
                } else if (filePath.getFileName().toString().startsWith(Constants.INPUT_FILE_NAME_2_PREFIX)) {
                    merchantKey = Constants.MERCHANT_2;
                }
                if (jsonMerchantMap.get(merchantKey) == null) {
                    jsonMerchantMap.put(merchantKey, new ArrayList<>());
                }
                jsonMerchantMap.get(merchantKey).add(gson.fromJson(jsonStr, JsonRootBean.class));

            }
            reportService.execute(jsonMerchantMap);
            archiveService.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ReportRunner runner = new ReportRunner();
        runner.execute();
    }
}
