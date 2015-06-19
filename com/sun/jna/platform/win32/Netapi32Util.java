package com.sun.jna.platform.win32;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.DsGetDC;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.LMAccess;
import com.sun.jna.platform.win32.Netapi32;
import com.sun.jna.platform.win32.Ole32Util;
import com.sun.jna.platform.win32.Secur32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;

public abstract class Netapi32Util {
   public Netapi32Util() {
   }

   public static String getDCName() {
      return getDCName((String)null, (String)null);
   }

   public static String getDCName(String var0, String var1) {
      PointerByReference var2 = new PointerByReference();

      String var4;
      try {
         int var3 = Netapi32.INSTANCE.NetGetDCName(var1, var0, var2);
         if(0 != var3) {
            throw new Win32Exception(var3);
         }

         var4 = var2.getValue().getString(0L, true);
      } finally {
         if(0 != Netapi32.INSTANCE.NetApiBufferFree(var2.getValue())) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         }

      }

      return var4;
   }

   public static int getJoinStatus() {
      return getJoinStatus((String)null);
   }

   public static int getJoinStatus(String var0) {
      PointerByReference var1 = new PointerByReference();
      IntByReference var2 = new IntByReference();
      boolean var9 = false;

      int var4;
      try {
         var9 = true;
         int var3 = Netapi32.INSTANCE.NetGetJoinInformation(var0, var1, var2);
         if(0 != var3) {
            throw new Win32Exception(var3);
         }

         var4 = var2.getValue();
         var9 = false;
      } finally {
         if(var9) {
            if(var1.getPointer() != null) {
               int var7 = Netapi32.INSTANCE.NetApiBufferFree(var1.getValue());
               if(0 != var7) {
                  throw new Win32Exception(var7);
               }
            }

         }
      }

      if(var1.getPointer() != null) {
         int var5 = Netapi32.INSTANCE.NetApiBufferFree(var1.getValue());
         if(0 != var5) {
            throw new Win32Exception(var5);
         }
      }

      return var4;
   }

   public static String getDomainName(String var0) {
      PointerByReference var1 = new PointerByReference();
      IntByReference var2 = new IntByReference();
      boolean var9 = false;

      String var4;
      try {
         var9 = true;
         int var3 = Netapi32.INSTANCE.NetGetJoinInformation(var0, var1, var2);
         if(0 != var3) {
            throw new Win32Exception(var3);
         }

         var4 = var1.getValue().getString(0L, true);
         var9 = false;
      } finally {
         if(var9) {
            if(var1.getPointer() != null) {
               int var7 = Netapi32.INSTANCE.NetApiBufferFree(var1.getValue());
               if(0 != var7) {
                  throw new Win32Exception(var7);
               }
            }

         }
      }

      if(var1.getPointer() != null) {
         int var5 = Netapi32.INSTANCE.NetApiBufferFree(var1.getValue());
         if(0 != var5) {
            throw new Win32Exception(var5);
         }
      }

      return var4;
   }

   public static Netapi32Util.LocalGroup[] getLocalGroups() {
      return getLocalGroups((String)null);
   }

   public static Netapi32Util.LocalGroup[] getLocalGroups(String var0) {
      PointerByReference var1 = new PointerByReference();
      IntByReference var2 = new IntByReference();
      IntByReference var3 = new IntByReference();
      boolean var16 = false;

      int var9;
      Netapi32Util.LocalGroup[] var18;
      try {
         var16 = true;
         int var4 = Netapi32.INSTANCE.NetLocalGroupEnum(var0, 1, var1, -1, var2, var3, (IntByReference)null);
         if(0 != var4 || var1.getValue() == Pointer.NULL) {
            throw new Win32Exception(var4);
         }

         LMAccess.LOCALGROUP_INFO_1 var5 = new LMAccess.LOCALGROUP_INFO_1(var1.getValue());
         LMAccess.LOCALGROUP_INFO_1[] var6 = (LMAccess.LOCALGROUP_INFO_1[])((LMAccess.LOCALGROUP_INFO_1[])var5.toArray(var2.getValue()));
         ArrayList var7 = new ArrayList();
         LMAccess.LOCALGROUP_INFO_1[] var8 = var6;
         var9 = var6.length;
         int var10 = 0;

         while(true) {
            if(var10 >= var9) {
               var18 = (Netapi32Util.LocalGroup[])var7.toArray(new Netapi32Util.LocalGroup[0]);
               var16 = false;
               break;
            }

            LMAccess.LOCALGROUP_INFO_1 var11 = var8[var10];
            Netapi32Util.LocalGroup var12 = new Netapi32Util.LocalGroup();
            var12.name = var11.lgrui1_name.toString();
            var12.comment = var11.lgrui1_comment.toString();
            var7.add(var12);
            ++var10;
         }
      } finally {
         if(var16) {
            if(var1.getValue() != Pointer.NULL) {
               int var14 = Netapi32.INSTANCE.NetApiBufferFree(var1.getValue());
               if(0 != var14) {
                  throw new Win32Exception(var14);
               }
            }

         }
      }

      if(var1.getValue() != Pointer.NULL) {
         var9 = Netapi32.INSTANCE.NetApiBufferFree(var1.getValue());
         if(0 != var9) {
            throw new Win32Exception(var9);
         }
      }

      return var18;
   }

   public static Netapi32Util.Group[] getGlobalGroups() {
      return getGlobalGroups((String)null);
   }

   public static Netapi32Util.Group[] getGlobalGroups(String var0) {
      PointerByReference var1 = new PointerByReference();
      IntByReference var2 = new IntByReference();
      IntByReference var3 = new IntByReference();
      boolean var16 = false;

      int var9;
      Netapi32Util.Group[] var18;
      try {
         var16 = true;
         int var4 = Netapi32.INSTANCE.NetGroupEnum(var0, 1, var1, -1, var2, var3, (IntByReference)null);
         if(0 != var4 || var1.getValue() == Pointer.NULL) {
            throw new Win32Exception(var4);
         }

         LMAccess.GROUP_INFO_1 var5 = new LMAccess.GROUP_INFO_1(var1.getValue());
         LMAccess.GROUP_INFO_1[] var6 = (LMAccess.GROUP_INFO_1[])((LMAccess.GROUP_INFO_1[])var5.toArray(var2.getValue()));
         ArrayList var7 = new ArrayList();
         LMAccess.GROUP_INFO_1[] var8 = var6;
         var9 = var6.length;
         int var10 = 0;

         while(true) {
            if(var10 >= var9) {
               var18 = (Netapi32Util.Group[])var7.toArray(new Netapi32Util.LocalGroup[0]);
               var16 = false;
               break;
            }

            LMAccess.GROUP_INFO_1 var11 = var8[var10];
            Netapi32Util.LocalGroup var12 = new Netapi32Util.LocalGroup();
            var12.name = var11.grpi1_name.toString();
            var12.comment = var11.grpi1_comment.toString();
            var7.add(var12);
            ++var10;
         }
      } finally {
         if(var16) {
            if(var1.getValue() != Pointer.NULL) {
               int var14 = Netapi32.INSTANCE.NetApiBufferFree(var1.getValue());
               if(0 != var14) {
                  throw new Win32Exception(var14);
               }
            }

         }
      }

      if(var1.getValue() != Pointer.NULL) {
         var9 = Netapi32.INSTANCE.NetApiBufferFree(var1.getValue());
         if(0 != var9) {
            throw new Win32Exception(var9);
         }
      }

      return var18;
   }

   public static Netapi32Util.User[] getUsers() {
      return getUsers((String)null);
   }

   public static Netapi32Util.User[] getUsers(String var0) {
      PointerByReference var1 = new PointerByReference();
      IntByReference var2 = new IntByReference();
      IntByReference var3 = new IntByReference();
      boolean var16 = false;

      int var9;
      Netapi32Util.User[] var18;
      try {
         var16 = true;
         int var4 = Netapi32.INSTANCE.NetUserEnum(var0, 1, 0, var1, -1, var2, var3, (IntByReference)null);
         if(0 != var4 || var1.getValue() == Pointer.NULL) {
            throw new Win32Exception(var4);
         }

         LMAccess.USER_INFO_1 var5 = new LMAccess.USER_INFO_1(var1.getValue());
         LMAccess.USER_INFO_1[] var6 = (LMAccess.USER_INFO_1[])((LMAccess.USER_INFO_1[])var5.toArray(var2.getValue()));
         ArrayList var7 = new ArrayList();
         LMAccess.USER_INFO_1[] var8 = var6;
         var9 = var6.length;
         int var10 = 0;

         while(true) {
            if(var10 >= var9) {
               var18 = (Netapi32Util.User[])var7.toArray(new Netapi32Util.User[0]);
               var16 = false;
               break;
            }

            LMAccess.USER_INFO_1 var11 = var8[var10];
            Netapi32Util.User var12 = new Netapi32Util.User();
            var12.name = var11.usri1_name.toString();
            var7.add(var12);
            ++var10;
         }
      } finally {
         if(var16) {
            if(var1.getValue() != Pointer.NULL) {
               int var14 = Netapi32.INSTANCE.NetApiBufferFree(var1.getValue());
               if(0 != var14) {
                  throw new Win32Exception(var14);
               }
            }

         }
      }

      if(var1.getValue() != Pointer.NULL) {
         var9 = Netapi32.INSTANCE.NetApiBufferFree(var1.getValue());
         if(0 != var9) {
            throw new Win32Exception(var9);
         }
      }

      return var18;
   }

   public static Netapi32Util.Group[] getCurrentUserLocalGroups() {
      return getUserLocalGroups(Secur32Util.getUserNameEx(2));
   }

   public static Netapi32Util.Group[] getUserLocalGroups(String var0) {
      return getUserLocalGroups(var0, (String)null);
   }

   public static Netapi32Util.Group[] getUserLocalGroups(String var0, String var1) {
      PointerByReference var2 = new PointerByReference();
      IntByReference var3 = new IntByReference();
      IntByReference var4 = new IntByReference();
      boolean var17 = false;

      int var10;
      Netapi32Util.Group[] var19;
      try {
         var17 = true;
         int var5 = Netapi32.INSTANCE.NetUserGetLocalGroups(var1, var0, 0, 0, var2, -1, var3, var4);
         if(var5 != 0) {
            throw new Win32Exception(var5);
         }

         LMAccess.LOCALGROUP_USERS_INFO_0 var6 = new LMAccess.LOCALGROUP_USERS_INFO_0(var2.getValue());
         LMAccess.LOCALGROUP_USERS_INFO_0[] var7 = (LMAccess.LOCALGROUP_USERS_INFO_0[])((LMAccess.LOCALGROUP_USERS_INFO_0[])var6.toArray(var3.getValue()));
         ArrayList var8 = new ArrayList();
         LMAccess.LOCALGROUP_USERS_INFO_0[] var9 = var7;
         var10 = var7.length;
         int var11 = 0;

         while(true) {
            if(var11 >= var10) {
               var19 = (Netapi32Util.Group[])var8.toArray(new Netapi32Util.Group[0]);
               var17 = false;
               break;
            }

            LMAccess.LOCALGROUP_USERS_INFO_0 var12 = var9[var11];
            Netapi32Util.LocalGroup var13 = new Netapi32Util.LocalGroup();
            var13.name = var12.lgrui0_name.toString();
            var8.add(var13);
            ++var11;
         }
      } finally {
         if(var17) {
            if(var2.getValue() != Pointer.NULL) {
               int var15 = Netapi32.INSTANCE.NetApiBufferFree(var2.getValue());
               if(0 != var15) {
                  throw new Win32Exception(var15);
               }
            }

         }
      }

      if(var2.getValue() != Pointer.NULL) {
         var10 = Netapi32.INSTANCE.NetApiBufferFree(var2.getValue());
         if(0 != var10) {
            throw new Win32Exception(var10);
         }
      }

      return var19;
   }

   public static Netapi32Util.Group[] getUserGroups(String var0) {
      return getUserGroups(var0, (String)null);
   }

   public static Netapi32Util.Group[] getUserGroups(String var0, String var1) {
      PointerByReference var2 = new PointerByReference();
      IntByReference var3 = new IntByReference();
      IntByReference var4 = new IntByReference();
      boolean var17 = false;

      int var10;
      Netapi32Util.Group[] var19;
      try {
         var17 = true;
         int var5 = Netapi32.INSTANCE.NetUserGetGroups(var1, var0, 0, var2, -1, var3, var4);
         if(var5 != 0) {
            throw new Win32Exception(var5);
         }

         LMAccess.GROUP_USERS_INFO_0 var6 = new LMAccess.GROUP_USERS_INFO_0(var2.getValue());
         LMAccess.GROUP_USERS_INFO_0[] var7 = (LMAccess.GROUP_USERS_INFO_0[])((LMAccess.GROUP_USERS_INFO_0[])var6.toArray(var3.getValue()));
         ArrayList var8 = new ArrayList();
         LMAccess.GROUP_USERS_INFO_0[] var9 = var7;
         var10 = var7.length;
         int var11 = 0;

         while(true) {
            if(var11 >= var10) {
               var19 = (Netapi32Util.Group[])var8.toArray(new Netapi32Util.Group[0]);
               var17 = false;
               break;
            }

            LMAccess.GROUP_USERS_INFO_0 var12 = var9[var11];
            Netapi32Util.Group var13 = new Netapi32Util.Group();
            var13.name = var12.grui0_name.toString();
            var8.add(var13);
            ++var11;
         }
      } finally {
         if(var17) {
            if(var2.getValue() != Pointer.NULL) {
               int var15 = Netapi32.INSTANCE.NetApiBufferFree(var2.getValue());
               if(0 != var15) {
                  throw new Win32Exception(var15);
               }
            }

         }
      }

      if(var2.getValue() != Pointer.NULL) {
         var10 = Netapi32.INSTANCE.NetApiBufferFree(var2.getValue());
         if(0 != var10) {
            throw new Win32Exception(var10);
         }
      }

      return var19;
   }

   public static Netapi32Util.DomainController getDC() {
      DsGetDC.PDOMAIN_CONTROLLER_INFO.PDOMAIN_CONTROLLER_INFO$ByReference var0 = new DsGetDC.PDOMAIN_CONTROLLER_INFO.PDOMAIN_CONTROLLER_INFO$ByReference();
      int var1 = Netapi32.INSTANCE.DsGetDcName((String)null, (String)null, (Guid.GUID)null, (String)null, 0, var0);
      if(0 != var1) {
         throw new Win32Exception(var1);
      } else {
         Netapi32Util.DomainController var2 = new Netapi32Util.DomainController();
         var2.address = var0.dci.DomainControllerAddress.toString();
         var2.addressType = var0.dci.DomainControllerAddressType;
         var2.clientSiteName = var0.dci.ClientSiteName.toString();
         var2.dnsForestName = var0.dci.DnsForestName.toString();
         var2.domainGuid = var0.dci.DomainGuid;
         var2.domainName = var0.dci.DomainName.toString();
         var2.flags = var0.dci.Flags;
         var2.name = var0.dci.DomainControllerName.toString();
         var1 = Netapi32.INSTANCE.NetApiBufferFree(var0.dci.getPointer());
         if(0 != var1) {
            throw new Win32Exception(var1);
         } else {
            return var2;
         }
      }
   }

   public static Netapi32Util.DomainTrust[] getDomainTrusts() {
      return getDomainTrusts((String)null);
   }

   public static Netapi32Util.DomainTrust[] getDomainTrusts(String var0) {
      NativeLongByReference var1 = new NativeLongByReference();
      DsGetDC.PDS_DOMAIN_TRUSTS.PDS_DOMAIN_TRUSTS$ByReference var2 = new DsGetDC.PDS_DOMAIN_TRUSTS.PDS_DOMAIN_TRUSTS$ByReference();
      int var3 = Netapi32.INSTANCE.DsEnumerateDomainTrusts(var0, new NativeLong(63L), var2, var1);
      if(0 != var3) {
         throw new Win32Exception(var3);
      } else {
         try {
            int var4 = var1.getValue().intValue();
            ArrayList var5 = new ArrayList(var4);
            DsGetDC.DS_DOMAIN_TRUSTS[] var6 = var2.getTrusts(var4);
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               DsGetDC.DS_DOMAIN_TRUSTS var9 = var6[var8];
               Netapi32Util.DomainTrust var10 = new Netapi32Util.DomainTrust();
               var10.DnsDomainName = var9.DnsDomainName.toString();
               var10.NetbiosDomainName = var9.NetbiosDomainName.toString();
               var10.DomainSid = var9.DomainSid;
               var10.DomainSidString = Advapi32Util.convertSidToStringSid(var9.DomainSid);
               var10.DomainGuid = var9.DomainGuid;
               var10.DomainGuidString = Ole32Util.getStringFromGUID(var9.DomainGuid);
               var10.flags = var9.Flags.intValue();
               var5.add(var10);
            }

            Netapi32Util.DomainTrust[] var14 = (Netapi32Util.DomainTrust[])var5.toArray(new Netapi32Util.DomainTrust[0]);
            return var14;
         } finally {
            var3 = Netapi32.INSTANCE.NetApiBufferFree(var2.getPointer());
            if(0 != var3) {
               throw new Win32Exception(var3);
            }
         }
      }
   }

   public static Netapi32Util.UserInfo getUserInfo(String var0) {
      return getUserInfo(var0, getDCName());
   }

   public static Netapi32Util.UserInfo getUserInfo(String var0, String var1) {
      PointerByReference var2 = new PointerByReference();
      boolean var3 = true;

      Netapi32Util.UserInfo var6;
      try {
         int var10 = Netapi32.INSTANCE.NetUserGetInfo(getDCName(), var0, 23, var2);
         if(var10 != 0) {
            throw new Win32Exception(var10);
         }

         LMAccess.USER_INFO_23 var4 = new LMAccess.USER_INFO_23(var2.getValue());
         Netapi32Util.UserInfo var5 = new Netapi32Util.UserInfo();
         var5.comment = var4.usri23_comment.toString();
         var5.flags = var4.usri23_flags;
         var5.fullName = var4.usri23_full_name.toString();
         var5.name = var4.usri23_name.toString();
         var5.sidString = Advapi32Util.convertSidToStringSid(var4.usri23_user_sid);
         var5.sid = var4.usri23_user_sid;
         var6 = var5;
      } finally {
         if(var2.getValue() != Pointer.NULL) {
            Netapi32.INSTANCE.NetApiBufferFree(var2.getValue());
         }

      }

      return var6;
   }

   public static class DomainTrust {
      public String NetbiosDomainName;
      public String DnsDomainName;
      public WinNT.PSID DomainSid;
      public String DomainSidString;
      public Guid.GUID DomainGuid;
      public String DomainGuidString;
      private int flags;

      public DomainTrust() {
      }

      public boolean isInForest() {
         return (this.flags & 1) != 0;
      }

      public boolean isOutbound() {
         return (this.flags & 2) != 0;
      }

      public boolean isRoot() {
         return (this.flags & 4) != 0;
      }

      public boolean isPrimary() {
         return (this.flags & 8) != 0;
      }

      public boolean isNativeMode() {
         return (this.flags & 16) != 0;
      }

      public boolean isInbound() {
         return (this.flags & 32) != 0;
      }
   }

   public static class DomainController {
      public String name;
      public String address;
      public int addressType;
      public Guid.GUID domainGuid;
      public String domainName;
      public String dnsForestName;
      public int flags;
      public String clientSiteName;

      public DomainController() {
      }
   }

   public static class LocalGroup extends Netapi32Util.Group {
      public String comment;

      public LocalGroup() {
      }
   }

   public static class UserInfo extends Netapi32Util.User {
      public String fullName;
      public String sidString;
      public WinNT.PSID sid;
      public int flags;

      public UserInfo() {
      }
   }

   public static class User {
      public String name;
      public String comment;

      public User() {
      }
   }

   public static class Group {
      public String name;

      public Group() {
      }
   }
}
