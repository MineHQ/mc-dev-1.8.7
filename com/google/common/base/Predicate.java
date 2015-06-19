package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public interface Predicate<T> {
   boolean apply(@Nullable T var1);

   boolean equals(@Nullable Object var1);
}
