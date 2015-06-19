package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.TreeTraverser;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.Iterator;

@Beta
@GwtCompatible(
   emulated = true
)
public abstract class BinaryTreeTraverser<T> extends TreeTraverser<T> {
   public BinaryTreeTraverser() {
   }

   public abstract Optional<T> leftChild(T var1);

   public abstract Optional<T> rightChild(T var1);

   public final Iterable<T> children(final T var1) {
      Preconditions.checkNotNull(var1);
      return new FluentIterable() {
         public Iterator<T> iterator() {
            return new AbstractIterator() {
               boolean doneLeft;
               boolean doneRight;

               protected T computeNext() {
                  Optional var1x;
                  if(!this.doneLeft) {
                     this.doneLeft = true;
                     var1x = BinaryTreeTraverser.this.leftChild(var1);
                     if(var1x.isPresent()) {
                        return var1x.get();
                     }
                  }

                  if(!this.doneRight) {
                     this.doneRight = true;
                     var1x = BinaryTreeTraverser.this.rightChild(var1);
                     if(var1x.isPresent()) {
                        return var1x.get();
                     }
                  }

                  return this.endOfData();
               }
            };
         }
      };
   }

   UnmodifiableIterator<T> preOrderIterator(T var1) {
      return new BinaryTreeTraverser.PreOrderIterator(var1);
   }

   UnmodifiableIterator<T> postOrderIterator(T var1) {
      return new BinaryTreeTraverser.PostOrderIterator(var1);
   }

   public final FluentIterable<T> inOrderTraversal(final T var1) {
      Preconditions.checkNotNull(var1);
      return new FluentIterable() {
         public UnmodifiableIterator<T> iterator() {
            return BinaryTreeTraverser.this.new InOrderIterator(var1);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Iterator iterator() {
            return this.iterator();
         }
      };
   }

   private static <T> void pushIfPresent(Deque<T> var0, Optional<T> var1) {
      if(var1.isPresent()) {
         var0.addLast(var1.get());
      }

   }

   private final class InOrderIterator extends AbstractIterator<T> {
      private final Deque<T> stack = new ArrayDeque();
      private final BitSet hasExpandedLeft = new BitSet();

      InOrderIterator(T var1) {
         this.stack.addLast(var2);
      }

      protected T computeNext() {
         while(!this.stack.isEmpty()) {
            Object var1 = this.stack.getLast();
            if(this.hasExpandedLeft.get(this.stack.size() - 1)) {
               this.stack.removeLast();
               this.hasExpandedLeft.clear(this.stack.size());
               BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(var1));
               return var1;
            }

            this.hasExpandedLeft.set(this.stack.size() - 1);
            BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(var1));
         }

         return this.endOfData();
      }
   }

   private final class PostOrderIterator extends UnmodifiableIterator<T> {
      private final Deque<T> stack = new ArrayDeque();
      private final BitSet hasExpanded;

      PostOrderIterator(T var1) {
         this.stack.addLast(var2);
         this.hasExpanded = new BitSet();
      }

      public boolean hasNext() {
         return !this.stack.isEmpty();
      }

      public T next() {
         while(true) {
            Object var1 = this.stack.getLast();
            boolean var2 = this.hasExpanded.get(this.stack.size() - 1);
            if(var2) {
               this.stack.removeLast();
               this.hasExpanded.clear(this.stack.size());
               return var1;
            }

            this.hasExpanded.set(this.stack.size() - 1);
            BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(var1));
            BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(var1));
         }
      }
   }

   private final class PreOrderIterator extends UnmodifiableIterator<T> implements PeekingIterator<T> {
      private final Deque<T> stack = new ArrayDeque();

      PreOrderIterator(T var1) {
         this.stack.addLast(var2);
      }

      public boolean hasNext() {
         return !this.stack.isEmpty();
      }

      public T next() {
         Object var1 = this.stack.removeLast();
         BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(var1));
         BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(var1));
         return var1;
      }

      public T peek() {
         return this.stack.getLast();
      }
   }
}
