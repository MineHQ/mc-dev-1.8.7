package com.sun.jna;

import com.sun.jna.Callback;
import com.sun.jna.FromNativeContext;
import com.sun.jna.FromNativeConverter;
import com.sun.jna.IntegerType;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.NativeString;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.StructureReadContext;
import com.sun.jna.StructureWriteContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.Union;
import com.sun.jna.WString;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.zip.Adler32;

public abstract class Structure {
   private static final boolean REVERSE_FIELDS;
   private static final boolean REQUIRES_FIELD_ORDER;
   static final boolean isPPC;
   static final boolean isSPARC;
   static final boolean isARM;
   public static final int ALIGN_DEFAULT = 0;
   public static final int ALIGN_NONE = 1;
   public static final int ALIGN_GNUC = 2;
   public static final int ALIGN_MSVC = 3;
   static final int MAX_GNUC_ALIGNMENT;
   protected static final int CALCULATE_SIZE = -1;
   static final Map layoutInfo;
   private Pointer memory;
   private int size;
   private int alignType;
   private int structAlignment;
   private Map structFields;
   private final Map nativeStrings;
   private TypeMapper typeMapper;
   private long typeInfo;
   private List fieldOrder;
   private boolean autoRead;
   private boolean autoWrite;
   private Structure[] array;
   private static final ThreadLocal reads;
   private static final ThreadLocal busy;

   protected Structure() {
      this((Pointer)null);
   }

   protected Structure(TypeMapper var1) {
      this((Pointer)null, 0, var1);
   }

   protected Structure(Pointer var1) {
      this(var1, 0);
   }

   protected Structure(Pointer var1, int var2) {
      this(var1, var2, (TypeMapper)null);
   }

   protected Structure(Pointer var1, int var2, TypeMapper var3) {
      this.size = -1;
      this.nativeStrings = new HashMap();
      this.autoRead = true;
      this.autoWrite = true;
      this.setAlignType(var2);
      this.setTypeMapper(var3);
      if(var1 != null) {
         this.useMemory(var1);
      } else {
         this.allocateMemory(-1);
      }

   }

   Map fields() {
      return this.structFields;
   }

   TypeMapper getTypeMapper() {
      return this.typeMapper;
   }

   protected void setTypeMapper(TypeMapper var1) {
      if(var1 == null) {
         Class var2 = this.getClass().getDeclaringClass();
         if(var2 != null) {
            var1 = Native.getTypeMapper(var2);
         }
      }

      this.typeMapper = var1;
      this.size = -1;
      if(this.memory instanceof Structure.AutoAllocated) {
         this.memory = null;
      }

   }

   protected void setAlignType(int var1) {
      if(var1 == 0) {
         Class var2 = this.getClass().getDeclaringClass();
         if(var2 != null) {
            var1 = Native.getStructureAlignment(var2);
         }

         if(var1 == 0) {
            if(Platform.isWindows()) {
               var1 = 3;
            } else {
               var1 = 2;
            }
         }
      }

      this.alignType = var1;
      this.size = -1;
      if(this.memory instanceof Structure.AutoAllocated) {
         this.memory = null;
      }

   }

   protected Memory autoAllocate(int var1) {
      return new Structure.AutoAllocated(var1);
   }

   protected void useMemory(Pointer var1) {
      this.useMemory(var1, 0);
   }

   protected void useMemory(Pointer var1, int var2) {
      try {
         this.memory = var1.share((long)var2);
         if(this.size == -1) {
            this.size = this.calculateSize(false);
         }

         if(this.size != -1) {
            this.memory = var1.share((long)var2, (long)this.size);
         }

         this.array = null;
      } catch (IndexOutOfBoundsException var4) {
         throw new IllegalArgumentException("Structure exceeds provided memory bounds");
      }
   }

   protected void ensureAllocated() {
      this.ensureAllocated(false);
   }

   private void ensureAllocated(boolean var1) {
      if(this.memory == null) {
         this.allocateMemory(var1);
      } else if(this.size == -1) {
         this.size = this.calculateSize(true, var1);
      }

   }

   protected void allocateMemory() {
      this.allocateMemory(false);
   }

   private void allocateMemory(boolean var1) {
      this.allocateMemory(this.calculateSize(true, var1));
   }

   protected void allocateMemory(int var1) {
      if(var1 == -1) {
         var1 = this.calculateSize(false);
      } else if(var1 <= 0) {
         throw new IllegalArgumentException("Structure size must be greater than zero: " + var1);
      }

      if(var1 != -1) {
         if(this.memory == null || this.memory instanceof Structure.AutoAllocated) {
            this.memory = this.autoAllocate(var1);
         }

         this.size = var1;
      }

   }

   public int size() {
      this.ensureAllocated();
      if(this.size == -1) {
         this.size = this.calculateSize(true);
      }

      return this.size;
   }

   public void clear() {
      this.memory.clear((long)this.size());
   }

   public Pointer getPointer() {
      this.ensureAllocated();
      return this.memory;
   }

   static Set busy() {
      return (Set)busy.get();
   }

   static Map reading() {
      return (Map)reads.get();
   }

   public void read() {
      this.ensureAllocated();
      if(!busy().contains(this)) {
         busy().add(this);
         if(this instanceof Structure.ByReference) {
            reading().put(this.getPointer(), this);
         }

         try {
            Iterator var1 = this.fields().values().iterator();

            while(var1.hasNext()) {
               this.readField((Structure.StructField)var1.next());
            }
         } finally {
            busy().remove(this);
            if(reading().get(this.getPointer()) == this) {
               reading().remove(this.getPointer());
            }

         }

      }
   }

   protected int fieldOffset(String var1) {
      this.ensureAllocated();
      Structure.StructField var2 = (Structure.StructField)this.fields().get(var1);
      if(var2 == null) {
         throw new IllegalArgumentException("No such field: " + var1);
      } else {
         return var2.offset;
      }
   }

   public Object readField(String var1) {
      this.ensureAllocated();
      Structure.StructField var2 = (Structure.StructField)this.fields().get(var1);
      if(var2 == null) {
         throw new IllegalArgumentException("No such field: " + var1);
      } else {
         return this.readField(var2);
      }
   }

   Object getField(Structure.StructField var1) {
      try {
         return var1.field.get(this);
      } catch (Exception var3) {
         throw new Error("Exception reading field \'" + var1.name + "\' in " + this.getClass() + ": " + var3);
      }
   }

   void setField(Structure.StructField var1, Object var2) {
      this.setField(var1, var2, false);
   }

   void setField(Structure.StructField var1, Object var2, boolean var3) {
      try {
         var1.field.set(this, var2);
      } catch (IllegalAccessException var6) {
         int var5 = var1.field.getModifiers();
         if(Modifier.isFinal(var5)) {
            if(var3) {
               throw new UnsupportedOperationException("This VM does not support Structures with final fields (field \'" + var1.name + "\' within " + this.getClass() + ")");
            } else {
               throw new UnsupportedOperationException("Attempt to write to read-only field \'" + var1.name + "\' within " + this.getClass());
            }
         } else {
            throw new Error("Unexpectedly unable to write to field \'" + var1.name + "\' within " + this.getClass() + ": " + var6);
         }
      }
   }

   static Structure updateStructureByReference(Class var0, Structure var1, Pointer var2) {
      if(var2 == null) {
         var1 = null;
      } else {
         if(var1 == null || !var2.equals(var1.getPointer())) {
            Structure var3 = (Structure)reading().get(var2);
            if(var3 != null && var0.equals(var3.getClass())) {
               var1 = var3;
            } else {
               var1 = newInstance(var0);
               var1.useMemory(var2);
            }
         }

         var1.autoRead();
      }

      return var1;
   }

   Object readField(Structure.StructField var1) {
      int var2 = var1.offset;
      Class var3 = var1.type;
      FromNativeConverter var4 = var1.readConverter;
      if(var4 != null) {
         var3 = var4.nativeType();
      }

      Object var5 = !Structure.class.isAssignableFrom(var3) && !Callback.class.isAssignableFrom(var3) && (!Platform.HAS_BUFFERS || !Buffer.class.isAssignableFrom(var3)) && !Pointer.class.isAssignableFrom(var3) && !NativeMapped.class.isAssignableFrom(var3) && !var3.isArray()?null:this.getField(var1);
      Object var6 = this.memory.getValue((long)var2, var3, var5);
      if(var4 != null) {
         var6 = var4.fromNative(var6, var1.context);
      }

      this.setField(var1, var6, true);
      return var6;
   }

   public void write() {
      this.ensureAllocated();
      if(this instanceof Structure.ByValue) {
         this.getTypeInfo();
      }

      if(!busy().contains(this)) {
         busy().add(this);

         try {
            Iterator var1 = this.fields().values().iterator();

            while(var1.hasNext()) {
               Structure.StructField var2 = (Structure.StructField)var1.next();
               if(!var2.isVolatile) {
                  this.writeField(var2);
               }
            }
         } finally {
            busy().remove(this);
         }

      }
   }

   public void writeField(String var1) {
      this.ensureAllocated();
      Structure.StructField var2 = (Structure.StructField)this.fields().get(var1);
      if(var2 == null) {
         throw new IllegalArgumentException("No such field: " + var1);
      } else {
         this.writeField(var2);
      }
   }

   public void writeField(String var1, Object var2) {
      this.ensureAllocated();
      Structure.StructField var3 = (Structure.StructField)this.fields().get(var1);
      if(var3 == null) {
         throw new IllegalArgumentException("No such field: " + var1);
      } else {
         this.setField(var3, var2);
         this.writeField(var3);
      }
   }

   void writeField(Structure.StructField var1) {
      if(!var1.isReadOnly) {
         int var2 = var1.offset;
         Object var3 = this.getField(var1);
         Class var4 = var1.type;
         ToNativeConverter var5 = var1.writeConverter;
         if(var5 != null) {
            var3 = var5.toNative(var3, new StructureWriteContext(this, var1.field));
            var4 = var5.nativeType();
         }

         if(String.class == var4 || WString.class == var4) {
            boolean var6 = var4 == WString.class;
            if(var3 != null) {
               NativeString var7 = new NativeString(var3.toString(), var6);
               this.nativeStrings.put(var1.name, var7);
               var3 = var7.getPointer();
            } else {
               var3 = null;
               this.nativeStrings.remove(var1.name);
            }
         }

         try {
            this.memory.setValue((long)var2, var3, var4);
         } catch (IllegalArgumentException var8) {
            String var9 = "Structure field \"" + var1.name + "\" was declared as " + var1.type + (var1.type == var4?"":" (native type " + var4 + ")") + ", which is not supported within a Structure";
            throw new IllegalArgumentException(var9);
         }
      }
   }

   private boolean hasFieldOrder() {
      synchronized(this) {
         return this.fieldOrder != null;
      }
   }

   protected List getFieldOrder() {
      synchronized(this) {
         if(this.fieldOrder == null) {
            this.fieldOrder = new ArrayList();
         }

         return this.fieldOrder;
      }
   }

   protected void setFieldOrder(String[] var1) {
      this.getFieldOrder().addAll(Arrays.asList(var1));
      this.size = -1;
      if(this.memory instanceof Structure.AutoAllocated) {
         this.memory = null;
      }

   }

   protected void sortFields(List var1, List var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         String var4 = (String)var2.get(var3);

         for(int var5 = 0; var5 < var1.size(); ++var5) {
            Field var6 = (Field)var1.get(var5);
            if(var4.equals(var6.getName())) {
               Collections.swap(var1, var3, var5);
               break;
            }
         }
      }

   }

   protected List getFields(boolean var1) {
      ArrayList var2 = new ArrayList();

      for(Class var3 = this.getClass(); !var3.equals(Structure.class); var3 = var3.getSuperclass()) {
         ArrayList var4 = new ArrayList();
         Field[] var5 = var3.getDeclaredFields();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            int var7 = var5[var6].getModifiers();
            if(!Modifier.isStatic(var7) && Modifier.isPublic(var7)) {
               var4.add(var5[var6]);
            }
         }

         if(REVERSE_FIELDS) {
            Collections.reverse(var4);
         }

         var2.addAll(0, var4);
      }

      if(REQUIRES_FIELD_ORDER || this.hasFieldOrder()) {
         List var8 = this.getFieldOrder();
         if(var8.size() < var2.size()) {
            if(var1) {
               throw new Error("This VM does not store fields in a predictable order; you must use Structure.setFieldOrder to explicitly indicate the field order: " + System.getProperty("java.vendor") + ", " + System.getProperty("java.version"));
            }

            return null;
         }

         this.sortFields(var2, var8);
      }

      return var2;
   }

   private synchronized boolean fieldOrderMatch(List var1) {
      return this.fieldOrder == var1 || this.fieldOrder != null && this.fieldOrder.equals(var1);
   }

   private int calculateSize(boolean var1) {
      return this.calculateSize(var1, false);
   }

   int calculateSize(boolean var1, boolean var2) {
      boolean var4 = true;
      Map var5 = layoutInfo;
      Structure.LayoutInfo var3;
      synchronized(layoutInfo) {
         var3 = (Structure.LayoutInfo)layoutInfo.get(this.getClass());
      }

      if(var3 == null || this.alignType != var3.alignType || this.typeMapper != var3.typeMapper || !this.fieldOrderMatch(var3.fieldOrder)) {
         var3 = this.deriveLayout(var1, var2);
         var4 = false;
      }

      if(var3 != null) {
         this.structAlignment = var3.alignment;
         this.structFields = var3.fields;
         var3.alignType = this.alignType;
         var3.typeMapper = this.typeMapper;
         var3.fieldOrder = this.fieldOrder;
         if(!var3.variable) {
            var5 = layoutInfo;
            synchronized(layoutInfo) {
               layoutInfo.put(this.getClass(), var3);
            }
         }

         if(var4) {
            this.initializeFields();
         }

         return var3.size;
      } else {
         return -1;
      }
   }

   private Structure.LayoutInfo deriveLayout(boolean var1, boolean var2) {
      Structure.LayoutInfo var3 = new Structure.LayoutInfo(null);
      int var4 = 0;
      List var5 = this.getFields(var1);
      if(var5 == null) {
         return null;
      } else {
         boolean var6 = true;

         for(Iterator var7 = var5.iterator(); var7.hasNext(); var6 = false) {
            Field var8 = (Field)var7.next();
            int var9 = var8.getModifiers();
            Class var10 = var8.getType();
            if(var10.isArray()) {
               var3.variable = true;
            }

            Structure.StructField var11 = new Structure.StructField();
            var11.isVolatile = Modifier.isVolatile(var9);
            var11.isReadOnly = Modifier.isFinal(var9);
            if(var11.isReadOnly) {
               if(!Platform.RO_FIELDS) {
                  throw new IllegalArgumentException("This VM does not support read-only fields (field \'" + var8.getName() + "\' within " + this.getClass() + ")");
               }

               var8.setAccessible(true);
            }

            var11.field = var8;
            var11.name = var8.getName();
            var11.type = var10;
            if(Callback.class.isAssignableFrom(var10) && !var10.isInterface()) {
               throw new IllegalArgumentException("Structure Callback field \'" + var8.getName() + "\' must be an interface");
            }

            if(var10.isArray() && Structure.class.equals(var10.getComponentType())) {
               String var21 = "Nested Structure arrays must use a derived Structure type so that the size of the elements can be determined";
               throw new IllegalArgumentException(var21);
            }

            boolean var12 = true;
            if(Modifier.isPublic(var8.getModifiers())) {
               Object var13 = this.getField(var11);
               if(var13 == null && var10.isArray()) {
                  if(var1) {
                     throw new IllegalStateException("Array fields must be initialized");
                  }

                  return null;
               }

               Class var14 = var10;
               if(NativeMapped.class.isAssignableFrom(var10)) {
                  NativeMappedConverter var22 = NativeMappedConverter.getInstance(var10);
                  var14 = var22.nativeType();
                  var11.writeConverter = var22;
                  var11.readConverter = var22;
                  var11.context = new StructureReadContext(this, var8);
               } else if(this.typeMapper != null) {
                  ToNativeConverter var15 = this.typeMapper.getToNativeConverter(var10);
                  FromNativeConverter var16 = this.typeMapper.getFromNativeConverter(var10);
                  if(var15 != null && var16 != null) {
                     var13 = var15.toNative(var13, new StructureWriteContext(this, var11.field));
                     var14 = var13 != null?var13.getClass():Pointer.class;
                     var11.writeConverter = var15;
                     var11.readConverter = var16;
                     var11.context = new StructureReadContext(this, var8);
                  } else if(var15 != null || var16 != null) {
                     String var17 = "Structures require bidirectional type conversion for " + var10;
                     throw new IllegalArgumentException(var17);
                  }
               }

               if(var13 == null) {
                  var13 = this.initializeField(var11, var10);
               }

               int var20;
               try {
                  var11.size = this.getNativeSize(var14, var13);
                  var20 = this.getNativeAlignment(var14, var13, var6);
               } catch (IllegalArgumentException var18) {
                  if(!var1 && this.typeMapper == null) {
                     return null;
                  }

                  String var23 = "Invalid Structure field in " + this.getClass() + ", field name \'" + var11.name + "\', " + var11.type + ": " + var18.getMessage();
                  throw new IllegalArgumentException(var23);
               }

               var3.alignment = Math.max(var3.alignment, var20);
               if(var4 % var20 != 0) {
                  var4 += var20 - var4 % var20;
               }

               var11.offset = var4;
               var4 += var11.size;
               var3.fields.put(var11.name, var11);
            }
         }

         if(var4 > 0) {
            int var19 = this.calculateAlignedSize(var4, var3.alignment);
            if(this instanceof Structure.ByValue && !var2) {
               this.getTypeInfo();
            }

            if(this.memory != null && !(this.memory instanceof Structure.AutoAllocated)) {
               this.memory = this.memory.share(0L, (long)var19);
            }

            var3.size = var19;
            return var3;
         } else {
            throw new IllegalArgumentException("Structure " + this.getClass() + " has unknown size (ensure " + "all fields are public)");
         }
      }
   }

   private void initializeFields() {
      Iterator var1 = this.fields().values().iterator();

      while(var1.hasNext()) {
         Structure.StructField var2 = (Structure.StructField)var1.next();
         this.initializeField(var2, var2.type);
      }

   }

   private Object initializeField(Structure.StructField var1, Class var2) {
      Object var3 = null;
      if(Structure.class.isAssignableFrom(var2) && !Structure.ByReference.class.isAssignableFrom(var2)) {
         try {
            var3 = newInstance(var2);
            this.setField(var1, var3);
         } catch (IllegalArgumentException var6) {
            String var5 = "Can\'t determine size of nested structure: " + var6.getMessage();
            throw new IllegalArgumentException(var5);
         }
      } else if(NativeMapped.class.isAssignableFrom(var2)) {
         NativeMappedConverter var4 = NativeMappedConverter.getInstance(var2);
         var3 = var4.defaultValue();
         this.setField(var1, var3);
      }

      return var3;
   }

   int calculateAlignedSize(int var1) {
      return this.calculateAlignedSize(var1, this.structAlignment);
   }

   private int calculateAlignedSize(int var1, int var2) {
      if(this.alignType != 1 && var1 % var2 != 0) {
         var1 += var2 - var1 % var2;
      }

      return var1;
   }

   protected int getStructAlignment() {
      if(this.size == -1) {
         this.calculateSize(true);
      }

      return this.structAlignment;
   }

   protected int getNativeAlignment(Class var1, Object var2, boolean var3) {
      boolean var4 = true;
      if(NativeMapped.class.isAssignableFrom(var1)) {
         NativeMappedConverter var5 = NativeMappedConverter.getInstance(var1);
         var1 = var5.nativeType();
         var2 = var5.toNative(var2, new ToNativeContext());
      }

      int var7 = Native.getNativeSize(var1, var2);
      int var6;
      if(!var1.isPrimitive() && Long.class != var1 && Integer.class != var1 && Short.class != var1 && Character.class != var1 && Byte.class != var1 && Boolean.class != var1 && Float.class != var1 && Double.class != var1) {
         if(Pointer.class != var1 && (!Platform.HAS_BUFFERS || !Buffer.class.isAssignableFrom(var1)) && !Callback.class.isAssignableFrom(var1) && WString.class != var1 && String.class != var1) {
            if(Structure.class.isAssignableFrom(var1)) {
               if(Structure.ByReference.class.isAssignableFrom(var1)) {
                  var6 = Pointer.SIZE;
               } else {
                  if(var2 == null) {
                     var2 = newInstance(var1);
                  }

                  var6 = ((Structure)var2).getStructAlignment();
               }
            } else {
               if(!var1.isArray()) {
                  throw new IllegalArgumentException("Type " + var1 + " has unknown " + "native alignment");
               }

               var6 = this.getNativeAlignment(var1.getComponentType(), (Object)null, var3);
            }
         } else {
            var6 = Pointer.SIZE;
         }
      } else {
         var6 = var7;
      }

      if(this.alignType == 1) {
         var6 = 1;
      } else if(this.alignType == 3) {
         var6 = Math.min(8, var6);
      } else if(this.alignType == 2 && (!var3 || !Platform.isMac() || !isPPC)) {
         var6 = Math.min(MAX_GNUC_ALIGNMENT, var6);
      }

      return var6;
   }

   public String toString() {
      return this.toString(Boolean.getBoolean("jna.dump_memory"));
   }

   public String toString(boolean var1) {
      return this.toString(0, true, true);
   }

   private String format(Class var1) {
      String var2 = var1.getName();
      int var3 = var2.lastIndexOf(".");
      return var2.substring(var3 + 1);
   }

   private String toString(int var1, boolean var2, boolean var3) {
      this.ensureAllocated();
      String var4 = System.getProperty("line.separator");
      String var5 = this.format(this.getClass()) + "(" + this.getPointer() + ")";
      if(!(this.getPointer() instanceof Memory)) {
         var5 = var5 + " (" + this.size() + " bytes)";
      }

      String var6 = "";

      for(int var7 = 0; var7 < var1; ++var7) {
         var6 = var6 + "  ";
      }

      String var13 = var4;
      if(!var2) {
         var13 = "...}";
      } else {
         Iterator var8 = this.fields().values().iterator();

         while(var8.hasNext()) {
            Structure.StructField var9 = (Structure.StructField)var8.next();
            Object var10 = this.getField(var9);
            String var11 = this.format(var9.type);
            String var12 = "";
            var13 = var13 + var6;
            if(var9.type.isArray() && var10 != null) {
               var11 = this.format(var9.type.getComponentType());
               var12 = "[" + Array.getLength(var10) + "]";
            }

            var13 = var13 + "  " + var11 + " " + var9.name + var12 + "@" + Integer.toHexString(var9.offset);
            if(var10 instanceof Structure) {
               var10 = ((Structure)var10).toString(var1 + 1, !(var10 instanceof Structure.ByReference), var3);
            }

            var13 = var13 + "=";
            if(var10 instanceof Long) {
               var13 = var13 + Long.toHexString(((Long)var10).longValue());
            } else if(var10 instanceof Integer) {
               var13 = var13 + Integer.toHexString(((Integer)var10).intValue());
            } else if(var10 instanceof Short) {
               var13 = var13 + Integer.toHexString(((Short)var10).shortValue());
            } else if(var10 instanceof Byte) {
               var13 = var13 + Integer.toHexString(((Byte)var10).byteValue());
            } else {
               var13 = var13 + String.valueOf(var10).trim();
            }

            var13 = var13 + var4;
            if(!var8.hasNext()) {
               var13 = var13 + var6 + "}";
            }
         }
      }

      if(var1 == 0 && var3) {
         boolean var14 = true;
         var13 = var13 + var4 + "memory dump" + var4;
         byte[] var15 = this.getPointer().getByteArray(0L, this.size());

         for(int var16 = 0; var16 < var15.length; ++var16) {
            if(var16 % 4 == 0) {
               var13 = var13 + "[";
            }

            if(var15[var16] >= 0 && var15[var16] < 16) {
               var13 = var13 + "0";
            }

            var13 = var13 + Integer.toHexString(var15[var16] & 255);
            if(var16 % 4 == 3 && var16 < var15.length - 1) {
               var13 = var13 + "]" + var4;
            }
         }

         var13 = var13 + "]";
      }

      return var5 + " {" + var13;
   }

   public Structure[] toArray(Structure[] var1) {
      this.ensureAllocated();
      int var3;
      if(this.memory instanceof Structure.AutoAllocated) {
         Memory var2 = (Memory)this.memory;
         var3 = var1.length * this.size();
         if(var2.size() < (long)var3) {
            this.useMemory(this.autoAllocate(var3));
         }
      }

      var1[0] = this;
      int var4 = this.size();

      for(var3 = 1; var3 < var1.length; ++var3) {
         var1[var3] = newInstance(this.getClass());
         var1[var3].useMemory(this.memory.share((long)(var3 * var4), (long)var4));
         var1[var3].read();
      }

      if(!(this instanceof Structure.ByValue)) {
         this.array = var1;
      }

      return var1;
   }

   public Structure[] toArray(int var1) {
      return this.toArray((Structure[])((Structure[])Array.newInstance(this.getClass(), var1)));
   }

   private Class baseClass() {
      return (this instanceof Structure.ByReference || this instanceof Structure.ByValue) && Structure.class.isAssignableFrom(this.getClass().getSuperclass())?this.getClass().getSuperclass():this.getClass();
   }

   public boolean equals(Object var1) {
      if(var1 == this) {
         return true;
      } else if(!(var1 instanceof Structure)) {
         return false;
      } else if(var1.getClass() != this.getClass() && ((Structure)var1).baseClass() != this.baseClass()) {
         return false;
      } else {
         Structure var2 = (Structure)var1;
         if(var2.getPointer().equals(this.getPointer())) {
            return true;
         } else if(var2.size() == this.size()) {
            this.clear();
            this.write();
            byte[] var3 = this.getPointer().getByteArray(0L, this.size());
            var2.clear();
            var2.write();
            byte[] var4 = var2.getPointer().getByteArray(0L, var2.size());
            return Arrays.equals(var3, var4);
         } else {
            return false;
         }
      }
   }

   public int hashCode() {
      this.clear();
      this.write();
      Adler32 var1 = new Adler32();
      var1.update(this.getPointer().getByteArray(0L, this.size()));
      return (int)var1.getValue();
   }

   protected void cacheTypeInfo(Pointer var1) {
      this.typeInfo = var1.peer;
   }

   protected Pointer getFieldTypeInfo(Structure.StructField var1) {
      Class var2 = var1.type;
      Object var3 = this.getField(var1);
      if(this.typeMapper != null) {
         ToNativeConverter var4 = this.typeMapper.getToNativeConverter(var2);
         if(var4 != null) {
            var2 = var4.nativeType();
            var3 = var4.toNative(var3, new ToNativeContext());
         }
      }

      return Structure.FFIType.get(var3, var2);
   }

   Pointer getTypeInfo() {
      Pointer var1 = getTypeInfo(this);
      this.cacheTypeInfo(var1);
      return var1;
   }

   public void setAutoSynch(boolean var1) {
      this.setAutoRead(var1);
      this.setAutoWrite(var1);
   }

   public void setAutoRead(boolean var1) {
      this.autoRead = var1;
   }

   public boolean getAutoRead() {
      return this.autoRead;
   }

   public void setAutoWrite(boolean var1) {
      this.autoWrite = var1;
   }

   public boolean getAutoWrite() {
      return this.autoWrite;
   }

   static Pointer getTypeInfo(Object var0) {
      return Structure.FFIType.get(var0);
   }

   public static Structure newInstance(Class var0) throws IllegalArgumentException {
      String var2;
      try {
         Structure var1 = (Structure)var0.newInstance();
         if(var1 instanceof Structure.ByValue) {
            var1.allocateMemory();
         }

         return var1;
      } catch (InstantiationException var3) {
         var2 = "Can\'t instantiate " + var0 + " (" + var3 + ")";
         throw new IllegalArgumentException(var2);
      } catch (IllegalAccessException var4) {
         var2 = "Instantiation of " + var0 + " not allowed, is it public? (" + var4 + ")";
         throw new IllegalArgumentException(var2);
      }
   }

   private static void structureArrayCheck(Structure[] var0) {
      Pointer var1 = var0[0].getPointer();
      int var2 = var0[0].size();

      for(int var3 = 1; var3 < var0.length; ++var3) {
         if(var0[var3].getPointer().peer != var1.peer + (long)(var2 * var3)) {
            String var4 = "Structure array elements must use contiguous memory (bad backing address at Structure array index " + var3 + ")";
            throw new IllegalArgumentException(var4);
         }
      }

   }

   public static void autoRead(Structure[] var0) {
      structureArrayCheck(var0);
      if(var0[0].array == var0) {
         var0[0].autoRead();
      } else {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            var0[var1].autoRead();
         }
      }

   }

   public void autoRead() {
      if(this.getAutoRead()) {
         this.read();
         if(this.array != null) {
            for(int var1 = 1; var1 < this.array.length; ++var1) {
               this.array[var1].autoRead();
            }
         }
      }

   }

   public static void autoWrite(Structure[] var0) {
      structureArrayCheck(var0);
      if(var0[0].array == var0) {
         var0[0].autoWrite();
      } else {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            var0[var1].autoWrite();
         }
      }

   }

   public void autoWrite() {
      if(this.getAutoWrite()) {
         this.write();
         if(this.array != null) {
            for(int var1 = 1; var1 < this.array.length; ++var1) {
               this.array[var1].autoWrite();
            }
         }
      }

   }

   protected int getNativeSize(Class var1, Object var2) {
      return Native.getNativeSize(var1, var2);
   }

   static {
      Field[] var0 = Structure.MemberOrder.class.getFields();
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.add(var0[var2].getName());
      }

      List var5 = Arrays.asList(Structure.MemberOrder.FIELDS);
      ArrayList var3 = new ArrayList(var5);
      Collections.reverse(var3);
      REVERSE_FIELDS = var1.equals(var3);
      REQUIRES_FIELD_ORDER = !var1.equals(var5) && !REVERSE_FIELDS;
      String var4 = System.getProperty("os.arch").toLowerCase();
      isPPC = "ppc".equals(var4) || "powerpc".equals(var4);
      isSPARC = "sparc".equals(var4);
      isARM = "arm".equals(var4);
      MAX_GNUC_ALIGNMENT = !isSPARC && (!isPPC && !isARM || !Platform.isLinux())?Native.LONG_SIZE:8;
      layoutInfo = new WeakHashMap();
      reads = new ThreadLocal() {
         protected synchronized Object initialValue() {
            return new HashMap();
         }
      };
      busy = new ThreadLocal() {
         protected synchronized Object initialValue() {
            return new null.2$StructureSet();
         }

         class 2$StructureSet extends AbstractCollection implements Set {
            private Structure[] elements;
            private int count;

            _$StructureSet/* $FF was: 2$StructureSet*/() {
            }

            private void ensureCapacity(int var1) {
               if(this.elements == null) {
                  this.elements = new Structure[var1 * 3 / 2];
               } else if(this.elements.length < var1) {
                  Structure[] var2 = new Structure[var1 * 3 / 2];
                  System.arraycopy(this.elements, 0, var2, 0, this.elements.length);
                  this.elements = var2;
               }

            }

            public int size() {
               return this.count;
            }

            public boolean contains(Object var1) {
               return this.indexOf(var1) != -1;
            }

            public boolean add(Object var1) {
               if(!this.contains(var1)) {
                  this.ensureCapacity(this.count + 1);
                  this.elements[this.count++] = (Structure)var1;
               }

               return true;
            }

            private int indexOf(Object var1) {
               Structure var2 = (Structure)var1;

               for(int var3 = 0; var3 < this.count; ++var3) {
                  Structure var4 = this.elements[var3];
                  if(var2 == var4 || var2.getClass() == var4.getClass() && var2.size() == var4.size() && var2.getPointer().equals(var4.getPointer())) {
                     return var3;
                  }
               }

               return -1;
            }

            public boolean remove(Object var1) {
               int var2 = this.indexOf(var1);
               if(var2 != -1) {
                  if(--this.count > 0) {
                     this.elements[var2] = this.elements[this.count];
                     this.elements[this.count] = null;
                  }

                  return true;
               } else {
                  return false;
               }
            }

            public Iterator iterator() {
               Structure[] var1 = new Structure[this.count];
               if(this.count > 0) {
                  System.arraycopy(this.elements, 0, var1, 0, this.count);
               }

               return Arrays.asList(var1).iterator();
            }
         }
      };
   }

   private class AutoAllocated extends Memory {
      public AutoAllocated(int var2) {
         super((long)var2);
         super.clear();
      }
   }

   static class FFIType extends Structure {
      private static Map typeInfoMap = new WeakHashMap();
      private static final int FFI_TYPE_STRUCT = 13;
      public Structure.FFIType.FFIType$size_t size;
      public short alignment;
      public short type = 13;
      public Pointer elements;

      private FFIType(Structure var1) {
         var1.ensureAllocated(true);
         Pointer[] var2;
         if(var1 instanceof Union) {
            Structure.StructField var3 = ((Union)var1).biggestField;
            var2 = new Pointer[]{get(var1.getField(var3), var3.type), null};
         } else {
            var2 = new Pointer[var1.fields().size() + 1];
            int var6 = 0;

            Structure.StructField var5;
            for(Iterator var4 = var1.fields().values().iterator(); var4.hasNext(); var2[var6++] = var1.getFieldTypeInfo(var5)) {
               var5 = (Structure.StructField)var4.next();
            }
         }

         this.init(var2);
      }

      private FFIType(Object var1, Class var2) {
         int var3 = Array.getLength(var1);
         Pointer[] var4 = new Pointer[var3 + 1];
         Pointer var5 = get((Object)null, var2.getComponentType());

         for(int var6 = 0; var6 < var3; ++var6) {
            var4[var6] = var5;
         }

         this.init(var4);
      }

      private void init(Pointer[] var1) {
         this.elements = new Memory((long)(Pointer.SIZE * var1.length));
         this.elements.write(0L, (Pointer[])var1, 0, var1.length);
         this.write();
      }

      static Pointer get(Object var0) {
         return var0 == null?Structure.FFIType.FFIType$FFITypes.ffi_type_pointer:(var0 instanceof Class?get((Object)null, (Class)var0):get(var0, var0.getClass()));
      }

      private static Pointer get(Object var0, Class var1) {
         TypeMapper var2 = Native.getTypeMapper(var1);
         if(var2 != null) {
            ToNativeConverter var3 = var2.getToNativeConverter(var1);
            if(var3 != null) {
               var1 = var3.nativeType();
            }
         }

         Map var8 = typeInfoMap;
         synchronized(typeInfoMap) {
            Object var4 = typeInfoMap.get(var1);
            if(var4 instanceof Pointer) {
               return (Pointer)var4;
            } else if(var4 instanceof Structure.FFIType) {
               return ((Structure.FFIType)var4).getPointer();
            } else if((!Platform.HAS_BUFFERS || !Buffer.class.isAssignableFrom(var1)) && !Callback.class.isAssignableFrom(var1)) {
               Structure.FFIType var5;
               if(Structure.class.isAssignableFrom(var1)) {
                  if(var0 == null) {
                     var0 = newInstance(var1);
                  }

                  if(Structure.ByReference.class.isAssignableFrom(var1)) {
                     typeInfoMap.put(var1, Structure.FFIType.FFIType$FFITypes.ffi_type_pointer);
                     return Structure.FFIType.FFIType$FFITypes.ffi_type_pointer;
                  } else {
                     var5 = new Structure.FFIType((Structure)var0);
                     typeInfoMap.put(var1, var5);
                     return var5.getPointer();
                  }
               } else if(NativeMapped.class.isAssignableFrom(var1)) {
                  NativeMappedConverter var9 = NativeMappedConverter.getInstance(var1);
                  return get(var9.toNative(var0, new ToNativeContext()), var9.nativeType());
               } else if(var1.isArray()) {
                  var5 = new Structure.FFIType(var0, var1);
                  typeInfoMap.put(var0, var5);
                  return var5.getPointer();
               } else {
                  throw new IllegalArgumentException("Unsupported Structure field type " + var1);
               }
            } else {
               typeInfoMap.put(var1, Structure.FFIType.FFIType$FFITypes.ffi_type_pointer);
               return Structure.FFIType.FFIType$FFITypes.ffi_type_pointer;
            }
         }
      }

      static {
         if(Native.POINTER_SIZE == 0) {
            throw new Error("Native library not initialized");
         } else if(Structure.FFIType.FFIType$FFITypes.ffi_type_void == null) {
            throw new Error("FFI types not initialized");
         } else {
            typeInfoMap.put(Void.TYPE, Structure.FFIType.FFIType$FFITypes.ffi_type_void);
            typeInfoMap.put(Void.class, Structure.FFIType.FFIType$FFITypes.ffi_type_void);
            typeInfoMap.put(Float.TYPE, Structure.FFIType.FFIType$FFITypes.ffi_type_float);
            typeInfoMap.put(Float.class, Structure.FFIType.FFIType$FFITypes.ffi_type_float);
            typeInfoMap.put(Double.TYPE, Structure.FFIType.FFIType$FFITypes.ffi_type_double);
            typeInfoMap.put(Double.class, Structure.FFIType.FFIType$FFITypes.ffi_type_double);
            typeInfoMap.put(Long.TYPE, Structure.FFIType.FFIType$FFITypes.ffi_type_sint64);
            typeInfoMap.put(Long.class, Structure.FFIType.FFIType$FFITypes.ffi_type_sint64);
            typeInfoMap.put(Integer.TYPE, Structure.FFIType.FFIType$FFITypes.ffi_type_sint32);
            typeInfoMap.put(Integer.class, Structure.FFIType.FFIType$FFITypes.ffi_type_sint32);
            typeInfoMap.put(Short.TYPE, Structure.FFIType.FFIType$FFITypes.ffi_type_sint16);
            typeInfoMap.put(Short.class, Structure.FFIType.FFIType$FFITypes.ffi_type_sint16);
            Pointer var0 = Native.WCHAR_SIZE == 2?Structure.FFIType.FFIType$FFITypes.ffi_type_uint16:Structure.FFIType.FFIType$FFITypes.ffi_type_uint32;
            typeInfoMap.put(Character.TYPE, var0);
            typeInfoMap.put(Character.class, var0);
            typeInfoMap.put(Byte.TYPE, Structure.FFIType.FFIType$FFITypes.ffi_type_sint8);
            typeInfoMap.put(Byte.class, Structure.FFIType.FFIType$FFITypes.ffi_type_sint8);
            typeInfoMap.put(Pointer.class, Structure.FFIType.FFIType$FFITypes.ffi_type_pointer);
            typeInfoMap.put(String.class, Structure.FFIType.FFIType$FFITypes.ffi_type_pointer);
            typeInfoMap.put(WString.class, Structure.FFIType.FFIType$FFITypes.ffi_type_pointer);
            typeInfoMap.put(Boolean.TYPE, Structure.FFIType.FFIType$FFITypes.ffi_type_uint32);
            typeInfoMap.put(Boolean.class, Structure.FFIType.FFIType$FFITypes.ffi_type_uint32);
         }
      }

      private static class FFIType$FFITypes {
         private static Pointer ffi_type_void;
         private static Pointer ffi_type_float;
         private static Pointer ffi_type_double;
         private static Pointer ffi_type_longdouble;
         private static Pointer ffi_type_uint8;
         private static Pointer ffi_type_sint8;
         private static Pointer ffi_type_uint16;
         private static Pointer ffi_type_sint16;
         private static Pointer ffi_type_uint32;
         private static Pointer ffi_type_sint32;
         private static Pointer ffi_type_uint64;
         private static Pointer ffi_type_sint64;
         private static Pointer ffi_type_pointer;

         private FFIType$FFITypes() {
         }
      }

      public static class FFIType$size_t extends IntegerType {
         public FFIType$size_t() {
            this(0L);
         }

         public FFIType$size_t(long var1) {
            super(Native.POINTER_SIZE, var1);
         }
      }
   }

   class StructField {
      public String name;
      public Class type;
      public Field field;
      public int size = -1;
      public int offset = -1;
      public boolean isVolatile;
      public boolean isReadOnly;
      public FromNativeConverter readConverter;
      public ToNativeConverter writeConverter;
      public FromNativeContext context;

      StructField() {
      }

      public String toString() {
         return this.name + "@" + this.offset + "[" + this.size + "] (" + this.type + ")";
      }
   }

   private class LayoutInfo {
      int size;
      int alignment;
      Map fields;
      int alignType;
      TypeMapper typeMapper;
      List fieldOrder;
      boolean variable;

      private LayoutInfo() {
         this.size = -1;
         this.alignment = 1;
         this.fields = Collections.synchronizedMap(new LinkedHashMap());
         this.alignType = 0;
      }

      // $FF: synthetic method
      LayoutInfo(Object var2) {
         this();
      }
   }

   private static class MemberOrder {
      private static final String[] FIELDS = new String[]{"first", "second", "middle", "penultimate", "last"};
      public int first;
      public int second;
      public int middle;
      public int penultimate;
      public int last;

      private MemberOrder() {
      }
   }

   public interface ByReference {
   }

   public interface ByValue {
   }
}
