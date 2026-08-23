package com.mojang.datafixers.types;

import com.mojang.datafixers.TypedOptic;
import com.mojang.datafixers.util.Either;

public interface TypeMatcher<FT, FR> {
  <S> Either<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException> match(Type<S> paramType);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\Type$TypeMatcher.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */