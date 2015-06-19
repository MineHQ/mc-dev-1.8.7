package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.io.LineBuffer;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.LinkedList;
import java.util.Queue;

@Beta
public final class LineReader {
   private final Readable readable;
   private final Reader reader;
   private final char[] buf = new char[4096];
   private final CharBuffer cbuf;
   private final Queue<String> lines;
   private final LineBuffer lineBuf;

   public LineReader(Readable var1) {
      this.cbuf = CharBuffer.wrap(this.buf);
      this.lines = new LinkedList();
      this.lineBuf = new LineBuffer() {
         protected void handleLine(String var1, String var2) {
            LineReader.this.lines.add(var1);
         }
      };
      this.readable = (Readable)Preconditions.checkNotNull(var1);
      this.reader = var1 instanceof Reader?(Reader)var1:null;
   }

   public String readLine() throws IOException {
      while(true) {
         if(this.lines.peek() == null) {
            this.cbuf.clear();
            int var1 = this.reader != null?this.reader.read(this.buf, 0, this.buf.length):this.readable.read(this.cbuf);
            if(var1 != -1) {
               this.lineBuf.add(this.buf, 0, var1);
               continue;
            }

            this.lineBuf.finish();
         }

         return (String)this.lines.poll();
      }
   }
}
