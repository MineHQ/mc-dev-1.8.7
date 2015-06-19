package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface Guid {
   public static class GUID extends Structure {
      public int Data1;
      public short Data2;
      public short Data3;
      public byte[] Data4 = new byte[8];

      public GUID() {
      }

      public GUID(Pointer var1) {
         super(var1);
         this.read();
      }

      public GUID(byte[] var1) {
         if(var1.length != 16) {
            throw new IllegalArgumentException("Invalid data length: " + var1.length);
         } else {
            long var2 = (long)(var1[3] & 255);
            var2 <<= 8;
            var2 |= (long)(var1[2] & 255);
            var2 <<= 8;
            var2 |= (long)(var1[1] & 255);
            var2 <<= 8;
            var2 |= (long)(var1[0] & 255);
            this.Data1 = (int)var2;
            int var4 = var1[5] & 255;
            var4 <<= 8;
            var4 |= var1[4] & 255;
            this.Data2 = (short)var4;
            int var5 = var1[7] & 255;
            var5 <<= 8;
            var5 |= var1[6] & 255;
            this.Data3 = (short)var5;
            this.Data4[0] = var1[8];
            this.Data4[1] = var1[9];
            this.Data4[2] = var1[10];
            this.Data4[3] = var1[11];
            this.Data4[4] = var1[12];
            this.Data4[5] = var1[13];
            this.Data4[6] = var1[14];
            this.Data4[7] = var1[15];
         }
      }

      public static class GUID$ByReference extends Guid.GUID implements Structure.ByReference {
         public GUID$ByReference() {
         }

         public GUID$ByReference(Guid.GUID var1) {
            super(var1.getPointer());
            this.Data1 = var1.Data1;
            this.Data2 = var1.Data2;
            this.Data3 = var1.Data3;
            this.Data4 = var1.Data4;
         }

         public GUID$ByReference(Pointer var1) {
            super(var1);
         }
      }
   }
}
