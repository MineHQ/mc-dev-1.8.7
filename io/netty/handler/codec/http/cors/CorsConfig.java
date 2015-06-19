package io.netty.handler.codec.http.cors;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.internal.StringUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

public final class CorsConfig {
   private final Set<String> origins;
   private final boolean anyOrigin;
   private final boolean enabled;
   private final Set<String> exposeHeaders;
   private final boolean allowCredentials;
   private final long maxAge;
   private final Set<HttpMethod> allowedRequestMethods;
   private final Set<String> allowedRequestHeaders;
   private final boolean allowNullOrigin;
   private final Map<CharSequence, Callable<?>> preflightHeaders;
   private final boolean shortCurcuit;

   private CorsConfig(CorsConfig.Builder var1) {
      this.origins = new LinkedHashSet(var1.origins);
      this.anyOrigin = var1.anyOrigin;
      this.enabled = var1.enabled;
      this.exposeHeaders = var1.exposeHeaders;
      this.allowCredentials = var1.allowCredentials;
      this.maxAge = var1.maxAge;
      this.allowedRequestMethods = var1.requestMethods;
      this.allowedRequestHeaders = var1.requestHeaders;
      this.allowNullOrigin = var1.allowNullOrigin;
      this.preflightHeaders = var1.preflightHeaders;
      this.shortCurcuit = var1.shortCurcuit;
   }

   public boolean isCorsSupportEnabled() {
      return this.enabled;
   }

   public boolean isAnyOriginSupported() {
      return this.anyOrigin;
   }

   public String origin() {
      return this.origins.isEmpty()?"*":(String)this.origins.iterator().next();
   }

   public Set<String> origins() {
      return this.origins;
   }

   public boolean isNullOriginAllowed() {
      return this.allowNullOrigin;
   }

   public Set<String> exposedHeaders() {
      return Collections.unmodifiableSet(this.exposeHeaders);
   }

   public boolean isCredentialsAllowed() {
      return this.allowCredentials;
   }

   public long maxAge() {
      return this.maxAge;
   }

   public Set<HttpMethod> allowedRequestMethods() {
      return Collections.unmodifiableSet(this.allowedRequestMethods);
   }

   public Set<String> allowedRequestHeaders() {
      return Collections.unmodifiableSet(this.allowedRequestHeaders);
   }

   public HttpHeaders preflightResponseHeaders() {
      if(this.preflightHeaders.isEmpty()) {
         return HttpHeaders.EMPTY_HEADERS;
      } else {
         DefaultHttpHeaders var1 = new DefaultHttpHeaders();
         Iterator var2 = this.preflightHeaders.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            Object var4 = getValue((Callable)var3.getValue());
            if(var4 instanceof Iterable) {
               var1.add((CharSequence)var3.getKey(), (Iterable)var4);
            } else {
               var1.add((CharSequence)var3.getKey(), var4);
            }
         }

         return var1;
      }
   }

   public boolean isShortCurcuit() {
      return this.shortCurcuit;
   }

   private static <T> T getValue(Callable<T> var0) {
      try {
         return var0.call();
      } catch (Exception var2) {
         throw new IllegalStateException("Could not generate value for callable [" + var0 + ']', var2);
      }
   }

   public String toString() {
      return StringUtil.simpleClassName((Object)this) + "[enabled=" + this.enabled + ", origins=" + this.origins + ", anyOrigin=" + this.anyOrigin + ", exposedHeaders=" + this.exposeHeaders + ", isCredentialsAllowed=" + this.allowCredentials + ", maxAge=" + this.maxAge + ", allowedRequestMethods=" + this.allowedRequestMethods + ", allowedRequestHeaders=" + this.allowedRequestHeaders + ", preflightHeaders=" + this.preflightHeaders + ']';
   }

   public static CorsConfig.Builder withAnyOrigin() {
      return new CorsConfig.Builder();
   }

   public static CorsConfig.Builder withOrigin(String var0) {
      return "*".equals(var0)?new CorsConfig.Builder():new CorsConfig.Builder(new String[]{var0});
   }

   public static CorsConfig.Builder withOrigins(String... var0) {
      return new CorsConfig.Builder(var0);
   }

   // $FF: synthetic method
   CorsConfig(CorsConfig.Builder var1, CorsConfig.SyntheticClass_1 var2) {
      this(var1);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   public static final class DateValueGenerator implements Callable<Date> {
      public DateValueGenerator() {
      }

      public Date call() throws Exception {
         return new Date();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object call() throws Exception {
         return this.call();
      }
   }

   private static final class ConstantValueGenerator implements Callable<Object> {
      private final Object value;

      private ConstantValueGenerator(Object var1) {
         if(var1 == null) {
            throw new IllegalArgumentException("value must not be null");
         } else {
            this.value = var1;
         }
      }

      public Object call() {
         return this.value;
      }

      // $FF: synthetic method
      ConstantValueGenerator(Object var1, CorsConfig.SyntheticClass_1 var2) {
         this(var1);
      }
   }

   public static class Builder {
      private final Set<String> origins;
      private final boolean anyOrigin;
      private boolean allowNullOrigin;
      private boolean enabled = true;
      private boolean allowCredentials;
      private final Set<String> exposeHeaders = new HashSet();
      private long maxAge;
      private final Set<HttpMethod> requestMethods = new HashSet();
      private final Set<String> requestHeaders = new HashSet();
      private final Map<CharSequence, Callable<?>> preflightHeaders = new HashMap();
      private boolean noPreflightHeaders;
      private boolean shortCurcuit;

      public Builder(String... var1) {
         this.origins = new LinkedHashSet(Arrays.asList(var1));
         this.anyOrigin = false;
      }

      public Builder() {
         this.anyOrigin = true;
         this.origins = Collections.emptySet();
      }

      public CorsConfig.Builder allowNullOrigin() {
         this.allowNullOrigin = true;
         return this;
      }

      public CorsConfig.Builder disable() {
         this.enabled = false;
         return this;
      }

      public CorsConfig.Builder exposeHeaders(String... var1) {
         this.exposeHeaders.addAll(Arrays.asList(var1));
         return this;
      }

      public CorsConfig.Builder allowCredentials() {
         this.allowCredentials = true;
         return this;
      }

      public CorsConfig.Builder maxAge(long var1) {
         this.maxAge = var1;
         return this;
      }

      public CorsConfig.Builder allowedRequestMethods(HttpMethod... var1) {
         this.requestMethods.addAll(Arrays.asList(var1));
         return this;
      }

      public CorsConfig.Builder allowedRequestHeaders(String... var1) {
         this.requestHeaders.addAll(Arrays.asList(var1));
         return this;
      }

      public CorsConfig.Builder preflightResponseHeader(CharSequence var1, Object... var2) {
         if(var2.length == 1) {
            this.preflightHeaders.put(var1, new CorsConfig.ConstantValueGenerator(var2[0]));
         } else {
            this.preflightResponseHeader((CharSequence)var1, (Iterable)Arrays.asList(var2));
         }

         return this;
      }

      public <T> CorsConfig.Builder preflightResponseHeader(CharSequence var1, Iterable<T> var2) {
         this.preflightHeaders.put(var1, new CorsConfig.ConstantValueGenerator(var2));
         return this;
      }

      public <T> CorsConfig.Builder preflightResponseHeader(String var1, Callable<T> var2) {
         this.preflightHeaders.put(var1, var2);
         return this;
      }

      public CorsConfig.Builder noPreflightResponseHeaders() {
         this.noPreflightHeaders = true;
         return this;
      }

      public CorsConfig build() {
         if(this.preflightHeaders.isEmpty() && !this.noPreflightHeaders) {
            this.preflightHeaders.put("Date", new CorsConfig.DateValueGenerator());
            this.preflightHeaders.put("Content-Length", new CorsConfig.ConstantValueGenerator("0"));
         }

         return new CorsConfig(this);
      }

      public CorsConfig.Builder shortCurcuit() {
         this.shortCurcuit = true;
         return this;
      }
   }
}
