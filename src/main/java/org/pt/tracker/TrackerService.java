package org.pt.tracker;
import com.turn.ttorrent.tracker.Tracker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.pt.config.Constants.TRACKER_PORT;

@Service
public class TrackerService {

    private Tracker tracker;
    @Value("${torrent.storage.path}")
    private String torrentStoragePath;

    public void startTracker() throws IOException {
        // 创建一个临时目录来存储Torrent信息
        Path torrentsPath = Paths.get(torrentStoragePath);
        if (!Files.exists(torrentsPath)) {
            Files.createDirectory(torrentsPath);
        }

        // 启动Tracker，监听8080端口
        tracker = new Tracker(new InetSocketAddress(TRACKER_PORT));

        // 启动Tracker
        tracker.start();
    }

    public void stopTracker() {
        if (tracker != null) {
            tracker.stop();
        }
    }
}