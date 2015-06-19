package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public interface Winioctl extends StdCallLibrary {
   int IOCTL_STORAGE_GET_DEVICE_NUMBER = 2953344;

   public static class STORAGE_DEVICE_NUMBER extends Structure {
      public int DeviceType;
      public int DeviceNumber;
      public int PartitionNumber;

      public STORAGE_DEVICE_NUMBER() {
      }

      public STORAGE_DEVICE_NUMBER(Pointer var1) {
         super(var1);
         this.read();
      }

      public static class STORAGE_DEVICE_NUMBER$ByReference extends Winioctl.STORAGE_DEVICE_NUMBER implements Structure.ByReference {
         public STORAGE_DEVICE_NUMBER$ByReference() {
         }

         public STORAGE_DEVICE_NUMBER$ByReference(Pointer var1) {
            super(var1);
         }
      }
   }
}
