package com.xiandu.runner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.deploy.util.StringUtils;
import com.xiandu.com.xiandu.factory.XianduGsonFactory;
import com.xiandu.model.JsonRootBean;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Administrator on 8/7/2020.
 */
public class ReportRunner {

    public static void main(String[] args) {


        try {
            Path path = Paths.get(ReportRunner.class.getClassLoader().getResource("trade.json").toURI());

            List<String> jsonStrList = Files.readAllLines(path);
            String jsonStr = StringUtils.join(jsonStrList, "");


            Gson gson = XianduGsonFactory.getXianduGson();

            JsonRootBean jsonRootBean = gson.fromJson(jsonStr, JsonRootBean.class);

            System.out.println(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }
}
