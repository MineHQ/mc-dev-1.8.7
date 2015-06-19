package oshi;

import com.sun.jna.Platform;
import oshi.PlatformEnum;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.software.os.linux.LinuxHardwareAbstractionLayer;
import oshi.software.os.linux.LinuxOperatingSystem;
import oshi.software.os.mac.MacHardwareAbstractionLayer;
import oshi.software.os.mac.MacOperatingSystem;
import oshi.software.os.windows.WindowsHardwareAbstractionLayer;
import oshi.software.os.windows.WindowsOperatingSystem;

public class SystemInfo {
   private OperatingSystem _os = null;
   private HardwareAbstractionLayer _hardware = null;
   private PlatformEnum currentPlatformEnum;

   public SystemInfo() {
      if(Platform.isWindows()) {
         this.currentPlatformEnum = PlatformEnum.WINDOWS;
      } else if(Platform.isLinux()) {
         this.currentPlatformEnum = PlatformEnum.LINUX;
      } else if(Platform.isMac()) {
         this.currentPlatformEnum = PlatformEnum.MACOSX;
      } else {
         this.currentPlatformEnum = PlatformEnum.UNKNOWN;
      }

   }

   public OperatingSystem getOperatingSystem() {
      if(this._os == null) {
         switch(null.$SwitchMap$oshi$PlatformEnum[this.currentPlatformEnum.ordinal()]) {
         case 1:
            this._os = new WindowsOperatingSystem();
            break;
         case 2:
            this._os = new LinuxOperatingSystem();
            break;
         case 3:
            this._os = new MacOperatingSystem();
            break;
         default:
            throw new RuntimeException("Operating system not supported: " + Platform.getOSType());
         }
      }

      return this._os;
   }

   public HardwareAbstractionLayer getHardware() {
      if(this._hardware == null) {
         switch(null.$SwitchMap$oshi$PlatformEnum[this.currentPlatformEnum.ordinal()]) {
         case 1:
            this._hardware = new WindowsHardwareAbstractionLayer();
            break;
         case 2:
            this._hardware = new LinuxHardwareAbstractionLayer();
            break;
         case 3:
            this._hardware = new MacHardwareAbstractionLayer();
            break;
         default:
            throw new RuntimeException("Operating system not supported: " + Platform.getOSType());
         }
      }

      return this._hardware;
   }
}
