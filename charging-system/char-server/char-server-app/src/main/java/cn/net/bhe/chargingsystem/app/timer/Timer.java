package cn.net.bhe.chargingsystem.app.timer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.net.bhe.chargingsystem.app.dict.Prop;
import cn.net.bhe.chargingsystem.app.service.WalletService;

@Service
public class Timer {

    @Autowired
    private WalletService walletServie;
    static Logger LOGGER = LoggerFactory.getLogger(Timer.class);

    @Scheduled(fixedDelay = Prop.ORDER_UNPAID_TIMER)
    public void unpaidOrderProcess() {
        if (!Prop.TIMER_ENABLED) {
            return;
        }
        List<String> ids = walletServie.unpaidOrderIdList();
        for (String id : ids) {
            walletServie.unpaidOrderStatusUpdate(id);
        }
    }

}
