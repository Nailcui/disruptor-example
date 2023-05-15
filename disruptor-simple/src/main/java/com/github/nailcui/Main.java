package com.github.nailcui;

import com.github.nailcui.disruptor.common.MsgEvent;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * @author nailcui
 * @date 2023-05-14 23:35
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        int bufferSize = 1024;
        Disruptor<MsgEvent> disruptor = new Disruptor<>(MsgEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            System.out.println("cost: " + (System.currentTimeMillis() - event.getTimestamp()));
        });
        disruptor.start();

        RingBuffer<MsgEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent((event, sequence, from) -> {
            event.setTimestamp(from);
        }, System.currentTimeMillis());

        Thread.sleep(2000);
    }

}
