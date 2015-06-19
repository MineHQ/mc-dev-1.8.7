package net.minecraft.server;

import com.google.common.collect.Maps;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import net.minecraft.server.HttpUtilities;
import net.minecraft.server.IMojangStatistics;

public class MojangStatisticsGenerator {
   private final Map<String, Object> a = Maps.newHashMap();
   private final Map<String, Object> b = Maps.newHashMap();
   private final String c = UUID.randomUUID().toString();
   private final URL d;
   private final IMojangStatistics e;
   private final Timer f = new Timer("Snooper Timer", true);
   private final Object g = new Object();
   private final long h;
   private boolean i;
   private int j;

   public MojangStatisticsGenerator(String var1, IMojangStatistics var2, long var3) {
      try {
         this.d = new URL("http://snoop.minecraft.net/" + var1 + "?version=" + 2);
      } catch (MalformedURLException var6) {
         throw new IllegalArgumentException();
      }

      this.e = var2;
      this.h = var3;
   }

   public void a() {
      if(!this.i) {
         this.i = true;
         this.h();
         this.f.schedule(new TimerTask() {
            public void run() {
               if(MojangStatisticsGenerator.this.e.getSnooperEnabled()) {
                  HashMap var1;
                  synchronized(MojangStatisticsGenerator.this.g) {
                     var1 = Maps.newHashMap(MojangStatisticsGenerator.this.b);
                     if(MojangStatisticsGenerator.this.j == 0) {
                        var1.putAll(MojangStatisticsGenerator.this.a);
                     }

                     var1.put("snooper_count", Integer.valueOf(MojangStatisticsGenerator.f(MojangStatisticsGenerator.this)));
                     var1.put("snooper_token", MojangStatisticsGenerator.this.c);
                  }

                  HttpUtilities.a(MojangStatisticsGenerator.this.d, (Map)var1, true);
               }
            }
         }, 0L, 900000L);
      }
   }

   private void h() {
      this.i();
      this.a("snooper_token", this.c);
      this.b("snooper_token", this.c);
      this.b("os_name", System.getProperty("os.name"));
      this.b("os_version", System.getProperty("os.version"));
      this.b("os_architecture", System.getProperty("os.arch"));
      this.b("java_version", System.getProperty("java.version"));
      this.a("version", "1.8.7");
      this.e.b(this);
   }

   private void i() {
      RuntimeMXBean var1 = ManagementFactory.getRuntimeMXBean();
      List var2 = var1.getInputArguments();
      int var3 = 0;
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if(var5.startsWith("-X")) {
            this.a("jvm_arg[" + var3++ + "]", var5);
         }
      }

      this.a("jvm_args", Integer.valueOf(var3));
   }

   public void b() {
      this.b("memory_total", Long.valueOf(Runtime.getRuntime().totalMemory()));
      this.b("memory_max", Long.valueOf(Runtime.getRuntime().maxMemory()));
      this.b("memory_free", Long.valueOf(Runtime.getRuntime().freeMemory()));
      this.b("cpu_cores", Integer.valueOf(Runtime.getRuntime().availableProcessors()));
      this.e.a(this);
   }

   public void a(String var1, Object var2) {
      Object var3 = this.g;
      synchronized(this.g) {
         this.b.put(var1, var2);
      }
   }

   public void b(String var1, Object var2) {
      Object var3 = this.g;
      synchronized(this.g) {
         this.a.put(var1, var2);
      }
   }

   public boolean d() {
      return this.i;
   }

   public void e() {
      this.f.cancel();
   }

   public long g() {
      return this.h;
   }

   // $FF: synthetic method
   static int f(MojangStatisticsGenerator var0) {
      return var0.j++;
   }
}
