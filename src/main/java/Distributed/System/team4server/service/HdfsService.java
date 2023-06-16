package Distributed.System.team4server.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class HdfsService {

    private final Configuration hadoopConfig;

    public void uploadHdfs(String file) {
        try {
            FileSystem hdfs = FileSystem.get(hadoopConfig);

            Path path = new Path("/logloadbalancer/user.txt");

            OutputStream outStream = hdfs.create(path);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
            writer.write(file);

            writer.close();
            hdfs.close();
        } catch (IOException e) {
            log.error("HDFS IOException. msg:{}", e);
        }
    }
}
