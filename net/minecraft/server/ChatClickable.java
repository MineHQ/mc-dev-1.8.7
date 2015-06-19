package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Map;

public class ChatClickable {
   private final ChatClickable.EnumClickAction a;
   private final String b;

   public ChatClickable(ChatClickable.EnumClickAction var1, String var2) {
      this.a = var1;
      this.b = var2;
   }

   public ChatClickable.EnumClickAction a() {
      return this.a;
   }

   public String b() {
      return this.b;
   }

   public boolean equals(Object var1) {
      if(this == var1) {
         return true;
      } else if(var1 != null && this.getClass() == var1.getClass()) {
         ChatClickable var2 = (ChatClickable)var1;
         if(this.a != var2.a) {
            return false;
         } else {
            if(this.b != null) {
               if(!this.b.equals(var2.b)) {
                  return false;
               }
            } else if(var2.b != null) {
               return false;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public String toString() {
      return "ClickEvent{action=" + this.a + ", value=\'" + this.b + '\'' + '}';
   }

   public int hashCode() {
      int var1 = this.a.hashCode();
      var1 = 31 * var1 + (this.b != null?this.b.hashCode():0);
      return var1;
   }

   public static enum EnumClickAction {
      OPEN_URL("open_url", true),
      OPEN_FILE("open_file", false),
      RUN_COMMAND("run_command", true),
      TWITCH_USER_INFO("twitch_user_info", false),
      SUGGEST_COMMAND("suggest_command", true),
      CHANGE_PAGE("change_page", true);

      private static final Map<String, ChatClickable.EnumClickAction> g;
      private final boolean h;
      private final String i;

      private EnumClickAction(String var3, boolean var4) {
         this.i = var3;
         this.h = var4;
      }

      public boolean a() {
         return this.h;
      }

      public String b() {
         return this.i;
      }

      public static ChatClickable.EnumClickAction a(String var0) {
         return (ChatClickable.EnumClickAction)g.get(var0);
      }

      static {
         g = Maps.newHashMap();
         ChatClickable.EnumClickAction[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            ChatClickable.EnumClickAction var3 = var0[var2];
            g.put(var3.b(), var3);
         }

      }
   }
}
