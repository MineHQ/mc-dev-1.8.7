package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceReportingEventHandler;
import org.apache.logging.log4j.core.async.RingBufferLogEvent;

public class RingBufferLogEventHandler implements SequenceReportingEventHandler<RingBufferLogEvent> {
   private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
   private Sequence sequenceCallback;
   private int counter;

   public RingBufferLogEventHandler() {
   }

   public void setSequenceCallback(Sequence var1) {
      this.sequenceCallback = var1;
   }

   public void onEvent(RingBufferLogEvent var1, long var2, boolean var4) throws Exception {
      var1.execute(var4);
      var1.clear();
      if(++this.counter > 50) {
         this.sequenceCallback.set(var2);
         this.counter = 0;
      }

   }

   // $FF: synthetic method
   // $FF: bridge method
   public void onEvent(Object var1, long var2, boolean var4) throws Exception {
      this.onEvent((RingBufferLogEvent)var1, var2, var4);
   }
}
