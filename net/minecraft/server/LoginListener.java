package net.minecraft.server;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.SecretKey;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.MinecraftEncryption;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.PacketLoginInEncryptionBegin;
import net.minecraft.server.PacketLoginInListener;
import net.minecraft.server.PacketLoginInStart;
import net.minecraft.server.PacketLoginOutDisconnect;
import net.minecraft.server.PacketLoginOutEncryptionBegin;
import net.minecraft.server.PacketLoginOutSetCompression;
import net.minecraft.server.PacketLoginOutSuccess;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginListener implements PacketLoginInListener, IUpdatePlayerListBox {
   private static final AtomicInteger b = new AtomicInteger(0);
   private static final Logger c = LogManager.getLogger();
   private static final Random random = new Random();
   private final byte[] e = new byte[4];
   private final MinecraftServer server;
   public final NetworkManager networkManager;
   private LoginListener.EnumProtocolState g;
   private int h;
   private GameProfile i;
   private String j;
   private SecretKey loginKey;
   private EntityPlayer l;

   public LoginListener(MinecraftServer var1, NetworkManager var2) {
      this.g = LoginListener.EnumProtocolState.HELLO;
      this.j = "";
      this.server = var1;
      this.networkManager = var2;
      random.nextBytes(this.e);
   }

   public void c() {
      if(this.g == LoginListener.EnumProtocolState.READY_TO_ACCEPT) {
         this.b();
      } else if(this.g == LoginListener.EnumProtocolState.e) {
         EntityPlayer var1 = this.server.getPlayerList().a(this.i.getId());
         if(var1 == null) {
            this.g = LoginListener.EnumProtocolState.READY_TO_ACCEPT;
            this.server.getPlayerList().a(this.networkManager, this.l);
            this.l = null;
         }
      }

      if(this.h++ == 600) {
         this.disconnect("Took too long to log in");
      }

   }

   public void disconnect(String var1) {
      try {
         c.info("Disconnecting " + this.d() + ": " + var1);
         ChatComponentText var2 = new ChatComponentText(var1);
         this.networkManager.handle(new PacketLoginOutDisconnect(var2));
         this.networkManager.close(var2);
      } catch (Exception var3) {
         c.error((String)"Error whilst disconnecting player", (Throwable)var3);
      }

   }

   public void b() {
      if(!this.i.isComplete()) {
         this.i = this.a(this.i);
      }

      String var1 = this.server.getPlayerList().attemptLogin(this.networkManager.getSocketAddress(), this.i);
      if(var1 != null) {
         this.disconnect(var1);
      } else {
         this.g = LoginListener.EnumProtocolState.ACCEPTED;
         if(this.server.aK() >= 0 && !this.networkManager.c()) {
            this.networkManager.a(new PacketLoginOutSetCompression(this.server.aK()), new ChannelFutureListener() {
               public void a(ChannelFuture var1) throws Exception {
                  LoginListener.this.networkManager.a(LoginListener.this.server.aK());
               }

               // $FF: synthetic method
               public void operationComplete(Future var1) throws Exception {
                  this.a((ChannelFuture)var1);
               }
            }, new GenericFutureListener[0]);
         }

         this.networkManager.handle(new PacketLoginOutSuccess(this.i));
         EntityPlayer var2 = this.server.getPlayerList().a(this.i.getId());
         if(var2 != null) {
            this.g = LoginListener.EnumProtocolState.e;
            this.l = this.server.getPlayerList().processLogin(this.i);
         } else {
            this.server.getPlayerList().a(this.networkManager, this.server.getPlayerList().processLogin(this.i));
         }
      }

   }

   public void a(IChatBaseComponent var1) {
      c.info(this.d() + " lost connection: " + var1.c());
   }

   public String d() {
      return this.i != null?this.i.toString() + " (" + this.networkManager.getSocketAddress().toString() + ")":String.valueOf(this.networkManager.getSocketAddress());
   }

   public void a(PacketLoginInStart var1) {
      Validate.validState(this.g == LoginListener.EnumProtocolState.HELLO, "Unexpected hello packet", new Object[0]);
      this.i = var1.a();
      if(this.server.getOnlineMode() && !this.networkManager.c()) {
         this.g = LoginListener.EnumProtocolState.KEY;
         this.networkManager.handle(new PacketLoginOutEncryptionBegin(this.j, this.server.Q().getPublic(), this.e));
      } else {
         this.g = LoginListener.EnumProtocolState.READY_TO_ACCEPT;
      }

   }

   public void a(PacketLoginInEncryptionBegin var1) {
      Validate.validState(this.g == LoginListener.EnumProtocolState.KEY, "Unexpected key packet", new Object[0]);
      PrivateKey var2 = this.server.Q().getPrivate();
      if(!Arrays.equals(this.e, var1.b(var2))) {
         throw new IllegalStateException("Invalid nonce!");
      } else {
         this.loginKey = var1.a(var2);
         this.g = LoginListener.EnumProtocolState.AUTHENTICATING;
         this.networkManager.a(this.loginKey);
         (new Thread("User Authenticator #" + b.incrementAndGet()) {
            public void run() {
               GameProfile var1 = LoginListener.this.i;

               try {
                  String var2 = (new BigInteger(MinecraftEncryption.a(LoginListener.this.j, LoginListener.this.server.Q().getPublic(), LoginListener.this.loginKey))).toString(16);
                  LoginListener.this.i = LoginListener.this.server.aD().hasJoinedServer(new GameProfile((UUID)null, var1.getName()), var2);
                  if(LoginListener.this.i != null) {
                     LoginListener.c.info("UUID of player " + LoginListener.this.i.getName() + " is " + LoginListener.this.i.getId());
                     LoginListener.this.g = LoginListener.EnumProtocolState.READY_TO_ACCEPT;
                  } else if(LoginListener.this.server.T()) {
                     LoginListener.c.warn("Failed to verify username but will let them in anyway!");
                     LoginListener.this.i = LoginListener.this.a(var1);
                     LoginListener.this.g = LoginListener.EnumProtocolState.READY_TO_ACCEPT;
                  } else {
                     LoginListener.this.disconnect("Failed to verify username!");
                     LoginListener.c.error("Username \'" + LoginListener.this.i.getName() + "\' tried to join with an invalid session");
                  }
               } catch (AuthenticationUnavailableException var3) {
                  if(LoginListener.this.server.T()) {
                     LoginListener.c.warn("Authentication servers are down but will let them in anyway!");
                     LoginListener.this.i = LoginListener.this.a(var1);
                     LoginListener.this.g = LoginListener.EnumProtocolState.READY_TO_ACCEPT;
                  } else {
                     LoginListener.this.disconnect("Authentication servers are down. Please try again later, sorry!");
                     LoginListener.c.error("Couldn\'t verify username because servers are unavailable");
                  }
               }

            }
         }).start();
      }
   }

   protected GameProfile a(GameProfile var1) {
      UUID var2 = UUID.nameUUIDFromBytes(("OfflinePlayer:" + var1.getName()).getBytes(Charsets.UTF_8));
      return new GameProfile(var2, var1.getName());
   }

   static enum EnumProtocolState {
      HELLO,
      KEY,
      AUTHENTICATING,
      READY_TO_ACCEPT,
      e,
      ACCEPTED;

      private EnumProtocolState() {
      }
   }
}
