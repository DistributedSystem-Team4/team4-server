package Distributed.System.team4server.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class HdfsService {

    @Value("${hadoop.fs.defaultFS}")
    private String hdfsFS;

    public void uploadHdfs(String file) {
        try {
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", hdfsFS);
            FileSystem fileSystem = FileSystem.get(conf);

            Path path = new Path("/logloadbalancer/user.txt");

            OutputStream outputStream = fileSystem.append(path);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(file);
            bufferedWriter.newLine();

            bufferedWriter.close();
            fileSystem.close();
        } catch (IOException e) {
            log.error("HDFS IOException. msg:{}", e);
        }
    }
}
