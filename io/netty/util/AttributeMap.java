package io.netty.util;

import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public interface AttributeMap {
   <T> Attribute<T> attr(AttributeKey<T> var1);
}
