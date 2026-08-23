package net.minecraft.world.level.chunk;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;

public interface Palette<T> {
  int idFor(T paramT, PaletteResize<T> paramPaletteResize);
  
  boolean maybeHas(Predicate<T> paramPredicate);
  
  T valueFor(int paramInt);
  
  void read(FriendlyByteBuf paramFriendlyByteBuf, IdMap<T> paramIdMap);
  
  void write(FriendlyByteBuf paramFriendlyByteBuf, IdMap<T> paramIdMap);
  
  int getSerializedSize(IdMap<T> paramIdMap);
  
  int getSize();
  
  Palette<T> copy();
  
  public static interface Factory {
    <A> Palette<A> create(int param1Int, List<A> param1List);
  }
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\Palette.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */