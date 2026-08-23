package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.Typed;
import com.mojang.datafixers.types.Type;

interface SubFixer<F> {
  Typed<F> fix(Typed<?> paramTyped, Type<F> paramType);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\FixProjectileStoredItem$SubFixer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */