package org.apache.commons.codec.digest;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.codec.digest.Sha2Crypt;
import org.apache.commons.codec.digest.UnixCrypt;

public class Crypt {
   public Crypt() {
   }

   public static String crypt(byte[] var0) {
      return crypt((byte[])var0, (String)null);
   }

   public static String crypt(byte[] var0, String var1) {
      return var1 == null?Sha2Crypt.sha512Crypt(var0):(var1.startsWith("$6$")?Sha2Crypt.sha512Crypt(var0, var1):(var1.startsWith("$5$")?Sha2Crypt.sha256Crypt(var0, var1):(var1.startsWith("$1$")?Md5Crypt.md5Crypt(var0, var1):UnixCrypt.crypt(var0, var1))));
   }

   public static String crypt(String var0) {
      return crypt((String)var0, (String)null);
   }

   public static String crypt(String var0, String var1) {
      return crypt(var0.getBytes(Charsets.UTF_8), var1);
   }
}
