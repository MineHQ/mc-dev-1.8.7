package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.builder.DiffResult;

public interface Diffable<T> {
   DiffResult diff(T var1);
}
