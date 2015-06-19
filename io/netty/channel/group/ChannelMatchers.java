package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ServerChannel;
import io.netty.channel.group.ChannelMatcher;

public final class ChannelMatchers {
   private static final ChannelMatcher ALL_MATCHER = new ChannelMatcher() {
      public boolean matches(Channel var1) {
         return true;
      }
   };
   private static final ChannelMatcher SERVER_CHANNEL_MATCHER = isInstanceOf(ServerChannel.class);
   private static final ChannelMatcher NON_SERVER_CHANNEL_MATCHER = isNotInstanceOf(ServerChannel.class);

   private ChannelMatchers() {
   }

   public static ChannelMatcher all() {
      return ALL_MATCHER;
   }

   public static ChannelMatcher isNot(Channel var0) {
      return invert(is(var0));
   }

   public static ChannelMatcher is(Channel var0) {
      return new ChannelMatchers.InstanceMatcher(var0);
   }

   public static ChannelMatcher isInstanceOf(Class<? extends Channel> var0) {
      return new ChannelMatchers.ClassMatcher(var0);
   }

   public static ChannelMatcher isNotInstanceOf(Class<? extends Channel> var0) {
      return invert(isInstanceOf(var0));
   }

   public static ChannelMatcher isServerChannel() {
      return SERVER_CHANNEL_MATCHER;
   }

   public static ChannelMatcher isNonServerChannel() {
      return NON_SERVER_CHANNEL_MATCHER;
   }

   public static ChannelMatcher invert(ChannelMatcher var0) {
      return new ChannelMatchers.InvertMatcher(var0);
   }

   public static ChannelMatcher compose(ChannelMatcher... var0) {
      if(var0.length < 1) {
         throw new IllegalArgumentException("matchers must at least contain one element");
      } else {
         return (ChannelMatcher)(var0.length == 1?var0[0]:new ChannelMatchers.CompositeMatcher(var0));
      }
   }

   private static final class ClassMatcher implements ChannelMatcher {
      private final Class<? extends Channel> clazz;

      ClassMatcher(Class<? extends Channel> var1) {
         this.clazz = var1;
      }

      public boolean matches(Channel var1) {
         return this.clazz.isInstance(var1);
      }
   }

   private static final class InstanceMatcher implements ChannelMatcher {
      private final Channel channel;

      InstanceMatcher(Channel var1) {
         this.channel = var1;
      }

      public boolean matches(Channel var1) {
         return this.channel == var1;
      }
   }

   private static final class InvertMatcher implements ChannelMatcher {
      private final ChannelMatcher matcher;

      InvertMatcher(ChannelMatcher var1) {
         this.matcher = var1;
      }

      public boolean matches(Channel var1) {
         return !this.matcher.matches(var1);
      }
   }

   private static final class CompositeMatcher implements ChannelMatcher {
      private final ChannelMatcher[] matchers;

      CompositeMatcher(ChannelMatcher... var1) {
         this.matchers = var1;
      }

      public boolean matches(Channel var1) {
         ChannelMatcher[] var2 = this.matchers;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ChannelMatcher var5 = var2[var4];
            if(!var5.matches(var1)) {
               return false;
            }
         }

         return true;
      }
   }
}
