package net.minecraft.util.parsing.packrat;

public interface NamedRule<S, T> {
  Atom<T> name();
  
  Rule<S, T> value();
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\NamedRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */