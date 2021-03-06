package charserver.cn.net.bhe.app.timer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import charserver.cn.net.bhe.app.dict.Prop;
import charserver.cn.net.bhe.app.service.WalletService;

@Service
public class Timer {

    @Autowired
    private WalletService walletService;
    static Logger LOGGER = LoggerFactory.getLogger(Timer.class);

    @Scheduled(fixedDelay = Prop.ORDER_UNPAID_TIMER)
    public void unpaidOrderProcess() {
        if (!Prop.TIMER_ENABLED) {
            return;
        }
        List<String> ids = walletService.unpaidOrderIdList();
        for (String id : ids) {
            walletService.unpaidOrderStatusUpdate(id);
        }
    }

}
