package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;

public interface NTSecApi extends StdCallLibrary {
   int ForestTrustTopLevelName = 0;
   int ForestTrustTopLevelNameEx = 1;
   int ForestTrustDomainInfo = 2;

   public static class PLSA_FOREST_TRUST_INFORMATION extends Structure {
      public NTSecApi.LSA_FOREST_TRUST_INFORMATION.LSA_FOREST_TRUST_INFORMATION$ByReference fti;

      public PLSA_FOREST_TRUST_INFORMATION() {
      }

      public static class PLSA_FOREST_TRUST_INFORMATION$ByReference extends NTSecApi.PLSA_FOREST_TRUST_INFORMATION implements Structure.ByReference {
         public PLSA_FOREST_TRUST_INFORMATION$ByReference() {
         }
      }
   }

   public static class LSA_FOREST_TRUST_INFORMATION extends Structure {
      public NativeLong RecordCount;
      public NTSecApi.PLSA_FOREST_TRUST_RECORD.PLSA_FOREST_TRUST_RECORD$ByReference Entries;

      public LSA_FOREST_TRUST_INFORMATION() {
      }

      public NTSecApi.PLSA_FOREST_TRUST_RECORD[] getEntries() {
         return (NTSecApi.PLSA_FOREST_TRUST_RECORD[])((NTSecApi.PLSA_FOREST_TRUST_RECORD[])this.Entries.toArray(this.RecordCount.intValue()));
      }

      public static class LSA_FOREST_TRUST_INFORMATION$ByReference extends NTSecApi.LSA_FOREST_TRUST_INFORMATION implements Structure.ByReference {
         public LSA_FOREST_TRUST_INFORMATION$ByReference() {
         }
      }
   }

   public static class PLSA_FOREST_TRUST_RECORD extends Structure {
      public NTSecApi.LSA_FOREST_TRUST_RECORD.LSA_FOREST_TRUST_RECORD$ByReference tr;

      public PLSA_FOREST_TRUST_RECORD() {
      }

      public static class PLSA_FOREST_TRUST_RECORD$ByReference extends NTSecApi.PLSA_FOREST_TRUST_RECORD implements Structure.ByReference {
         public PLSA_FOREST_TRUST_RECORD$ByReference() {
         }
      }
   }

   public static class LSA_FOREST_TRUST_RECORD extends Structure {
      public NativeLong Flags;
      public int ForestTrustType;
      public WinNT.LARGE_INTEGER Time;
      public NTSecApi.LSA_FOREST_TRUST_RECORD.LSA_FOREST_TRUST_RECORD$UNION u;

      public LSA_FOREST_TRUST_RECORD() {
      }

      public void read() {
         super.read();
         switch(this.ForestTrustType) {
         case 0:
         case 1:
            this.u.setType(NTSecApi.LSA_UNICODE_STRING.class);
            break;
         case 2:
            this.u.setType(NTSecApi.LSA_FOREST_TRUST_DOMAIN_INFO.class);
            break;
         default:
            this.u.setType(NTSecApi.LSA_FOREST_TRUST_BINARY_DATA.class);
         }

         this.u.read();
      }

      public static class LSA_FOREST_TRUST_RECORD$UNION extends Union {
         public NTSecApi.LSA_UNICODE_STRING TopLevelName;
         public NTSecApi.LSA_FOREST_TRUST_DOMAIN_INFO DomainInfo;
         public NTSecApi.LSA_FOREST_TRUST_BINARY_DATA Data;

         public LSA_FOREST_TRUST_RECORD$UNION() {
         }

         public static class LSA_FOREST_TRUST_RECORD$UNION$ByReference extends NTSecApi.LSA_FOREST_TRUST_RECORD.LSA_FOREST_TRUST_RECORD$UNION implements Structure.ByReference {
            public LSA_FOREST_TRUST_RECORD$UNION$ByReference() {
            }
         }
      }

      public static class LSA_FOREST_TRUST_RECORD$ByReference extends NTSecApi.LSA_FOREST_TRUST_RECORD implements Structure.ByReference {
         public LSA_FOREST_TRUST_RECORD$ByReference() {
         }
      }
   }

   public static class LSA_FOREST_TRUST_BINARY_DATA extends Structure {
      public NativeLong Length;
      public Pointer Buffer;

      public LSA_FOREST_TRUST_BINARY_DATA() {
      }
   }

   public static class LSA_FOREST_TRUST_DOMAIN_INFO extends Structure {
      public WinNT.PSID.PSID$ByReference Sid;
      public NTSecApi.LSA_UNICODE_STRING DnsName;
      public NTSecApi.LSA_UNICODE_STRING NetbiosName;

      public LSA_FOREST_TRUST_DOMAIN_INFO() {
      }
   }

   public static class PLSA_UNICODE_STRING {
      public NTSecApi.LSA_UNICODE_STRING.LSA_UNICODE_STRING$ByReference s;

      public PLSA_UNICODE_STRING() {
      }

      public static class PLSA_UNICODE_STRING$ByReference extends NTSecApi.PLSA_UNICODE_STRING implements Structure.ByReference {
         public PLSA_UNICODE_STRING$ByReference() {
         }
      }
   }

   public static class LSA_UNICODE_STRING extends Structure {
      public short Length;
      public short MaximumLength;
      public Pointer Buffer;

      public LSA_UNICODE_STRING() {
      }

      public String getString() {
         byte[] var1 = this.Buffer.getByteArray(0L, this.Length);
         if(var1.length >= 2 && var1[var1.length - 1] == 0) {
            return this.Buffer.getString(0L, true);
         } else {
            Memory var2 = new Memory((long)(var1.length + 2));
            var2.write(0L, (byte[])var1, 0, var1.length);
            return var2.getString(0L, true);
         }
      }

      public static class LSA_UNICODE_STRING$ByReference extends NTSecApi.LSA_UNICODE_STRING implements Structure.ByReference {
         public LSA_UNICODE_STRING$ByReference() {
         }
      }
   }
}
