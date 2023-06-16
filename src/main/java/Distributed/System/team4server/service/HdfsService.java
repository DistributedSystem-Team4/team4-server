package Distributed.System.team4server.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HdfsService {

    @Value("${hadoop.fs.defaultFS}")
    private String hdfsFS;

    private List<String> logCache = new ArrayList<>();
    private int logCacheThreshold = 300;

    public void cacheLog(String log) {
        logCache.add(log);

        if (logCache.size() >= logCacheThreshold) {
            writeToHdfs();
        }
    }

    private void writeToHdfs() {
        try {
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", hdfsFS);
            FileSystem hdfs = FileSystem.get(conf);
            Path filePath = new Path("/logloadbalancer/user.txt");

            // HDFS 파일이 이미 존재하는지 확인
            if (!hdfs.exists(filePath)) {
                // 존재하지 않으면 새로 생성
                hdfs.createNewFile(filePath);
            }

            // 파일에 append 모드로 작성하기 위해 OutputStream 생성
            OutputStream outputStream = hdfs.append(filePath);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

            // 로그 캐시의 모든 로그를 파일에 작성
            for (String log : logCache) {
                bufferedWriter.write(log);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            outputStream.close();

            // 캐시 비우기
            logCache.clear();
        } catch (IOException e) {
            log.error("HDFS IOException. msg={}", e.getMessage());
        }
    }
}
