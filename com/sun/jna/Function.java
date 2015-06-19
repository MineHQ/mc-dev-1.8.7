package com.sun.jna;

import com.sun.jna.Callback;
import com.sun.jna.CallbackReference;
import com.sun.jna.FromNativeContext;
import com.sun.jna.FromNativeConverter;
import com.sun.jna.FunctionParameterContext;
import com.sun.jna.FunctionResultContext;
import com.sun.jna.Memory;
import com.sun.jna.MethodParameterContext;
import com.sun.jna.MethodResultContext;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.NativeString;
import com.sun.jna.Pointer;
import com.sun.jna.StringArray;
import com.sun.jna.Structure;
import com.sun.jna.ToNativeContext;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

public class Function extends Pointer {
   public static final int MAX_NARGS = 256;
   public static final int C_CONVENTION = 0;
   public static final int ALT_CONVENTION = 1;
   private static final int MASK_CC = 3;
   public static final int THROW_LAST_ERROR = 4;
   static final Integer INTEGER_TRUE = new Integer(-1);
   static final Integer INTEGER_FALSE = new Integer(0);
   private NativeLibrary library;
   private final String functionName;
   int callFlags;
   final Map options;
   static final String OPTION_INVOKING_METHOD = "invoking-method";

   public static Function getFunction(String var0, String var1) {
      return NativeLibrary.getInstance(var0).getFunction(var1);
   }

   public static Function getFunction(String var0, String var1, int var2) {
      return NativeLibrary.getInstance(var0).getFunction(var1, var2);
   }

   public static Function getFunction(Pointer var0) {
      return getFunction(var0, 0);
   }

   public static Function getFunction(Pointer var0, int var1) {
      return new Function(var0, var1);
   }

   Function(NativeLibrary var1, String var2, int var3) {
      this.checkCallingConvention(var3 & 3);
      if(var2 == null) {
         throw new NullPointerException("Function name must not be null");
      } else {
         this.library = var1;
         this.functionName = var2;
         this.callFlags = var3;
         this.options = var1.options;

         try {
            this.peer = var1.getSymbolAddress(var2);
         } catch (UnsatisfiedLinkError var5) {
            throw new UnsatisfiedLinkError("Error looking up function \'" + var2 + "\': " + var5.getMessage());
         }
      }
   }

   Function(Pointer var1, int var2) {
      this.checkCallingConvention(var2 & 3);
      if(var1 != null && var1.peer != 0L) {
         this.functionName = var1.toString();
         this.callFlags = var2;
         this.peer = var1.peer;
         this.options = Collections.EMPTY_MAP;
      } else {
         throw new NullPointerException("Function address may not be null");
      }
   }

   private void checkCallingConvention(int var1) throws IllegalArgumentException {
      switch(var1) {
      case 0:
      case 1:
         return;
      default:
         throw new IllegalArgumentException("Unrecognized calling convention: " + var1);
      }
   }

   public String getName() {
      return this.functionName;
   }

   public int getCallingConvention() {
      return this.callFlags & 3;
   }

   public Object invoke(Class var1, Object[] var2) {
      return this.invoke(var1, var2, this.options);
   }

   public Object invoke(Class var1, Object[] var2, Map var3) {
      Object[] var4 = new Object[0];
      if(var2 != null) {
         if(var2.length > 256) {
            throw new UnsupportedOperationException("Maximum argument count is 256");
         }

         var4 = new Object[var2.length];
         System.arraycopy(var2, 0, var4, 0, var4.length);
      }

      TypeMapper var5 = (TypeMapper)var3.get("type-mapper");
      Method var6 = (Method)var3.get("invoking-method");
      boolean var7 = Boolean.TRUE.equals(var3.get("allow-objects"));

      for(int var8 = 0; var8 < var4.length; ++var8) {
         var4[var8] = this.convertArgument(var4, var8, var6, var5, var7);
      }

      Class var18 = var1;
      Object var9 = null;
      if(NativeMapped.class.isAssignableFrom(var1)) {
         NativeMappedConverter var10 = NativeMappedConverter.getInstance(var1);
         var9 = var10;
         var18 = var10.nativeType();
      } else if(var5 != null) {
         var9 = var5.getFromNativeConverter(var1);
         if(var9 != null) {
            var18 = ((FromNativeConverter)var9).nativeType();
         }
      }

      Object var19 = this.invoke(var4, var18, var7);
      if(var9 != null) {
         Object var11;
         if(var6 != null) {
            var11 = new MethodResultContext(var1, this, var2, var6);
         } else {
            var11 = new FunctionResultContext(var1, this, var2);
         }

         var19 = ((FromNativeConverter)var9).fromNative(var19, (FromNativeContext)var11);
      }

      if(var2 != null) {
         for(int var20 = 0; var20 < var2.length; ++var20) {
            Object var12 = var2[var20];
            if(var12 != null) {
               if(var12 instanceof Structure) {
                  if(!(var12 instanceof Structure.ByValue)) {
                     ((Structure)var12).autoRead();
                  }
               } else if(var4[var20] instanceof Function.PostCallRead) {
                  ((Function.PostCallRead)var4[var20]).read();
                  if(var4[var20] instanceof Function.PointerArray) {
                     Function.PointerArray var13 = (Function.PointerArray)var4[var20];
                     if(Structure.ByReference[].class.isAssignableFrom(var12.getClass())) {
                        Class var14 = var12.getClass().getComponentType();
                        Structure[] var15 = (Structure[])((Structure[])var12);

                        for(int var16 = 0; var16 < var15.length; ++var16) {
                           Pointer var17 = var13.getPointer((long)(Pointer.SIZE * var16));
                           var15[var16] = Structure.updateStructureByReference(var14, var15[var16], var17);
                        }
                     }
                  }
               } else if(Structure[].class.isAssignableFrom(var12.getClass())) {
                  Structure.autoRead((Structure[])((Structure[])var12));
               }
            }
         }
      }

      return var19;
   }

   Object invoke(Object[] var1, Class var2, boolean var3) {
      Object var4 = null;
      if(var2 != null && var2 != Void.TYPE && var2 != Void.class) {
         if(var2 != Boolean.TYPE && var2 != Boolean.class) {
            if(var2 != Byte.TYPE && var2 != Byte.class) {
               if(var2 != Short.TYPE && var2 != Short.class) {
                  if(var2 != Character.TYPE && var2 != Character.class) {
                     if(var2 != Integer.TYPE && var2 != Integer.class) {
                        if(var2 != Long.TYPE && var2 != Long.class) {
                           if(var2 != Float.TYPE && var2 != Float.class) {
                              if(var2 != Double.TYPE && var2 != Double.class) {
                                 if(var2 == String.class) {
                                    var4 = this.invokeString(this.callFlags, var1, false);
                                 } else if(var2 == WString.class) {
                                    String var5 = this.invokeString(this.callFlags, var1, true);
                                    if(var5 != null) {
                                       var4 = new WString(var5);
                                    }
                                 } else {
                                    if(Pointer.class.isAssignableFrom(var2)) {
                                       return this.invokePointer(this.callFlags, var1);
                                    }

                                    if(Structure.class.isAssignableFrom(var2)) {
                                       Structure var9;
                                       if(Structure.ByValue.class.isAssignableFrom(var2)) {
                                          var9 = Native.invokeStructure(this.peer, this.callFlags, var1, Structure.newInstance(var2));
                                          var9.autoRead();
                                          var4 = var9;
                                       } else {
                                          var4 = this.invokePointer(this.callFlags, var1);
                                          if(var4 != null) {
                                             var9 = Structure.newInstance(var2);
                                             var9.useMemory((Pointer)var4);
                                             var9.autoRead();
                                             var4 = var9;
                                          }
                                       }
                                    } else if(Callback.class.isAssignableFrom(var2)) {
                                       var4 = this.invokePointer(this.callFlags, var1);
                                       if(var4 != null) {
                                          var4 = CallbackReference.getCallback(var2, (Pointer)var4);
                                       }
                                    } else {
                                       Pointer var10;
                                       if(var2 == String[].class) {
                                          var10 = this.invokePointer(this.callFlags, var1);
                                          if(var10 != null) {
                                             var4 = var10.getStringArray(0L);
                                          }
                                       } else if(var2 == WString[].class) {
                                          var10 = this.invokePointer(this.callFlags, var1);
                                          if(var10 != null) {
                                             String[] var6 = var10.getStringArray(0L, true);
                                             WString[] var7 = new WString[var6.length];

                                             for(int var8 = 0; var8 < var6.length; ++var8) {
                                                var7[var8] = new WString(var6[var8]);
                                             }

                                             var4 = var7;
                                          }
                                       } else if(var2 == Pointer[].class) {
                                          var10 = this.invokePointer(this.callFlags, var1);
                                          if(var10 != null) {
                                             var4 = var10.getPointerArray(0L);
                                          }
                                       } else {
                                          if(!var3) {
                                             throw new IllegalArgumentException("Unsupported return type " + var2 + " in function " + this.getName());
                                          }

                                          var4 = Native.invokeObject(this.peer, this.callFlags, var1);
                                          if(var4 != null && !var2.isAssignableFrom(var4.getClass())) {
                                             throw new ClassCastException("Return type " + var2 + " does not match result " + var4.getClass());
                                          }
                                       }
                                    }
                                 }
                              } else {
                                 var4 = new Double(Native.invokeDouble(this.peer, this.callFlags, var1));
                              }
                           } else {
                              var4 = new Float(Native.invokeFloat(this.peer, this.callFlags, var1));
                           }
                        } else {
                           var4 = new Long(Native.invokeLong(this.peer, this.callFlags, var1));
                        }
                     } else {
                        var4 = new Integer(Native.invokeInt(this.peer, this.callFlags, var1));
                     }
                  } else {
                     var4 = new Character((char)Native.invokeInt(this.peer, this.callFlags, var1));
                  }
               } else {
                  var4 = new Short((short)Native.invokeInt(this.peer, this.callFlags, var1));
               }
            } else {
               var4 = new Byte((byte)Native.invokeInt(this.peer, this.callFlags, var1));
            }
         } else {
            var4 = valueOf(Native.invokeInt(this.peer, this.callFlags, var1) != 0);
         }
      } else {
         Native.invokeVoid(this.peer, this.callFlags, var1);
         var4 = null;
      }

      return var4;
   }

   private Pointer invokePointer(int var1, Object[] var2) {
      long var3 = Native.invokePointer(this.peer, var1, var2);
      return var3 == 0L?null:new Pointer(var3);
   }

   private Object convertArgument(Object[] var1, int var2, Method var3, TypeMapper var4, boolean var5) {
      Object var6 = var1[var2];
      Class var7;
      if(var6 != null) {
         var7 = var6.getClass();
         Object var8 = null;
         if(NativeMapped.class.isAssignableFrom(var7)) {
            var8 = NativeMappedConverter.getInstance(var7);
         } else if(var4 != null) {
            var8 = var4.getToNativeConverter(var7);
         }

         if(var8 != null) {
            Object var9;
            if(var3 != null) {
               var9 = new MethodParameterContext(this, var1, var2, var3);
            } else {
               var9 = new FunctionParameterContext(this, var1, var2);
            }

            var6 = ((ToNativeConverter)var8).toNative(var6, (ToNativeContext)var9);
         }
      }

      if(var6 != null && !this.isPrimitiveArray(var6.getClass())) {
         var7 = var6.getClass();
         Class var15;
         if(var6 instanceof Structure) {
            Structure var14 = (Structure)var6;
            var14.autoWrite();
            if(var14 instanceof Structure.ByValue) {
               var15 = var14.getClass();
               if(var3 != null) {
                  Class[] var16 = var3.getParameterTypes();
                  if(isVarArgs(var3)) {
                     if(var2 < var16.length - 1) {
                        var15 = var16[var2];
                     } else {
                        Class var17 = var16[var16.length - 1].getComponentType();
                        if(var17 != Object.class) {
                           var15 = var17;
                        }
                     }
                  } else {
                     var15 = var16[var2];
                  }
               }

               if(Structure.ByValue.class.isAssignableFrom(var15)) {
                  return var14;
               }
            }

            return var14.getPointer();
         } else if(var6 instanceof Callback) {
            return CallbackReference.getFunctionPointer((Callback)var6);
         } else if(var6 instanceof String) {
            return (new NativeString((String)var6, false)).getPointer();
         } else if(var6 instanceof WString) {
            return (new NativeString(var6.toString(), true)).getPointer();
         } else if(var6 instanceof Boolean) {
            return Boolean.TRUE.equals(var6)?INTEGER_TRUE:INTEGER_FALSE;
         } else if(String[].class == var7) {
            return new StringArray((String[])((String[])var6));
         } else if(WString[].class == var7) {
            return new StringArray((WString[])((WString[])var6));
         } else if(Pointer[].class == var7) {
            return new Function.PointerArray((Pointer[])((Pointer[])var6));
         } else if(NativeMapped[].class.isAssignableFrom(var7)) {
            return new Function.NativeMappedArray((NativeMapped[])((NativeMapped[])var6));
         } else if(Structure[].class.isAssignableFrom(var7)) {
            Structure[] var13 = (Structure[])((Structure[])var6);
            var15 = var7.getComponentType();
            boolean var10 = Structure.ByReference.class.isAssignableFrom(var15);
            if(var10) {
               Pointer[] var11 = new Pointer[var13.length + 1];

               for(int var12 = 0; var12 < var13.length; ++var12) {
                  var11[var12] = var13[var12] != null?var13[var12].getPointer():null;
               }

               return new Function.PointerArray(var11);
            } else if(var13.length == 0) {
               throw new IllegalArgumentException("Structure array must have non-zero length");
            } else if(var13[0] == null) {
               Structure.newInstance(var15).toArray(var13);
               return var13[0].getPointer();
            } else {
               Structure.autoWrite(var13);
               return var13[0].getPointer();
            }
         } else if(var7.isArray()) {
            throw new IllegalArgumentException("Unsupported array argument type: " + var7.getComponentType());
         } else if(var5) {
            return var6;
         } else if(!Native.isSupportedNativeType(var6.getClass())) {
            throw new IllegalArgumentException("Unsupported argument type " + var6.getClass().getName() + " at parameter " + var2 + " of function " + this.getName());
         } else {
            return var6;
         }
      } else {
         return var6;
      }
   }

   private boolean isPrimitiveArray(Class var1) {
      return var1.isArray() && var1.getComponentType().isPrimitive();
   }

   public void invoke(Object[] var1) {
      this.invoke(Void.class, var1);
   }

   private String invokeString(int var1, Object[] var2, boolean var3) {
      Pointer var4 = this.invokePointer(var1, var2);
      String var5 = null;
      if(var4 != null) {
         if(var3) {
            var5 = var4.getString(0L, var3);
         } else {
            var5 = var4.getString(0L);
         }
      }

      return var5;
   }

   public String toString() {
      return this.library != null?"native function " + this.functionName + "(" + this.library.getName() + ")@0x" + Long.toHexString(this.peer):"native function@0x" + Long.toHexString(this.peer);
   }

   public Object invokeObject(Object[] var1) {
      return this.invoke(Object.class, var1);
   }

   public Pointer invokePointer(Object[] var1) {
      return (Pointer)this.invoke(Pointer.class, var1);
   }

   public String invokeString(Object[] var1, boolean var2) {
      Object var3 = this.invoke(var2?WString.class:String.class, var1);
      return var3 != null?var3.toString():null;
   }

   public int invokeInt(Object[] var1) {
      return ((Integer)this.invoke(Integer.class, var1)).intValue();
   }

   public long invokeLong(Object[] var1) {
      return ((Long)this.invoke(Long.class, var1)).longValue();
   }

   public float invokeFloat(Object[] var1) {
      return ((Float)this.invoke(Float.class, var1)).floatValue();
   }

   public double invokeDouble(Object[] var1) {
      return ((Double)this.invoke(Double.class, var1)).doubleValue();
   }

   public void invokeVoid(Object[] var1) {
      this.invoke(Void.class, var1);
   }

   public boolean equals(Object var1) {
      if(var1 == this) {
         return true;
      } else if(var1 == null) {
         return false;
      } else if(var1.getClass() != this.getClass()) {
         return false;
      } else {
         Function var2 = (Function)var1;
         return var2.callFlags == this.callFlags && var2.options.equals(this.options) && var2.peer == this.peer;
      }
   }

   public int hashCode() {
      return this.callFlags + this.options.hashCode() + super.hashCode();
   }

   static Object[] concatenateVarArgs(Object[] var0) {
      if(var0 != null && var0.length > 0) {
         Object var1 = var0[var0.length - 1];
         Class var2 = var1 != null?var1.getClass():null;
         if(var2 != null && var2.isArray()) {
            Object[] var3 = (Object[])((Object[])var1);
            Object[] var4 = new Object[var0.length + var3.length];
            System.arraycopy(var0, 0, var4, 0, var0.length - 1);
            System.arraycopy(var3, 0, var4, var0.length - 1, var3.length);
            var4[var4.length - 1] = null;
            var0 = var4;
         }
      }

      return var0;
   }

   static boolean isVarArgs(Method var0) {
      try {
         Method var1 = var0.getClass().getMethod("isVarArgs", new Class[0]);
         return Boolean.TRUE.equals(var1.invoke(var0, new Object[0]));
      } catch (SecurityException var2) {
         ;
      } catch (NoSuchMethodException var3) {
         ;
      } catch (IllegalArgumentException var4) {
         ;
      } catch (IllegalAccessException var5) {
         ;
      } catch (InvocationTargetException var6) {
         ;
      }

      return false;
   }

   static Boolean valueOf(boolean var0) {
      return var0?Boolean.TRUE:Boolean.FALSE;
   }

   private static class PointerArray extends Memory implements Function.PostCallRead {
      private final Pointer[] original;

      public PointerArray(Pointer[] var1) {
         super((long)(Pointer.SIZE * (var1.length + 1)));
         this.original = var1;

         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.setPointer((long)(var2 * Pointer.SIZE), var1[var2]);
         }

         this.setPointer((long)(Pointer.SIZE * var1.length), (Pointer)null);
      }

      public void read() {
         this.read(0L, this.original, 0, this.original.length);
      }
   }

   private static class NativeMappedArray extends Memory implements Function.PostCallRead {
      private final NativeMapped[] original;

      public NativeMappedArray(NativeMapped[] var1) {
         super((long)Native.getNativeSize(var1.getClass(), var1));
         this.original = var1;
         this.setValue(0L, this.original, this.original.getClass());
      }

      public void read() {
         this.getValue(0L, this.original.getClass(), this.original);
      }
   }

   public interface PostCallRead {
      void read();
   }
}
