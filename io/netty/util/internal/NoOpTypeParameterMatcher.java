package io.netty.util.internal;

import io.netty.util.internal.TypeParameterMatcher;

public final class NoOpTypeParameterMatcher extends TypeParameterMatcher {
   public NoOpTypeParameterMatcher() {
   }

   public boolean match(Object var1) {
      return true;
   }
}
