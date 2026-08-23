package net.minecraft.world.level.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;

public interface ValueInput {
  <T> Optional<T> read(String paramString, Codec<T> paramCodec);
  
  @Deprecated
  <T> Optional<T> read(MapCodec<T> paramMapCodec);
  
  Optional<ValueInput> child(String paramString);
  
  ValueInput childOrEmpty(String paramString);
  
  Optional<ValueInputList> childrenList(String paramString);
  
  ValueInputList childrenListOrEmpty(String paramString);
  
  <T> Optional<TypedInputList<T>> list(String paramString, Codec<T> paramCodec);
  
  <T> TypedInputList<T> listOrEmpty(String paramString, Codec<T> paramCodec);
  
  boolean getBooleanOr(String paramString, boolean paramBoolean);
  
  byte getByteOr(String paramString, byte paramByte);
  
  int getShortOr(String paramString, short paramShort);
  
  Optional<Integer> getInt(String paramString);
  
  int getIntOr(String paramString, int paramInt);
  
  long getLongOr(String paramString, long paramLong);
  
  Optional<Long> getLong(String paramString);
  
  float getFloatOr(String paramString, float paramFloat);
  
  double getDoubleOr(String paramString, double paramDouble);
  
  Optional<String> getString(String paramString);
  
  String getStringOr(String paramString1, String paramString2);
  
  Optional<int[]> getIntArray(String paramString);
  
  @Deprecated
  HolderLookup.Provider lookup();
  
  public static interface TypedInputList<T> extends Iterable<T> {
    boolean isEmpty();
    
    Stream<T> stream();
  }
  
  public static interface ValueInputList extends Iterable<ValueInput> {
    boolean isEmpty();
    
    Stream<ValueInput> stream();
  }
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\ValueInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */