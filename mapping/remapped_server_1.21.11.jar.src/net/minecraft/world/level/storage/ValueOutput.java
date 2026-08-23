package net.minecraft.world.level.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public interface ValueOutput {
  <T> void store(String paramString, Codec<T> paramCodec, T paramT);
  
  <T> void storeNullable(String paramString, Codec<T> paramCodec, T paramT);
  
  @Deprecated
  <T> void store(MapCodec<T> paramMapCodec, T paramT);
  
  void putBoolean(String paramString, boolean paramBoolean);
  
  void putByte(String paramString, byte paramByte);
  
  void putShort(String paramString, short paramShort);
  
  void putInt(String paramString, int paramInt);
  
  void putLong(String paramString, long paramLong);
  
  void putFloat(String paramString, float paramFloat);
  
  void putDouble(String paramString, double paramDouble);
  
  void putString(String paramString1, String paramString2);
  
  void putIntArray(String paramString, int[] paramArrayOfint);
  
  ValueOutput child(String paramString);
  
  ValueOutputList childrenList(String paramString);
  
  <T> TypedOutputList<T> list(String paramString, Codec<T> paramCodec);
  
  void discard(String paramString);
  
  boolean isEmpty();
  
  public static interface TypedOutputList<T> {
    void add(T param1T);
    
    boolean isEmpty();
  }
  
  public static interface ValueOutputList {
    ValueOutput addChild();
    
    void discardLast();
    
    boolean isEmpty();
  }
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\ValueOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */