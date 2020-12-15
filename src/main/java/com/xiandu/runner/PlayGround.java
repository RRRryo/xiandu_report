package com.xiandu.runner;

import com.google.gson.Gson;
import com.xiandu.factory.XianduGsonFactory;
import com.xiandu.model.JsonRootBean;
import com.xiandu.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Administrator on 12/12/2020.
 */
public class PlayGround {

    public static void main(String[] args) {
        List<JsonRootBean> jsonRootBeanList = new ArrayList<>();
        try {
            Path dirPath = Paths.get(Constants.ROOT_PATH + "/input/");
            List<Path> filePaths = Files.list(dirPath).collect(Collectors.toList());
            for (Path filePath : filePaths) {
                List<String> jsonStrList = Files.readAllLines(filePath);
                String jsonStr = StringUtils.join(jsonStrList, StringUtils.EMPTY);
                Gson gson = XianduGsonFactory.getXianduGson();
                jsonRootBeanList.add(gson.fromJson(jsonStr, JsonRootBean.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
