package net.minecraft.util.parsing.packrat;

@FunctionalInterface
public interface RuleAction<S, T> {
  T run(ParseState<S> paramParseState);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\Rule$RuleAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */