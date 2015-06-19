package net.minecraft.server;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.server.ChatClickable;
import net.minecraft.server.ChatHoverable;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.IChatBaseComponent;

public class ChatModifier {
   private ChatModifier a;
   private EnumChatFormat b;
   private Boolean c;
   private Boolean d;
   private Boolean e;
   private Boolean f;
   private Boolean g;
   private ChatClickable h;
   private ChatHoverable i;
   private String j;
   private static final ChatModifier k = new ChatModifier() {
      public EnumChatFormat getColor() {
         return null;
      }

      public boolean isBold() {
         return false;
      }

      public boolean isItalic() {
         return false;
      }

      public boolean isStrikethrough() {
         return false;
      }

      public boolean isUnderlined() {
         return false;
      }

      public boolean isRandom() {
         return false;
      }

      public ChatClickable h() {
         return null;
      }

      public ChatHoverable i() {
         return null;
      }

      public String j() {
         return null;
      }

      public ChatModifier setColor(EnumChatFormat var1) {
         throw new UnsupportedOperationException();
      }

      public ChatModifier setBold(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      public ChatModifier setItalic(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      public ChatModifier setStrikethrough(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      public ChatModifier setUnderline(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      public ChatModifier setRandom(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      public ChatModifier setChatClickable(ChatClickable var1) {
         throw new UnsupportedOperationException();
      }

      public ChatModifier setChatHoverable(ChatHoverable var1) {
         throw new UnsupportedOperationException();
      }

      public ChatModifier setChatModifier(ChatModifier var1) {
         throw new UnsupportedOperationException();
      }

      public String toString() {
         return "Style.ROOT";
      }

      public ChatModifier clone() {
         return this;
      }

      public ChatModifier n() {
         return this;
      }
   };

   public ChatModifier() {
   }

   public EnumChatFormat getColor() {
      return this.b == null?this.o().getColor():this.b;
   }

   public boolean isBold() {
      return this.c == null?this.o().isBold():this.c.booleanValue();
   }

   public boolean isItalic() {
      return this.d == null?this.o().isItalic():this.d.booleanValue();
   }

   public boolean isStrikethrough() {
      return this.f == null?this.o().isStrikethrough():this.f.booleanValue();
   }

   public boolean isUnderlined() {
      return this.e == null?this.o().isUnderlined():this.e.booleanValue();
   }

   public boolean isRandom() {
      return this.g == null?this.o().isRandom():this.g.booleanValue();
   }

   public boolean g() {
      return this.c == null && this.d == null && this.f == null && this.e == null && this.g == null && this.b == null && this.h == null && this.i == null;
   }

   public ChatClickable h() {
      return this.h == null?this.o().h():this.h;
   }

   public ChatHoverable i() {
      return this.i == null?this.o().i():this.i;
   }

   public String j() {
      return this.j == null?this.o().j():this.j;
   }

   public ChatModifier setColor(EnumChatFormat var1) {
      this.b = var1;
      return this;
   }

   public ChatModifier setBold(Boolean var1) {
      this.c = var1;
      return this;
   }

   public ChatModifier setItalic(Boolean var1) {
      this.d = var1;
      return this;
   }

   public ChatModifier setStrikethrough(Boolean var1) {
      this.f = var1;
      return this;
   }

   public ChatModifier setUnderline(Boolean var1) {
      this.e = var1;
      return this;
   }

   public ChatModifier setRandom(Boolean var1) {
      this.g = var1;
      return this;
   }

   public ChatModifier setChatClickable(ChatClickable var1) {
      this.h = var1;
      return this;
   }

   public ChatModifier setChatHoverable(ChatHoverable var1) {
      this.i = var1;
      return this;
   }

   public ChatModifier setInsertion(String var1) {
      this.j = var1;
      return this;
   }

   public ChatModifier setChatModifier(ChatModifier var1) {
      this.a = var1;
      return this;
   }

   private ChatModifier o() {
      return this.a == null?k:this.a;
   }

   public String toString() {
      return "Style{hasParent=" + (this.a != null) + ", color=" + this.b + ", bold=" + this.c + ", italic=" + this.d + ", underlined=" + this.e + ", obfuscated=" + this.g + ", clickEvent=" + this.h() + ", hoverEvent=" + this.i() + ", insertion=" + this.j() + '}';
   }

   public boolean equals(Object var1) {
      if(this == var1) {
         return true;
      } else if(!(var1 instanceof ChatModifier)) {
         return false;
      } else {
         boolean var10000;
         label77: {
            ChatModifier var2 = (ChatModifier)var1;
            if(this.isBold() == var2.isBold() && this.getColor() == var2.getColor() && this.isItalic() == var2.isItalic() && this.isRandom() == var2.isRandom() && this.isStrikethrough() == var2.isStrikethrough() && this.isUnderlined() == var2.isUnderlined()) {
               label71: {
                  if(this.h() != null) {
                     if(!this.h().equals(var2.h())) {
                        break label71;
                     }
                  } else if(var2.h() != null) {
                     break label71;
                  }

                  if(this.i() != null) {
                     if(!this.i().equals(var2.i())) {
                        break label71;
                     }
                  } else if(var2.i() != null) {
                     break label71;
                  }

                  if(this.j() != null) {
                     if(this.j().equals(var2.j())) {
                        break label77;
                     }
                  } else if(var2.j() == null) {
                     break label77;
                  }
               }
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      }
   }

   public int hashCode() {
      int var1 = this.b.hashCode();
      var1 = 31 * var1 + this.c.hashCode();
      var1 = 31 * var1 + this.d.hashCode();
      var1 = 31 * var1 + this.e.hashCode();
      var1 = 31 * var1 + this.f.hashCode();
      var1 = 31 * var1 + this.g.hashCode();
      var1 = 31 * var1 + this.h.hashCode();
      var1 = 31 * var1 + this.i.hashCode();
      var1 = 31 * var1 + this.j.hashCode();
      return var1;
   }

   public ChatModifier clone() {
      ChatModifier var1 = new ChatModifier();
      var1.c = this.c;
      var1.d = this.d;
      var1.f = this.f;
      var1.e = this.e;
      var1.g = this.g;
      var1.b = this.b;
      var1.h = this.h;
      var1.i = this.i;
      var1.a = this.a;
      var1.j = this.j;
      return var1;
   }

   public ChatModifier n() {
      ChatModifier var1 = new ChatModifier();
      var1.setBold(Boolean.valueOf(this.isBold()));
      var1.setItalic(Boolean.valueOf(this.isItalic()));
      var1.setStrikethrough(Boolean.valueOf(this.isStrikethrough()));
      var1.setUnderline(Boolean.valueOf(this.isUnderlined()));
      var1.setRandom(Boolean.valueOf(this.isRandom()));
      var1.setColor(this.getColor());
      var1.setChatClickable(this.h());
      var1.setChatHoverable(this.i());
      var1.setInsertion(this.j());
      return var1;
   }

   public static class ChatModifierSerializer implements JsonDeserializer<ChatModifier>, JsonSerializer<ChatModifier> {
      public ChatModifierSerializer() {
      }

      public ChatModifier a(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         if(var1.isJsonObject()) {
            ChatModifier var4 = new ChatModifier();
            JsonObject var5 = var1.getAsJsonObject();
            if(var5 == null) {
               return null;
            } else {
               if(var5.has("bold")) {
                  var4.c = Boolean.valueOf(var5.get("bold").getAsBoolean());
               }

               if(var5.has("italic")) {
                  var4.d = Boolean.valueOf(var5.get("italic").getAsBoolean());
               }

               if(var5.has("underlined")) {
                  var4.e = Boolean.valueOf(var5.get("underlined").getAsBoolean());
               }

               if(var5.has("strikethrough")) {
                  var4.f = Boolean.valueOf(var5.get("strikethrough").getAsBoolean());
               }

               if(var5.has("obfuscated")) {
                  var4.g = Boolean.valueOf(var5.get("obfuscated").getAsBoolean());
               }

               if(var5.has("color")) {
                  var4.b = (EnumChatFormat)var3.deserialize(var5.get("color"), EnumChatFormat.class);
               }

               if(var5.has("insertion")) {
                  var4.j = var5.get("insertion").getAsString();
               }

               JsonObject var6;
               JsonPrimitive var7;
               if(var5.has("clickEvent")) {
                  var6 = var5.getAsJsonObject("clickEvent");
                  if(var6 != null) {
                     var7 = var6.getAsJsonPrimitive("action");
                     ChatClickable.EnumClickAction var8 = var7 == null?null:ChatClickable.EnumClickAction.a(var7.getAsString());
                     JsonPrimitive var9 = var6.getAsJsonPrimitive("value");
                     String var10 = var9 == null?null:var9.getAsString();
                     if(var8 != null && var10 != null && var8.a()) {
                        var4.h = new ChatClickable(var8, var10);
                     }
                  }
               }

               if(var5.has("hoverEvent")) {
                  var6 = var5.getAsJsonObject("hoverEvent");
                  if(var6 != null) {
                     var7 = var6.getAsJsonPrimitive("action");
                     ChatHoverable.EnumHoverAction var11 = var7 == null?null:ChatHoverable.EnumHoverAction.a(var7.getAsString());
                     IChatBaseComponent var12 = (IChatBaseComponent)var3.deserialize(var6.get("value"), IChatBaseComponent.class);
                     if(var11 != null && var12 != null && var11.a()) {
                        var4.i = new ChatHoverable(var11, var12);
                     }
                  }
               }

               return var4;
            }
         } else {
            return null;
         }
      }

      public JsonElement a(ChatModifier var1, Type var2, JsonSerializationContext var3) {
         if(var1.g()) {
            return null;
         } else {
            JsonObject var4 = new JsonObject();
            if(var1.c != null) {
               var4.addProperty("bold", var1.c);
            }

            if(var1.d != null) {
               var4.addProperty("italic", var1.d);
            }

            if(var1.e != null) {
               var4.addProperty("underlined", var1.e);
            }

            if(var1.f != null) {
               var4.addProperty("strikethrough", var1.f);
            }

            if(var1.g != null) {
               var4.addProperty("obfuscated", var1.g);
            }

            if(var1.b != null) {
               var4.add("color", var3.serialize(var1.b));
            }

            if(var1.j != null) {
               var4.add("insertion", var3.serialize(var1.j));
            }

            JsonObject var5;
            if(var1.h != null) {
               var5 = new JsonObject();
               var5.addProperty("action", var1.h.a().b());
               var5.addProperty("value", var1.h.b());
               var4.add("clickEvent", var5);
            }

            if(var1.i != null) {
               var5 = new JsonObject();
               var5.addProperty("action", var1.i.a().b());
               var5.add("value", var3.serialize(var1.i.b()));
               var4.add("hoverEvent", var5);
            }

            return var4;
         }
      }

      // $FF: synthetic method
      public JsonElement serialize(Object var1, Type var2, JsonSerializationContext var3) {
         return this.a((ChatModifier)var1, var2, var3);
      }

      // $FF: synthetic method
      public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return this.a(var1, var2, var3);
      }
   }
}
