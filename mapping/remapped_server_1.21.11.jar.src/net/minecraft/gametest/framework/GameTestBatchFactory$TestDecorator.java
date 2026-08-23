package net.minecraft.gametest.framework;

import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface TestDecorator {
  Stream<GameTestInfo> decorate(Holder.Reference<GameTestInstance> paramReference, ServerLevel paramServerLevel);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestBatchFactory$TestDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */