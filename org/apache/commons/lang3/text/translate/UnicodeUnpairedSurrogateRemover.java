package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.CodePointTranslator;

public class UnicodeUnpairedSurrogateRemover extends CodePointTranslator {
   public UnicodeUnpairedSurrogateRemover() {
   }

   public boolean translate(int var1, Writer var2) throws IOException {
      return var1 >= '\ud800' && var1 <= '\udfff';
   }
}
