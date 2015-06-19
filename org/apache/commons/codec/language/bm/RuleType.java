package org.apache.commons.codec.language.bm;

public enum RuleType {
   APPROX("approx"),
   EXACT("exact"),
   RULES("rules");

   private final String name;

   private RuleType(String var3) {
      this.name = var3;
   }

   public String getName() {
      return this.name;
   }
}
