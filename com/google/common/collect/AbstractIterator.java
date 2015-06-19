package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import java.util.NoSuchElementException;

@GwtCompatible
public abstract class AbstractIterator<T> extends UnmodifiableIterator<T> {
   private AbstractIterator.State state;
   private T next;

   protected AbstractIterator() {
      this.state = AbstractIterator.State.NOT_READY;
   }

   protected abstract T computeNext();

   protected final T endOfData() {
      this.state = AbstractIterator.State.DONE;
      return null;
   }

   public final boolean hasNext() {
      Preconditions.checkState(this.state != AbstractIterator.State.FAILED);
      switch(AbstractIterator.SyntheticClass_1.$SwitchMap$com$google$common$collect$AbstractIterator$State[this.state.ordinal()]) {
      case 1:
         return false;
      case 2:
         return true;
      default:
         return this.tryToComputeNext();
      }
   }

   private boolean tryToComputeNext() {
      this.state = AbstractIterator.State.FAILED;
      this.next = this.computeNext();
      if(this.state != AbstractIterator.State.DONE) {
         this.state = AbstractIterator.State.READY;
         return true;
      } else {
         return false;
      }
   }

   public final T next() {
      if(!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         this.state = AbstractIterator.State.NOT_READY;
         Object var1 = this.next;
         this.next = null;
         return var1;
      }
   }

   public final T peek() {
      if(!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         return this.next;
      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$com$google$common$collect$AbstractIterator$State = new int[AbstractIterator.State.values().length];

      static {
         try {
            $SwitchMap$com$google$common$collect$AbstractIterator$State[AbstractIterator.State.DONE.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$google$common$collect$AbstractIterator$State[AbstractIterator.State.READY.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   private static enum State {
      READY,
      NOT_READY,
      DONE,
      FAILED;

      private State() {
      }
   }
}
