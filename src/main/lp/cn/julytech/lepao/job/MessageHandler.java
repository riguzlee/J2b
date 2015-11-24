package cn.julytech.lepao.job;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jfinal.log.Logger;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InMsg;

public class MessageHandler implements Runnable {
    private static Logger                            logger   = Logger.getLogger(MessageHandler.class.getName());

    public static final ConcurrentLinkedQueue<InMsg> msgQueue = new ConcurrentLinkedQueue<InMsg>();

    static final ExecutorService                     pool     = Executors.newCachedThreadPool();

    @Override
    public void run() {
        logger.info("=>Running : MessageHandleJob...");
        InMsg item = null;
        while (true) {
            item = msgQueue.poll();
            if (item == null) {
                continue;
            }
            if (item instanceof InImageMsg) {
                InImageMsg imgMsg = (InImageMsg) item;
                logger.info("Pushing job to queue...");
                pool.execute(new SyncImageJob(imgMsg));
            }
        }
    }

}
