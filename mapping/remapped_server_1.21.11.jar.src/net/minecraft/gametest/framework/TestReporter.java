package net.minecraft.gametest.framework;

public interface TestReporter {
  void onTestFailed(GameTestInfo paramGameTestInfo);
  
  void onTestSuccess(GameTestInfo paramGameTestInfo);
  
  default void finish() {}
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\TestReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */