package com.sun.jna;

import com.sun.jna.Callback;
import com.sun.jna.CallbackReference;
import com.sun.jna.FromNativeContext;
import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.Platform;
import com.sun.jna.Structure;
import com.sun.jna.ToNativeContext;
import com.sun.jna.WString;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class Pointer {
   public static final int SIZE;
   public static final Pointer NULL;
   protected long peer;

   public static final Pointer createConstant(long var0) {
      return new Pointer.Opaque(var0);
   }

   public static final Pointer createConstant(int var0) {
      return new Pointer.Opaque((long)var0 & -1L);
   }

   Pointer() {
   }

   public Pointer(long var1) {
      this.peer = var1;
   }

   public Pointer share(long var1) {
      return this.share(var1, 0L);
   }

   public Pointer share(long var1, long var3) {
      return var1 == 0L?this:new Pointer(this.peer + var1);
   }

   public void clear(long var1) {
      this.setMemory(0L, var1, (byte)0);
   }

   public boolean equals(Object var1) {
      return var1 == this?true:(var1 == null?false:var1 instanceof Pointer && ((Pointer)var1).peer == this.peer);
   }

   public int hashCode() {
      return (int)((this.peer >>> 32) + (this.peer & -1L));
   }

   public long indexOf(long var1, byte var3) {
      return Native.indexOf(this.peer + var1, var3);
   }

   public void read(long var1, byte[] var3, int var4, int var5) {
      Native.read(this.peer + var1, var3, var4, var5);
   }

   public void read(long var1, short[] var3, int var4, int var5) {
      Native.read(this.peer + var1, var3, var4, var5);
   }

   public void read(long var1, char[] var3, int var4, int var5) {
      Native.read(this.peer + var1, var3, var4, var5);
   }

   public void read(long var1, int[] var3, int var4, int var5) {
      Native.read(this.peer + var1, var3, var4, var5);
   }

   public void read(long var1, long[] var3, int var4, int var5) {
      Native.read(this.peer + var1, var3, var4, var5);
   }

   public void read(long var1, float[] var3, int var4, int var5) {
      Native.read(this.peer + var1, var3, var4, var5);
   }

   public void read(long var1, double[] var3, int var4, int var5) {
      Native.read(this.peer + var1, var3, var4, var5);
   }

   public void read(long var1, Pointer[] var3, int var4, int var5) {
      for(int var6 = 0; var6 < var5; ++var6) {
         Pointer var7 = this.getPointer(var1 + (long)(var6 * SIZE));
         Pointer var8 = var3[var6 + var4];
         if(var8 == null || var7 == null || var7.peer != var8.peer) {
            var3[var6 + var4] = var7;
         }
      }

   }

   public void write(long var1, byte[] var3, int var4, int var5) {
      Native.write(this.peer + var1, var3, var4, var5);
   }

   public void write(long var1, short[] var3, int var4, int var5) {
      Native.write(this.peer + var1, var3, var4, var5);
   }

   public void write(long var1, char[] var3, int var4, int var5) {
      Native.write(this.peer + var1, var3, var4, var5);
   }

   public void write(long var1, int[] var3, int var4, int var5) {
      Native.write(this.peer + var1, var3, var4, var5);
   }

   public void write(long var1, long[] var3, int var4, int var5) {
      Native.write(this.peer + var1, var3, var4, var5);
   }

   public void write(long var1, float[] var3, int var4, int var5) {
      Native.write(this.peer + var1, var3, var4, var5);
   }

   public void write(long var1, double[] var3, int var4, int var5) {
      Native.write(this.peer + var1, var3, var4, var5);
   }

   public void write(long var1, Pointer[] var3, int var4, int var5) {
      for(int var6 = 0; var6 < var5; ++var6) {
         this.setPointer(var1 + (long)(var6 * SIZE), var3[var4 + var6]);
      }

   }

   Object getValue(long var1, Class var3, Object var4) {
      Object var5 = null;
      if(Structure.class.isAssignableFrom(var3)) {
         Structure var12 = (Structure)var4;
         if(Structure.ByReference.class.isAssignableFrom(var3)) {
            var12 = Structure.updateStructureByReference(var3, var12, this.getPointer(var1));
         } else {
            var12.useMemory(this, (int)var1);
            var12.read();
         }

         var5 = var12;
      } else if(var3 != Boolean.TYPE && var3 != Boolean.class) {
         if(var3 != Byte.TYPE && var3 != Byte.class) {
            if(var3 != Short.TYPE && var3 != Short.class) {
               if(var3 != Character.TYPE && var3 != Character.class) {
                  if(var3 != Integer.TYPE && var3 != Integer.class) {
                     if(var3 != Long.TYPE && var3 != Long.class) {
                        if(var3 != Float.TYPE && var3 != Float.class) {
                           if(var3 != Double.TYPE && var3 != Double.class) {
                              Pointer var9;
                              Pointer var11;
                              if(Pointer.class.isAssignableFrom(var3)) {
                                 var9 = this.getPointer(var1);
                                 if(var9 != null) {
                                    var11 = var4 instanceof Pointer?(Pointer)var4:null;
                                    if(var11 != null && var9.peer == var11.peer) {
                                       var5 = var11;
                                    } else {
                                       var5 = var9;
                                    }
                                 }
                              } else if(var3 == String.class) {
                                 var9 = this.getPointer(var1);
                                 var5 = var9 != null?var9.getString(0L):null;
                              } else if(var3 == WString.class) {
                                 var9 = this.getPointer(var1);
                                 var5 = var9 != null?new WString(var9.getString(0L, true)):null;
                              } else if(Callback.class.isAssignableFrom(var3)) {
                                 var9 = this.getPointer(var1);
                                 if(var9 == null) {
                                    var5 = null;
                                 } else {
                                    Callback var13 = (Callback)var4;
                                    Pointer var14 = CallbackReference.getFunctionPointer(var13);
                                    if(!var9.equals(var14)) {
                                       var13 = CallbackReference.getCallback(var3, var9);
                                    }

                                    var5 = var13;
                                 }
                              } else if(Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(var3)) {
                                 var9 = this.getPointer(var1);
                                 if(var9 == null) {
                                    var5 = null;
                                 } else {
                                    var11 = var4 == null?null:Native.getDirectBufferPointer((Buffer)var4);
                                    if(var11 == null || !var11.equals(var9)) {
                                       throw new IllegalStateException("Can\'t autogenerate a direct buffer on memory read");
                                    }

                                    var5 = var4;
                                 }
                              } else if(NativeMapped.class.isAssignableFrom(var3)) {
                                 NativeMapped var6 = (NativeMapped)var4;
                                 if(var6 != null) {
                                    Object var7 = this.getValue(var1, var6.nativeType(), (Object)null);
                                    var5 = var6.fromNative(var7, new FromNativeContext(var3));
                                 } else {
                                    NativeMappedConverter var10 = NativeMappedConverter.getInstance(var3);
                                    Object var8 = this.getValue(var1, var10.nativeType(), (Object)null);
                                    var5 = var10.fromNative(var8, new FromNativeContext(var3));
                                 }
                              } else {
                                 if(!var3.isArray()) {
                                    throw new IllegalArgumentException("Reading \"" + var3 + "\" from memory is not supported");
                                 }

                                 var5 = var4;
                                 if(var4 == null) {
                                    throw new IllegalStateException("Need an initialized array");
                                 }

                                 this.getArrayValue(var1, var4, var3.getComponentType());
                              }
                           } else {
                              var5 = new Double(this.getDouble(var1));
                           }
                        } else {
                           var5 = new Float(this.getFloat(var1));
                        }
                     } else {
                        var5 = new Long(this.getLong(var1));
                     }
                  } else {
                     var5 = new Integer(this.getInt(var1));
                  }
               } else {
                  var5 = new Character(this.getChar(var1));
               }
            } else {
               var5 = new Short(this.getShort(var1));
            }
         } else {
            var5 = new Byte(this.getByte(var1));
         }
      } else {
         var5 = Function.valueOf(this.getInt(var1) != 0);
      }

      return var5;
   }

   private void getArrayValue(long var1, Object var3, Class var4) {
      boolean var5 = false;
      int var12 = Array.getLength(var3);
      if(var4 == Byte.TYPE) {
         this.read(var1, (byte[])((byte[])((byte[])var3)), 0, var12);
      } else if(var4 == Short.TYPE) {
         this.read(var1, (short[])((short[])((short[])var3)), 0, var12);
      } else if(var4 == Character.TYPE) {
         this.read(var1, (char[])((char[])((char[])var3)), 0, var12);
      } else if(var4 == Integer.TYPE) {
         this.read(var1, (int[])((int[])((int[])var3)), 0, var12);
      } else if(var4 == Long.TYPE) {
         this.read(var1, (long[])((long[])((long[])var3)), 0, var12);
      } else if(var4 == Float.TYPE) {
         this.read(var1, (float[])((float[])((float[])var3)), 0, var12);
      } else if(var4 == Double.TYPE) {
         this.read(var1, (double[])((double[])((double[])var3)), 0, var12);
      } else if(Pointer.class.isAssignableFrom(var4)) {
         this.read(var1, (Pointer[])((Pointer[])((Pointer[])var3)), 0, var12);
      } else {
         int var9;
         if(Structure.class.isAssignableFrom(var4)) {
            Structure[] var7 = (Structure[])((Structure[])var3);
            if(Structure.ByReference.class.isAssignableFrom(var4)) {
               Pointer[] var8 = this.getPointerArray(var1, var7.length);

               for(var9 = 0; var9 < var7.length; ++var9) {
                  var7[var9] = Structure.updateStructureByReference(var4, var7[var9], var8[var9]);
               }
            } else {
               for(int var14 = 0; var14 < var7.length; ++var14) {
                  if(var7[var14] == null) {
                     var7[var14] = Structure.newInstance(var4);
                  }

                  var7[var14].useMemory(this, (int)(var1 + (long)(var14 * var7[var14].size())));
                  var7[var14].read();
               }
            }
         } else {
            if(!NativeMapped.class.isAssignableFrom(var4)) {
               throw new IllegalArgumentException("Reading array of " + var4 + " from memory not supported");
            }

            NativeMapped[] var13 = (NativeMapped[])((NativeMapped[])var3);
            NativeMappedConverter var15 = NativeMappedConverter.getInstance(var4);
            var9 = Native.getNativeSize(var3.getClass(), var3) / var13.length;

            for(int var10 = 0; var10 < var13.length; ++var10) {
               Object var11 = this.getValue(var1 + (long)(var9 * var10), var15.nativeType(), var13[var10]);
               var13[var10] = (NativeMapped)var15.fromNative(var11, new FromNativeContext(var4));
            }
         }
      }

   }

   public byte getByte(long var1) {
      return Native.getByte(this.peer + var1);
   }

   public char getChar(long var1) {
      return Native.getChar(this.peer + var1);
   }

   public short getShort(long var1) {
      return Native.getShort(this.peer + var1);
   }

   public int getInt(long var1) {
      return Native.getInt(this.peer + var1);
   }

   public long getLong(long var1) {
      return Native.getLong(this.peer + var1);
   }

   public NativeLong getNativeLong(long var1) {
      return new NativeLong(NativeLong.SIZE == 8?this.getLong(var1):(long)this.getInt(var1));
   }

   public float getFloat(long var1) {
      return Native.getFloat(this.peer + var1);
   }

   public double getDouble(long var1) {
      return Native.getDouble(this.peer + var1);
   }

   public Pointer getPointer(long var1) {
      return Native.getPointer(this.peer + var1);
   }

   public ByteBuffer getByteBuffer(long var1, long var3) {
      return Native.getDirectByteBuffer(this.peer + var1, var3).order(ByteOrder.nativeOrder());
   }

   public String getString(long var1, boolean var3) {
      return Native.getString(this.peer + var1, var3);
   }

   public String getString(long var1) {
      String var3 = System.getProperty("jna.encoding");
      if(var3 != null) {
         long var4 = this.indexOf(var1, (byte)0);
         if(var4 != -1L) {
            if(var4 > 2147483647L) {
               throw new OutOfMemoryError("String exceeds maximum length: " + var4);
            }

            byte[] var6 = this.getByteArray(var1, (int)var4);

            try {
               return new String(var6, var3);
            } catch (UnsupportedEncodingException var8) {
               ;
            }
         }
      }

      return this.getString(var1, false);
   }

   public byte[] getByteArray(long var1, int var3) {
      byte[] var4 = new byte[var3];
      this.read(var1, (byte[])var4, 0, var3);
      return var4;
   }

   public char[] getCharArray(long var1, int var3) {
      char[] var4 = new char[var3];
      this.read(var1, (char[])var4, 0, var3);
      return var4;
   }

   public short[] getShortArray(long var1, int var3) {
      short[] var4 = new short[var3];
      this.read(var1, (short[])var4, 0, var3);
      return var4;
   }

   public int[] getIntArray(long var1, int var3) {
      int[] var4 = new int[var3];
      this.read(var1, (int[])var4, 0, var3);
      return var4;
   }

   public long[] getLongArray(long var1, int var3) {
      long[] var4 = new long[var3];
      this.read(var1, (long[])var4, 0, var3);
      return var4;
   }

   public float[] getFloatArray(long var1, int var3) {
      float[] var4 = new float[var3];
      this.read(var1, (float[])var4, 0, var3);
      return var4;
   }

   public double[] getDoubleArray(long var1, int var3) {
      double[] var4 = new double[var3];
      this.read(var1, (double[])var4, 0, var3);
      return var4;
   }

   public Pointer[] getPointerArray(long var1) {
      ArrayList var3 = new ArrayList();
      int var4 = 0;

      for(Pointer var5 = this.getPointer(var1); var5 != null; var5 = this.getPointer(var1 + (long)var4)) {
         var3.add(var5);
         var4 += SIZE;
      }

      return (Pointer[])((Pointer[])var3.toArray(new Pointer[var3.size()]));
   }

   public Pointer[] getPointerArray(long var1, int var3) {
      Pointer[] var4 = new Pointer[var3];
      this.read(var1, (Pointer[])var4, 0, var3);
      return var4;
   }

   public String[] getStringArray(long var1) {
      return this.getStringArray(var1, -1, false);
   }

   public String[] getStringArray(long var1, int var3) {
      return this.getStringArray(var1, var3, false);
   }

   public String[] getStringArray(long var1, boolean var3) {
      return this.getStringArray(var1, -1, var3);
   }

   public String[] getStringArray(long var1, int var3, boolean var4) {
      ArrayList var5 = new ArrayList();
      int var7 = 0;
      Pointer var6;
      if(var3 != -1) {
         var6 = this.getPointer(var1 + (long)var7);
         int var10 = 0;

         while(var10++ < var3) {
            String var9 = var6 == null?null:var6.getString(0L, var4);
            var5.add(var9);
            if(var10 < var3) {
               var7 += SIZE;
               var6 = this.getPointer(var1 + (long)var7);
            }
         }
      } else {
         while((var6 = this.getPointer(var1 + (long)var7)) != null) {
            String var8 = var6 == null?null:var6.getString(0L, var4);
            var5.add(var8);
            var7 += SIZE;
         }
      }

      return (String[])((String[])var5.toArray(new String[var5.size()]));
   }

   void setValue(long var1, Object var3, Class var4) {
      if(var4 != Boolean.TYPE && var4 != Boolean.class) {
         if(var4 != Byte.TYPE && var4 != Byte.class) {
            if(var4 != Short.TYPE && var4 != Short.class) {
               if(var4 != Character.TYPE && var4 != Character.class) {
                  if(var4 != Integer.TYPE && var4 != Integer.class) {
                     if(var4 != Long.TYPE && var4 != Long.class) {
                        if(var4 != Float.TYPE && var4 != Float.class) {
                           if(var4 != Double.TYPE && var4 != Double.class) {
                              if(var4 == Pointer.class) {
                                 this.setPointer(var1, (Pointer)var3);
                              } else if(var4 == String.class) {
                                 this.setPointer(var1, (Pointer)var3);
                              } else if(var4 == WString.class) {
                                 this.setPointer(var1, (Pointer)var3);
                              } else if(Structure.class.isAssignableFrom(var4)) {
                                 Structure var5 = (Structure)var3;
                                 if(Structure.ByReference.class.isAssignableFrom(var4)) {
                                    this.setPointer(var1, var5 == null?null:var5.getPointer());
                                    if(var5 != null) {
                                       var5.autoWrite();
                                    }
                                 } else {
                                    var5.useMemory(this, (int)var1);
                                    var5.write();
                                 }
                              } else if(Callback.class.isAssignableFrom(var4)) {
                                 this.setPointer(var1, CallbackReference.getFunctionPointer((Callback)var3));
                              } else if(Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(var4)) {
                                 Pointer var8 = var3 == null?null:Native.getDirectBufferPointer((Buffer)var3);
                                 this.setPointer(var1, var8);
                              } else if(NativeMapped.class.isAssignableFrom(var4)) {
                                 NativeMappedConverter var7 = NativeMappedConverter.getInstance(var4);
                                 Class var6 = var7.nativeType();
                                 this.setValue(var1, var7.toNative(var3, new ToNativeContext()), var6);
                              } else {
                                 if(!var4.isArray()) {
                                    throw new IllegalArgumentException("Writing " + var4 + " to memory is not supported");
                                 }

                                 this.setArrayValue(var1, var3, var4.getComponentType());
                              }
                           } else {
                              this.setDouble(var1, var3 == null?0.0D:((Double)var3).doubleValue());
                           }
                        } else {
                           this.setFloat(var1, var3 == null?0.0F:((Float)var3).floatValue());
                        }
                     } else {
                        this.setLong(var1, var3 == null?0L:((Long)var3).longValue());
                     }
                  } else {
                     this.setInt(var1, var3 == null?0:((Integer)var3).intValue());
                  }
               } else {
                  this.setChar(var1, var3 == null?'\u0000':((Character)var3).charValue());
               }
            } else {
               this.setShort(var1, var3 == null?0:((Short)var3).shortValue());
            }
         } else {
            this.setByte(var1, var3 == null?0:((Byte)var3).byteValue());
         }
      } else {
         this.setInt(var1, Boolean.TRUE.equals(var3)?-1:0);
      }

   }

   private void setArrayValue(long var1, Object var3, Class var4) {
      if(var4 == Byte.TYPE) {
         byte[] var5 = (byte[])((byte[])var3);
         this.write(var1, (byte[])var5, 0, var5.length);
      } else if(var4 == Short.TYPE) {
         short[] var11 = (short[])((short[])var3);
         this.write(var1, (short[])var11, 0, var11.length);
      } else if(var4 == Character.TYPE) {
         char[] var12 = (char[])((char[])var3);
         this.write(var1, (char[])var12, 0, var12.length);
      } else if(var4 == Integer.TYPE) {
         int[] var13 = (int[])((int[])var3);
         this.write(var1, (int[])var13, 0, var13.length);
      } else if(var4 == Long.TYPE) {
         long[] var14 = (long[])((long[])var3);
         this.write(var1, (long[])var14, 0, var14.length);
      } else if(var4 == Float.TYPE) {
         float[] var16 = (float[])((float[])var3);
         this.write(var1, (float[])var16, 0, var16.length);
      } else if(var4 == Double.TYPE) {
         double[] var17 = (double[])((double[])var3);
         this.write(var1, (double[])var17, 0, var17.length);
      } else if(Pointer.class.isAssignableFrom(var4)) {
         Pointer[] var19 = (Pointer[])((Pointer[])var3);
         this.write(var1, (Pointer[])var19, 0, var19.length);
      } else if(Structure.class.isAssignableFrom(var4)) {
         Structure[] var20 = (Structure[])((Structure[])var3);
         if(Structure.ByReference.class.isAssignableFrom(var4)) {
            Pointer[] var6 = new Pointer[var20.length];

            for(int var7 = 0; var7 < var20.length; ++var7) {
               if(var20[var7] == null) {
                  var6[var7] = null;
               } else {
                  var6[var7] = var20[var7].getPointer();
                  var20[var7].write();
               }
            }

            this.write(var1, (Pointer[])var6, 0, var6.length);
         } else {
            for(int var15 = 0; var15 < var20.length; ++var15) {
               if(var20[var15] == null) {
                  var20[var15] = Structure.newInstance(var4);
               }

               var20[var15].useMemory(this, (int)(var1 + (long)(var15 * var20[var15].size())));
               var20[var15].write();
            }
         }
      } else {
         if(!NativeMapped.class.isAssignableFrom(var4)) {
            throw new IllegalArgumentException("Writing array of " + var4 + " to memory not supported");
         }

         NativeMapped[] var22 = (NativeMapped[])((NativeMapped[])var3);
         NativeMappedConverter var18 = NativeMappedConverter.getInstance(var4);
         Class var21 = var18.nativeType();
         int var8 = Native.getNativeSize(var3.getClass(), var3) / var22.length;

         for(int var9 = 0; var9 < var22.length; ++var9) {
            Object var10 = var18.toNative(var22[var9], new ToNativeContext());
            this.setValue(var1 + (long)(var9 * var8), var10, var21);
         }
      }

   }

   public void setMemory(long var1, long var3, byte var5) {
      Native.setMemory(this.peer + var1, var3, var5);
   }

   public void setByte(long var1, byte var3) {
      Native.setByte(this.peer + var1, var3);
   }

   public void setShort(long var1, short var3) {
      Native.setShort(this.peer + var1, var3);
   }

   public void setChar(long var1, char var3) {
      Native.setChar(this.peer + var1, var3);
   }

   public void setInt(long var1, int var3) {
      Native.setInt(this.peer + var1, var3);
   }

   public void setLong(long var1, long var3) {
      Native.setLong(this.peer + var1, var3);
   }

   public void setNativeLong(long var1, NativeLong var3) {
      if(NativeLong.SIZE == 8) {
         this.setLong(var1, var3.longValue());
      } else {
         this.setInt(var1, var3.intValue());
      }

   }

   public void setFloat(long var1, float var3) {
      Native.setFloat(this.peer + var1, var3);
   }

   public void setDouble(long var1, double var3) {
      Native.setDouble(this.peer + var1, var3);
   }

   public void setPointer(long var1, Pointer var3) {
      Native.setPointer(this.peer + var1, var3 != null?var3.peer:0L);
   }

   public void setString(long var1, String var3, boolean var4) {
      Native.setString(this.peer + var1, var3, var4);
   }

   public void setString(long var1, String var3) {
      byte[] var4 = Native.getBytes(var3);
      this.write(var1, (byte[])var4, 0, var4.length);
      this.setByte(var1 + (long)var4.length, (byte)0);
   }

   public String toString() {
      return "native@0x" + Long.toHexString(this.peer);
   }

   public static long nativeValue(Pointer var0) {
      return var0.peer;
   }

   public static void nativeValue(Pointer var0, long var1) {
      var0.peer = var1;
   }

   static {
      if((SIZE = Native.POINTER_SIZE) == 0) {
         throw new Error("Native library not initialized");
      } else {
         NULL = null;
      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class Opaque extends Pointer {
      private final String MSG;

      private Opaque(long var1) {
         super(var1);
         this.MSG = "This pointer is opaque: " + this;
      }

      public long indexOf(long var1, byte var3) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void read(long var1, byte[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void read(long var1, char[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void read(long var1, short[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void read(long var1, int[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void read(long var1, long[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void read(long var1, float[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void read(long var1, double[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void write(long var1, byte[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void write(long var1, char[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void write(long var1, short[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void write(long var1, int[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void write(long var1, long[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void write(long var1, float[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void write(long var1, double[] var3, int var4, int var5) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public byte getByte(long var1) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public char getChar(long var1) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public short getShort(long var1) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public int getInt(long var1) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public long getLong(long var1) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public float getFloat(long var1) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public double getDouble(long var1) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public Pointer getPointer(long var1) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public String getString(long var1, boolean var3) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void setByte(long var1, byte var3) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void setChar(long var1, char var3) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void setShort(long var1, short var3) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void setInt(long var1, int var3) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void setLong(long var1, long var3) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void setFloat(long var1, float var3) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void setDouble(long var1, double var3) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void setPointer(long var1, Pointer var3) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public void setString(long var1, String var3, boolean var4) {
         throw new UnsupportedOperationException(this.MSG);
      }

      public String toString() {
         return "opaque@0x" + Long.toHexString(this.peer);
      }

      // $FF: synthetic method
      Opaque(long var1, Pointer.SyntheticClass_1 var3) {
         this(var1);
      }
   }
}
