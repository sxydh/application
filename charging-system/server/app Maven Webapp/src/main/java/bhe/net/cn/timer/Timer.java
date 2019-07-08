package bhe.net.cn.timer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import bhe.net.cn.service.WalletService;

@Service
public class Timer {

    @Autowired
    private WalletService walletServie;
    static Logger LOGGER = LoggerFactory.getLogger(Timer.class);

    @Scheduled(fixedDelay = 60 * 1000)
    public void unpaidOrderProcess() {
        List<String> ids = walletServie.unpaidOrderIdList();
        for (String id : ids) {
            walletServie.unpaidOrderStatusUpdate(id);
        }
    }

}
