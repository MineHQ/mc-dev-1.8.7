package io.netty.channel.rxtx;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelConfig;
import io.netty.channel.rxtx.RxtxChannelOption;
import java.util.Map;

final class DefaultRxtxChannelConfig extends DefaultChannelConfig implements RxtxChannelConfig {
   private volatile int baudrate = 115200;
   private volatile boolean dtr;
   private volatile boolean rts;
   private volatile RxtxChannelConfig.Stopbits stopbits;
   private volatile RxtxChannelConfig.Databits databits;
   private volatile RxtxChannelConfig.Paritybit paritybit;
   private volatile int waitTime;
   private volatile int readTimeout;

   DefaultRxtxChannelConfig(RxtxChannel var1) {
      super(var1);
      this.stopbits = RxtxChannelConfig.Stopbits.STOPBITS_1;
      this.databits = RxtxChannelConfig.Databits.DATABITS_8;
      this.paritybit = RxtxChannelConfig.Paritybit.NONE;
      this.readTimeout = 1000;
   }

   public Map<ChannelOption<?>, Object> getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{RxtxChannelOption.BAUD_RATE, RxtxChannelOption.DTR, RxtxChannelOption.RTS, RxtxChannelOption.STOP_BITS, RxtxChannelOption.DATA_BITS, RxtxChannelOption.PARITY_BIT, RxtxChannelOption.WAIT_TIME});
   }

   public <T> T getOption(ChannelOption<T> var1) {
      return var1 == RxtxChannelOption.BAUD_RATE?Integer.valueOf(this.getBaudrate()):(var1 == RxtxChannelOption.DTR?Boolean.valueOf(this.isDtr()):(var1 == RxtxChannelOption.RTS?Boolean.valueOf(this.isRts()):(var1 == RxtxChannelOption.STOP_BITS?this.getStopbits():(var1 == RxtxChannelOption.DATA_BITS?this.getDatabits():(var1 == RxtxChannelOption.PARITY_BIT?this.getParitybit():(var1 == RxtxChannelOption.WAIT_TIME?Integer.valueOf(this.getWaitTimeMillis()):(var1 == RxtxChannelOption.READ_TIMEOUT?Integer.valueOf(this.getReadTimeout()):super.getOption(var1))))))));
   }

   public <T> boolean setOption(ChannelOption<T> var1, T var2) {
      this.validate(var1, var2);
      if(var1 == RxtxChannelOption.BAUD_RATE) {
         this.setBaudrate(((Integer)var2).intValue());
      } else if(var1 == RxtxChannelOption.DTR) {
         this.setDtr(((Boolean)var2).booleanValue());
      } else if(var1 == RxtxChannelOption.RTS) {
         this.setRts(((Boolean)var2).booleanValue());
      } else if(var1 == RxtxChannelOption.STOP_BITS) {
         this.setStopbits((RxtxChannelConfig.Stopbits)var2);
      } else if(var1 == RxtxChannelOption.DATA_BITS) {
         this.setDatabits((RxtxChannelConfig.Databits)var2);
      } else if(var1 == RxtxChannelOption.PARITY_BIT) {
         this.setParitybit((RxtxChannelConfig.Paritybit)var2);
      } else if(var1 == RxtxChannelOption.WAIT_TIME) {
         this.setWaitTimeMillis(((Integer)var2).intValue());
      } else {
         if(var1 != RxtxChannelOption.READ_TIMEOUT) {
            return super.setOption(var1, var2);
         }

         this.setReadTimeout(((Integer)var2).intValue());
      }

      return true;
   }

   public RxtxChannelConfig setBaudrate(int var1) {
      this.baudrate = var1;
      return this;
   }

   public RxtxChannelConfig setStopbits(RxtxChannelConfig.Stopbits var1) {
      this.stopbits = var1;
      return this;
   }

   public RxtxChannelConfig setDatabits(RxtxChannelConfig.Databits var1) {
      this.databits = var1;
      return this;
   }

   public RxtxChannelConfig setParitybit(RxtxChannelConfig.Paritybit var1) {
      this.paritybit = var1;
      return this;
   }

   public int getBaudrate() {
      return this.baudrate;
   }

   public RxtxChannelConfig.Stopbits getStopbits() {
      return this.stopbits;
   }

   public RxtxChannelConfig.Databits getDatabits() {
      return this.databits;
   }

   public RxtxChannelConfig.Paritybit getParitybit() {
      return this.paritybit;
   }

   public boolean isDtr() {
      return this.dtr;
   }

   public RxtxChannelConfig setDtr(boolean var1) {
      this.dtr = var1;
      return this;
   }

   public boolean isRts() {
      return this.rts;
   }

   public RxtxChannelConfig setRts(boolean var1) {
      this.rts = var1;
      return this;
   }

   public int getWaitTimeMillis() {
      return this.waitTime;
   }

   public RxtxChannelConfig setWaitTimeMillis(int var1) {
      if(var1 < 0) {
         throw new IllegalArgumentException("Wait time must be >= 0");
      } else {
         this.waitTime = var1;
         return this;
      }
   }

   public RxtxChannelConfig setReadTimeout(int var1) {
      if(var1 < 0) {
         throw new IllegalArgumentException("readTime must be >= 0");
      } else {
         this.readTimeout = var1;
         return this;
      }
   }

   public int getReadTimeout() {
      return this.readTimeout;
   }

   public RxtxChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public RxtxChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public RxtxChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public RxtxChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public RxtxChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public RxtxChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public RxtxChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public RxtxChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public RxtxChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public RxtxChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      super.setMessageSizeEstimator(var1);
      return this;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      return this.setMessageSizeEstimator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setWriteBufferLowWaterMark(int var1) {
      return this.setWriteBufferLowWaterMark(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setWriteBufferHighWaterMark(int var1) {
      return this.setWriteBufferHighWaterMark(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setAutoClose(boolean var1) {
      return this.setAutoClose(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setAutoRead(boolean var1) {
      return this.setAutoRead(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      return this.setRecvByteBufAllocator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setAllocator(ByteBufAllocator var1) {
      return this.setAllocator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setWriteSpinCount(int var1) {
      return this.setWriteSpinCount(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setMaxMessagesPerRead(int var1) {
      return this.setMaxMessagesPerRead(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setConnectTimeoutMillis(int var1) {
      return this.setConnectTimeoutMillis(var1);
   }
}
