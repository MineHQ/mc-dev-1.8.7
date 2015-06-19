package net.minecraft.server;

import net.minecraft.server.ChatMessage;

public class ChatMessageException extends IllegalArgumentException {
   public ChatMessageException(ChatMessage var1, String var2) {
      super(String.format("Error parsing: %s: %s", new Object[]{var1, var2}));
   }

   public ChatMessageException(ChatMessage var1, int var2) {
      super(String.format("Invalid index %d requested for %s", new Object[]{Integer.valueOf(var2), var1}));
   }

   public ChatMessageException(ChatMessage var1, Throwable var2) {
      super(String.format("Error while parsing: %s", new Object[]{var1}), var2);
   }
}
