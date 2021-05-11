package com.xiandu.service;

import com.xiandu.utils.Constants;
import com.xiandu.utils.DateUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 12/15/2020.
 */
public class ArchiveService {

    public void execute() {
        try {
            Path dirPath = Paths.get(Constants.INPUT_PATH);
            List<Path> filePaths = Files.list(dirPath).collect(Collectors.toList());
            File archiveDir = new File(Constants.ARCHIVE_PATH + DateUtils.getTodayIsoFormat());
            if (!archiveDir.exists()) {
                archiveDir.mkdir();
            }
            for (Path filePath : filePaths) {
                Path targetPath = Paths.get(archiveDir.getPath() + "/" + filePath.getFileName());
                Files.deleteIfExists(targetPath);
                Files.move(filePath, targetPath);
            }
            new File(Constants.INPUT_PATH + "trade_1_1.json").createNewFile();
            new File(Constants.INPUT_PATH + "trade_2_1.json").createNewFile();
            new File(Constants.INPUT_PATH + "trade_4_1.json").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
