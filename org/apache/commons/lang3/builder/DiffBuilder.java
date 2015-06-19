package org.apache.commons.lang3.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.Builder;
import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DiffBuilder implements Builder<DiffResult> {
   private final List<Diff<?>> diffs;
   private final boolean objectsTriviallyEqual;
   private final Object left;
   private final Object right;
   private final ToStringStyle style;

   public DiffBuilder(Object var1, Object var2, ToStringStyle var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("lhs cannot be null");
      } else if(var2 == null) {
         throw new IllegalArgumentException("rhs cannot be null");
      } else {
         this.diffs = new ArrayList();
         this.left = var1;
         this.right = var2;
         this.style = var3;
         this.objectsTriviallyEqual = var1 == var2 || var1.equals(var2);
      }
   }

   public DiffBuilder append(final String var1, final boolean var2, final boolean var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(var2 != var3) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Boolean getLeft() {
                  return Boolean.valueOf(var2);
               }

               public Boolean getRight() {
                  return Boolean.valueOf(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final boolean[] var2, final boolean[] var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Boolean[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Boolean[] getRight() {
                  return ArrayUtils.toObject(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final byte var2, final byte var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(var2 != var3) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Byte getLeft() {
                  return Byte.valueOf(var2);
               }

               public Byte getRight() {
                  return Byte.valueOf(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final byte[] var2, final byte[] var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Byte[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Byte[] getRight() {
                  return ArrayUtils.toObject(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final char var2, final char var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(var2 != var3) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Character getLeft() {
                  return Character.valueOf(var2);
               }

               public Character getRight() {
                  return Character.valueOf(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final char[] var2, final char[] var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Character[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Character[] getRight() {
                  return ArrayUtils.toObject(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final double var2, final double var4) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(Double.doubleToLongBits(var2) != Double.doubleToLongBits(var4)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Double getLeft() {
                  return Double.valueOf(var2);
               }

               public Double getRight() {
                  return Double.valueOf(var4);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final double[] var2, final double[] var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Double[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Double[] getRight() {
                  return ArrayUtils.toObject(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final float var2, final float var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(Float.floatToIntBits(var2) != Float.floatToIntBits(var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Float getLeft() {
                  return Float.valueOf(var2);
               }

               public Float getRight() {
                  return Float.valueOf(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final float[] var2, final float[] var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Float[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Float[] getRight() {
                  return ArrayUtils.toObject(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final int var2, final int var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(var2 != var3) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Integer getLeft() {
                  return Integer.valueOf(var2);
               }

               public Integer getRight() {
                  return Integer.valueOf(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final int[] var2, final int[] var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Integer[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Integer[] getRight() {
                  return ArrayUtils.toObject(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final long var2, final long var4) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(var2 != var4) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Long getLeft() {
                  return Long.valueOf(var2);
               }

               public Long getRight() {
                  return Long.valueOf(var4);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final long[] var2, final long[] var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Long[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Long[] getRight() {
                  return ArrayUtils.toObject(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final short var2, final short var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(var2 != var3) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Short getLeft() {
                  return Short.valueOf(var2);
               }

               public Short getRight() {
                  return Short.valueOf(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final short[] var2, final short[] var3) {
      if(var1 == null) {
         throw new IllegalArgumentException("Field name cannot be null");
      } else if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Short[] getLeft() {
                  return ArrayUtils.toObject(var2);
               }

               public Short[] getRight() {
                  return ArrayUtils.toObject(var3);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffBuilder append(final String var1, final Object var2, final Object var3) {
      if(this.objectsTriviallyEqual) {
         return this;
      } else if(var2 == var3) {
         return this;
      } else {
         Object var4;
         if(var2 != null) {
            var4 = var2;
         } else {
            var4 = var3;
         }

         if(var4.getClass().isArray()) {
            return var4 instanceof boolean[]?this.append(var1, (boolean[])((boolean[])var2), (boolean[])((boolean[])var3)):(var4 instanceof byte[]?this.append(var1, (byte[])((byte[])var2), (byte[])((byte[])var3)):(var4 instanceof char[]?this.append(var1, (char[])((char[])var2), (char[])((char[])var3)):(var4 instanceof double[]?this.append(var1, (double[])((double[])var2), (double[])((double[])var3)):(var4 instanceof float[]?this.append(var1, (float[])((float[])var2), (float[])((float[])var3)):(var4 instanceof int[]?this.append(var1, (int[])((int[])var2), (int[])((int[])var3)):(var4 instanceof long[]?this.append(var1, (long[])((long[])var2), (long[])((long[])var3)):(var4 instanceof short[]?this.append(var1, (short[])((short[])var2), (short[])((short[])var3)):this.append(var1, (Object[])((Object[])var2), (Object[])((Object[])var3)))))))));
         } else {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Object getLeft() {
                  return var2;
               }

               public Object getRight() {
                  return var3;
               }
            });
            return this;
         }
      }
   }

   public DiffBuilder append(final String var1, final Object[] var2, final Object[] var3) {
      if(this.objectsTriviallyEqual) {
         return this;
      } else {
         if(!Arrays.equals(var2, var3)) {
            this.diffs.add(new Diff(var1) {
               private static final long serialVersionUID = 1L;

               public Object[] getLeft() {
                  return var2;
               }

               public Object[] getRight() {
                  return var3;
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getRight() {
                  return this.getRight();
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object getLeft() {
                  return this.getLeft();
               }
            });
         }

         return this;
      }
   }

   public DiffResult build() {
      return new DiffResult(this.left, this.right, this.diffs, this.style);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object build() {
      return this.build();
   }
}
