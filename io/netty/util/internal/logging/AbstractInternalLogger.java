package io.netty.util.internal.logging;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.ObjectStreamException;
import java.io.Serializable;

public abstract class AbstractInternalLogger implements InternalLogger, Serializable {
   private static final long serialVersionUID = -6382972526573193470L;
   private final String name;

   protected AbstractInternalLogger(String var1) {
      if(var1 == null) {
         throw new NullPointerException("name");
      } else {
         this.name = var1;
      }
   }

   public String name() {
      return this.name;
   }

   public boolean isEnabled(InternalLogLevel var1) {
      switch(AbstractInternalLogger.SyntheticClass_1.$SwitchMap$io$netty$util$internal$logging$InternalLogLevel[var1.ordinal()]) {
      case 1:
         return this.isTraceEnabled();
      case 2:
         return this.isDebugEnabled();
      case 3:
         return this.isInfoEnabled();
      case 4:
         return this.isWarnEnabled();
      case 5:
         return this.isErrorEnabled();
      default:
         throw new Error();
      }
   }

   public void log(InternalLogLevel var1, String var2, Throwable var3) {
      switch(AbstractInternalLogger.SyntheticClass_1.$SwitchMap$io$netty$util$internal$logging$InternalLogLevel[var1.ordinal()]) {
      case 1:
         this.trace(var2, var3);
         break;
      case 2:
         this.debug(var2, var3);
         break;
      case 3:
         this.info(var2, var3);
         break;
      case 4:
         this.warn(var2, var3);
         break;
      case 5:
         this.error(var2, var3);
         break;
      default:
         throw new Error();
      }

   }

   public void log(InternalLogLevel var1, String var2) {
      switch(AbstractInternalLogger.SyntheticClass_1.$SwitchMap$io$netty$util$internal$logging$InternalLogLevel[var1.ordinal()]) {
      case 1:
         this.trace(var2);
         break;
      case 2:
         this.debug(var2);
         break;
      case 3:
         this.info(var2);
         break;
      case 4:
         this.warn(var2);
         break;
      case 5:
         this.error(var2);
         break;
      default:
         throw new Error();
      }

   }

   public void log(InternalLogLevel var1, String var2, Object var3) {
      switch(AbstractInternalLogger.SyntheticClass_1.$SwitchMap$io$netty$util$internal$logging$InternalLogLevel[var1.ordinal()]) {
      case 1:
         this.trace(var2, var3);
         break;
      case 2:
         this.debug(var2, var3);
         break;
      case 3:
         this.info(var2, var3);
         break;
      case 4:
         this.warn(var2, var3);
         break;
      case 5:
         this.error(var2, var3);
         break;
      default:
         throw new Error();
      }

   }

   public void log(InternalLogLevel var1, String var2, Object var3, Object var4) {
      switch(AbstractInternalLogger.SyntheticClass_1.$SwitchMap$io$netty$util$internal$logging$InternalLogLevel[var1.ordinal()]) {
      case 1:
         this.trace(var2, var3, var4);
         break;
      case 2:
         this.debug(var2, var3, var4);
         break;
      case 3:
         this.info(var2, var3, var4);
         break;
      case 4:
         this.warn(var2, var3, var4);
         break;
      case 5:
         this.error(var2, var3, var4);
         break;
      default:
         throw new Error();
      }

   }

   public void log(InternalLogLevel var1, String var2, Object... var3) {
      switch(AbstractInternalLogger.SyntheticClass_1.$SwitchMap$io$netty$util$internal$logging$InternalLogLevel[var1.ordinal()]) {
      case 1:
         this.trace(var2, var3);
         break;
      case 2:
         this.debug(var2, var3);
         break;
      case 3:
         this.info(var2, var3);
         break;
      case 4:
         this.warn(var2, var3);
         break;
      case 5:
         this.error(var2, var3);
         break;
      default:
         throw new Error();
      }

   }

   protected Object readResolve() throws ObjectStreamException {
      return InternalLoggerFactory.getInstance(this.name());
   }

   public String toString() {
      return StringUtil.simpleClassName((Object)this) + '(' + this.name() + ')';
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$util$internal$logging$InternalLogLevel = new int[InternalLogLevel.values().length];

      static {
         try {
            $SwitchMap$io$netty$util$internal$logging$InternalLogLevel[InternalLogLevel.TRACE.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$io$netty$util$internal$logging$InternalLogLevel[InternalLogLevel.DEBUG.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$io$netty$util$internal$logging$InternalLogLevel[InternalLogLevel.INFO.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$io$netty$util$internal$logging$InternalLogLevel[InternalLogLevel.WARN.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$util$internal$logging$InternalLogLevel[InternalLogLevel.ERROR.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
