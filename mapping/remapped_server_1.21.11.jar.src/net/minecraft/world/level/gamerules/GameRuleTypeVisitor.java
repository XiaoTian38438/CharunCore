package net.minecraft.world.level.gamerules;

public interface GameRuleTypeVisitor {
  default <T> void visit(GameRule<T> paramGameRule) {}
  
  default void visitBoolean(GameRule<Boolean> paramGameRule) {}
  
  default void visitInteger(GameRule<Integer> paramGameRule) {}
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\gamerules\GameRuleTypeVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */