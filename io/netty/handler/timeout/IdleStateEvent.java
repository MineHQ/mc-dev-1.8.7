package io.netty.handler.timeout;

import io.netty.handler.timeout.IdleState;

public final class IdleStateEvent {
   public static final IdleStateEvent FIRST_READER_IDLE_STATE_EVENT;
   public static final IdleStateEvent READER_IDLE_STATE_EVENT;
   public static final IdleStateEvent FIRST_WRITER_IDLE_STATE_EVENT;
   public static final IdleStateEvent WRITER_IDLE_STATE_EVENT;
   public static final IdleStateEvent FIRST_ALL_IDLE_STATE_EVENT;
   public static final IdleStateEvent ALL_IDLE_STATE_EVENT;
   private final IdleState state;
   private final boolean first;

   private IdleStateEvent(IdleState var1, boolean var2) {
      this.state = var1;
      this.first = var2;
   }

   public IdleState state() {
      return this.state;
   }

   public boolean isFirst() {
      return this.first;
   }

   static {
      FIRST_READER_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.READER_IDLE, true);
      READER_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.READER_IDLE, false);
      FIRST_WRITER_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.WRITER_IDLE, true);
      WRITER_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.WRITER_IDLE, false);
      FIRST_ALL_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.ALL_IDLE, true);
      ALL_IDLE_STATE_EVENT = new IdleStateEvent(IdleState.ALL_IDLE, false);
   }
}
