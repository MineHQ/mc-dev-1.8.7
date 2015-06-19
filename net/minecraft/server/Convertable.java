package net.minecraft.server;

import net.minecraft.server.IDataManager;
import net.minecraft.server.IProgressUpdate;

public interface Convertable {
   IDataManager a(String var1, boolean var2);

   void d();

   boolean e(String var1);

   boolean isConvertable(String var1);

   boolean convert(String var1, IProgressUpdate var2);
}
