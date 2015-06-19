package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.win32.StdCallLibrary;

public interface Sspi extends StdCallLibrary {
   int MAX_TOKEN_SIZE = 12288;
   int SECPKG_CRED_INBOUND = 1;
   int SECPKG_CRED_OUTBOUND = 2;
   int SECURITY_NATIVE_DREP = 16;
   int ISC_REQ_ALLOCATE_MEMORY = 256;
   int ISC_REQ_CONFIDENTIALITY = 16;
   int ISC_REQ_CONNECTION = 2048;
   int ISC_REQ_DELEGATE = 1;
   int ISC_REQ_EXTENDED_ERROR = 16384;
   int ISC_REQ_INTEGRITY = 65536;
   int ISC_REQ_MUTUAL_AUTH = 2;
   int ISC_REQ_REPLAY_DETECT = 4;
   int ISC_REQ_SEQUENCE_DETECT = 8;
   int ISC_REQ_STREAM = 32768;
   int SECBUFFER_VERSION = 0;
   int SECBUFFER_EMPTY = 0;
   int SECBUFFER_DATA = 1;
   int SECBUFFER_TOKEN = 2;

   public static class SecPkgInfo extends Structure {
      public NativeLong fCapabilities = new NativeLong(0L);
      public short wVersion = 1;
      public short wRPCID = 0;
      public NativeLong cbMaxToken = new NativeLong(0L);
      public WString Name;
      public WString Comment;

      public SecPkgInfo() {
      }

      public static class SecPkgInfo$ByReference extends Sspi.SecPkgInfo implements Structure.ByReference {
         public SecPkgInfo$ByReference() {
         }
      }
   }

   public static class PSecPkgInfo extends Structure {
      public Sspi.SecPkgInfo.SecPkgInfo$ByReference pPkgInfo;

      public PSecPkgInfo() {
      }

      public Sspi.SecPkgInfo.SecPkgInfo$ByReference[] toArray(int var1) {
         return (Sspi.SecPkgInfo.SecPkgInfo$ByReference[])((Sspi.SecPkgInfo.SecPkgInfo$ByReference[])this.pPkgInfo.toArray(var1));
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Structure[] toArray(int var1) {
         return this.toArray(var1);
      }

      public static class PSecPkgInfo$ByReference extends Sspi.PSecPkgInfo implements Structure.ByReference {
         public PSecPkgInfo$ByReference() {
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Structure[] toArray(int var1) {
            return super.toArray(var1);
         }
      }
   }

   public static class TimeStamp extends Sspi.SECURITY_INTEGER {
      public TimeStamp() {
      }
   }

   public static class SECURITY_INTEGER extends Structure {
      public NativeLong dwLower = new NativeLong(0L);
      public NativeLong dwUpper = new NativeLong(0L);

      public SECURITY_INTEGER() {
      }
   }

   public static class SecBufferDesc extends Structure {
      public NativeLong ulVersion = new NativeLong(0L);
      public NativeLong cBuffers = new NativeLong(1L);
      public Sspi.SecBuffer.SecBuffer$ByReference[] pBuffers;

      public SecBufferDesc() {
         Sspi.SecBuffer.SecBuffer$ByReference var1 = new Sspi.SecBuffer.SecBuffer$ByReference();
         this.pBuffers = (Sspi.SecBuffer.SecBuffer$ByReference[])((Sspi.SecBuffer.SecBuffer$ByReference[])var1.toArray(1));
         this.allocateMemory();
      }

      public SecBufferDesc(int var1, byte[] var2) {
         Sspi.SecBuffer.SecBuffer$ByReference var3 = new Sspi.SecBuffer.SecBuffer$ByReference(var1, var2);
         this.pBuffers = (Sspi.SecBuffer.SecBuffer$ByReference[])((Sspi.SecBuffer.SecBuffer$ByReference[])var3.toArray(1));
         this.allocateMemory();
      }

      public SecBufferDesc(int var1, int var2) {
         Sspi.SecBuffer.SecBuffer$ByReference var3 = new Sspi.SecBuffer.SecBuffer$ByReference(var1, var2);
         this.pBuffers = (Sspi.SecBuffer.SecBuffer$ByReference[])((Sspi.SecBuffer.SecBuffer$ByReference[])var3.toArray(1));
         this.allocateMemory();
      }

      public byte[] getBytes() {
         if(this.pBuffers != null && this.cBuffers != null) {
            if(this.cBuffers.intValue() == 1) {
               return this.pBuffers[0].getBytes();
            } else {
               throw new RuntimeException("cBuffers > 1");
            }
         } else {
            throw new RuntimeException("pBuffers | cBuffers");
         }
      }
   }

   public static class SecBuffer extends Structure {
      public NativeLong cbBuffer;
      public NativeLong BufferType;
      public Pointer pvBuffer;

      public SecBuffer() {
         this.cbBuffer = new NativeLong(0L);
         this.pvBuffer = null;
         this.BufferType = new NativeLong(0L);
      }

      public SecBuffer(int var1, int var2) {
         this.cbBuffer = new NativeLong((long)var2);
         this.pvBuffer = new Memory((long)var2);
         this.BufferType = new NativeLong((long)var1);
         this.allocateMemory();
      }

      public SecBuffer(int var1, byte[] var2) {
         this.cbBuffer = new NativeLong((long)var2.length);
         this.pvBuffer = new Memory((long)var2.length);
         this.pvBuffer.write(0L, (byte[])var2, 0, var2.length);
         this.BufferType = new NativeLong((long)var1);
         this.allocateMemory();
      }

      public byte[] getBytes() {
         return this.pvBuffer.getByteArray(0L, this.cbBuffer.intValue());
      }

      public static class SecBuffer$ByReference extends Sspi.SecBuffer implements Structure.ByReference {
         public SecBuffer$ByReference() {
         }

         public SecBuffer$ByReference(int var1, int var2) {
            super(var1, var2);
         }

         public SecBuffer$ByReference(int var1, byte[] var2) {
            super(var1, var2);
         }

         public byte[] getBytes() {
            return super.getBytes();
         }
      }
   }

   public static class CtxtHandle extends Sspi.SecHandle {
      public CtxtHandle() {
      }
   }

   public static class CredHandle extends Sspi.SecHandle {
      public CredHandle() {
      }
   }

   public static class PSecHandle extends Structure {
      public Sspi.SecHandle.SecHandle$ByReference secHandle;

      public PSecHandle() {
      }

      public PSecHandle(Sspi.SecHandle var1) {
         super(var1.getPointer());
         this.read();
      }

      public static class PSecHandle$ByReference extends Sspi.PSecHandle implements Structure.ByReference {
         public PSecHandle$ByReference() {
         }
      }
   }

   public static class SecHandle extends Structure {
      public Pointer dwLower = null;
      public Pointer dwUpper = null;

      public SecHandle() {
      }

      public boolean isNull() {
         return this.dwLower == null && this.dwUpper == null;
      }

      public static class SecHandle$ByReference extends Sspi.SecHandle implements Structure.ByReference {
         public SecHandle$ByReference() {
         }
      }
   }
}
