package com.sun.jna.platform.win32;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;

public interface DsGetDC extends StdCallLibrary {
   int DS_DOMAIN_IN_FOREST = 1;
   int DS_DOMAIN_DIRECT_OUTBOUND = 2;
   int DS_DOMAIN_TREE_ROOT = 4;
   int DS_DOMAIN_PRIMARY = 8;
   int DS_DOMAIN_NATIVE_MODE = 16;
   int DS_DOMAIN_DIRECT_INBOUND = 32;
   int DS_DOMAIN_VALID_FLAGS = 63;

   public static class PDS_DOMAIN_TRUSTS extends Structure {
      public DsGetDC.DS_DOMAIN_TRUSTS.DS_DOMAIN_TRUSTS$ByReference t;

      public PDS_DOMAIN_TRUSTS() {
      }

      public DsGetDC.DS_DOMAIN_TRUSTS[] getTrusts(int var1) {
         return (DsGetDC.DS_DOMAIN_TRUSTS[])((DsGetDC.DS_DOMAIN_TRUSTS[])this.t.toArray(var1));
      }

      public static class PDS_DOMAIN_TRUSTS$ByReference extends DsGetDC.PDS_DOMAIN_TRUSTS implements Structure.ByReference {
         public PDS_DOMAIN_TRUSTS$ByReference() {
         }
      }
   }

   public static class DS_DOMAIN_TRUSTS extends Structure {
      public WString NetbiosDomainName;
      public WString DnsDomainName;
      public NativeLong Flags;
      public NativeLong ParentIndex;
      public NativeLong TrustType;
      public NativeLong TrustAttributes;
      public WinNT.PSID.PSID$ByReference DomainSid;
      public Guid.GUID DomainGuid;

      public DS_DOMAIN_TRUSTS() {
      }

      public static class DS_DOMAIN_TRUSTS$ByReference extends DsGetDC.DS_DOMAIN_TRUSTS implements Structure.ByReference {
         public DS_DOMAIN_TRUSTS$ByReference() {
         }
      }
   }

   public static class PDOMAIN_CONTROLLER_INFO extends Structure {
      public DsGetDC.DOMAIN_CONTROLLER_INFO.DOMAIN_CONTROLLER_INFO$ByReference dci;

      public PDOMAIN_CONTROLLER_INFO() {
      }

      public static class PDOMAIN_CONTROLLER_INFO$ByReference extends DsGetDC.PDOMAIN_CONTROLLER_INFO implements Structure.ByReference {
         public PDOMAIN_CONTROLLER_INFO$ByReference() {
         }
      }
   }

   public static class DOMAIN_CONTROLLER_INFO extends Structure {
      public WString DomainControllerName;
      public WString DomainControllerAddress;
      public int DomainControllerAddressType;
      public Guid.GUID DomainGuid;
      public WString DomainName;
      public WString DnsForestName;
      public int Flags;
      public WString DcSiteName;
      public WString ClientSiteName;

      public DOMAIN_CONTROLLER_INFO() {
      }

      public DOMAIN_CONTROLLER_INFO(Pointer var1) {
         super(var1);
         this.read();
      }

      public static class DOMAIN_CONTROLLER_INFO$ByReference extends DsGetDC.DOMAIN_CONTROLLER_INFO implements Structure.ByReference {
         public DOMAIN_CONTROLLER_INFO$ByReference() {
         }
      }
   }
}
