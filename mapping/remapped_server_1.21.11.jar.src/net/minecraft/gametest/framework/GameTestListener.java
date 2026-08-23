package net.minecraft.gametest.framework;

public interface GameTestListener {
  void testStructureLoaded(GameTestInfo paramGameTestInfo);
  
  void testPassed(GameTestInfo paramGameTestInfo, GameTestRunner paramGameTestRunner);
  
  void testFailed(GameTestInfo paramGameTestInfo, GameTestRunner paramGameTestRunner);
  
  void testAddedForRerun(GameTestInfo paramGameTestInfo1, GameTestInfo paramGameTestInfo2, GameTestRunner paramGameTestRunner);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */