package com.mojang.datafixers.optics;

import com.mojang.datafixers.FunctionType;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;

public interface Wander<S, T, A, B> {
  <F extends com.mojang.datafixers.kinds.K1> FunctionType<S, App<F, T>> wander(Applicative<F, ?> paramApplicative, FunctionType<A, App<F, B>> paramFunctionType);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Wander.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */