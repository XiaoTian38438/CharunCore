/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.mojang.datafixers;

import com.mojang.datafixers.DataFixerUpper;
import com.mojang.datafixers.RewriteResult;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.View;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.BitSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DataFix {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataFix.class);
    private final Schema outputSchema;
    private final boolean changesType;
    @Nullable
    private TypeRewriteRule rule;

    public DataFix(Schema schema, boolean bl) {
        this.outputSchema = schema;
        this.changesType = bl;
    }

    protected <A> TypeRewriteRule fixTypeEverywhere(String string, Type<A> type, Function<DynamicOps<?>, Function<A, A>> function) {
        return this.fixTypeEverywhere(string, type, type, function, new BitSet());
    }

    protected <A, B> TypeRewriteRule convertUnchecked(String string, Type<A> type, Type<B> type2) {
        return this.fixTypeEverywhere(string, type, type2, dynamicOps -> Function.identity(), new BitSet());
    }

    protected TypeRewriteRule writeAndRead(String string, Type<?> type, Type<?> type2) {
        return this.writeFixAndRead(string, type, type2, Function.identity());
    }

    protected <A, B> TypeRewriteRule writeFixAndRead(String string, Type<A> type, Type<B> type2, Function<Dynamic<?>, Dynamic<?>> function) {
        AtomicReference atomicReference = new AtomicReference();
        RewriteResult<A, B> rewriteResult = DataFix.unchecked(string, type, type2, dynamicOps -> object -> {
            Optional optional = ((Type)atomicReference.getPlain()).writeDynamic(dynamicOps, object).resultOrPartial(arg_0 -> ((Logger)LOGGER).error(arg_0));
            if (optional.isEmpty()) {
                throw new RuntimeException("Could not write the object in " + string);
            }
            Dynamic dynamic = (Dynamic)function.apply(optional.get());
            Optional optional2 = type2.readTyped(dynamic).resultOrPartial(arg_0 -> ((Logger)LOGGER).error(arg_0));
            if (optional2.isEmpty()) {
                throw new RuntimeException("Could not read the new object in " + string);
            }
            return optional2.get().getFirst().getValue();
        }, new BitSet());
        TypeRewriteRule typeRewriteRule = this.fixTypeEverywhere(type, rewriteResult);
        atomicReference.setPlain(type.all(typeRewriteRule, true, false).view().newType());
        return typeRewriteRule;
    }

    protected <A, B> TypeRewriteRule fixTypeEverywhere(String string, Type<A> type, Type<B> type2, Function<DynamicOps<?>, Function<A, B>> function) {
        return this.fixTypeEverywhere(string, type, type2, function, new BitSet());
    }

    protected <A, B> TypeRewriteRule fixTypeEverywhere(String string, Type<A> type, Type<B> type2, Function<DynamicOps<?>, Function<A, B>> function, BitSet bitSet) {
        return this.fixTypeEverywhere(type, DataFix.unchecked(string, type, type2, function, bitSet));
    }

    protected <A> TypeRewriteRule fixTypeEverywhereTyped(String string, Type<A> type, Function<Typed<?>, Typed<?>> function) {
        return this.fixTypeEverywhereTyped(string, type, function, new BitSet());
    }

    protected <A> TypeRewriteRule fixTypeEverywhereTyped(String string, Type<A> type, Function<Typed<?>, Typed<?>> function, BitSet bitSet) {
        return this.fixTypeEverywhereTyped(string, type, type, function, bitSet);
    }

    protected <A, B> TypeRewriteRule fixTypeEverywhereTyped(String string, Type<A> type, Type<B> type2, Function<Typed<?>, Typed<?>> function) {
        return this.fixTypeEverywhereTyped(string, type, type2, function, new BitSet());
    }

    protected <A, B> TypeRewriteRule fixTypeEverywhereTyped(String string, Type<A> type, Type<B> type2, Function<Typed<?>, Typed<?>> function, BitSet bitSet) {
        return this.fixTypeEverywhere(type, DataFix.checked(string, type, type2, function, bitSet));
    }

    private static <A, B> RewriteResult<A, B> unchecked(String string, Type<A> type, Type<B> type2, Function<DynamicOps<?>, Function<A, B>> function, BitSet bitSet) {
        return RewriteResult.create(View.create(string, type, type2, new NamedFunctionWrapper<A, B>(string, function)), bitSet);
    }

    public static <A, B> RewriteResult<A, B> checked(String string, Type<A> type, Type<B> type2, Function<Typed<?>, Typed<?>> function, BitSet bitSet) {
        return RewriteResult.create(View.create(string, type, type2, new NamedFunctionWrapper(string, dynamicOps -> object -> {
            Typed typed = (Typed)function.apply(new Typed<Object>(type, (DynamicOps<?>)dynamicOps, object));
            if (!type2.equals(typed.type, true, false)) {
                throw new IllegalStateException(String.format("Dynamic type check failed: %s not equal to %s", type2, typed.type));
            }
            return typed.value;
        })), bitSet);
    }

    protected <A, B> TypeRewriteRule fixTypeEverywhere(Type<A> type, RewriteResult<A, B> rewriteResult) {
        return TypeRewriteRule.checkOnce(TypeRewriteRule.everywhere(TypeRewriteRule.ifSame(type, rewriteResult), DataFixerUpper.OPTIMIZATION_RULE, true, true), this::onFail);
    }

    protected void onFail(Type<?> type) {
        LOGGER.info("Not matched: " + String.valueOf(this) + " " + String.valueOf(type));
    }

    public final int getVersionKey() {
        return this.getOutputSchema().getVersionKey();
    }

    public TypeRewriteRule getRule() {
        if (this.rule == null) {
            this.rule = this.makeRule();
        }
        return this.rule;
    }

    protected abstract TypeRewriteRule makeRule();

    protected Schema getInputSchema() {
        if (this.changesType) {
            return this.outputSchema.getParent();
        }
        return this.getOutputSchema();
    }

    protected Schema getOutputSchema() {
        return this.outputSchema;
    }

    private static final class NamedFunctionWrapper<A, B>
    implements Function<DynamicOps<?>, Function<A, B>> {
        private final String name;
        private final Function<DynamicOps<?>, Function<A, B>> delegate;

        public NamedFunctionWrapper(String string, Function<DynamicOps<?>, Function<A, B>> function) {
            this.name = string;
            this.delegate = function;
        }

        @Override
        public Function<A, B> apply(DynamicOps<?> dynamicOps) {
            return this.delegate.apply(dynamicOps);
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            NamedFunctionWrapper namedFunctionWrapper = (NamedFunctionWrapper)object;
            return Objects.equals(this.name, namedFunctionWrapper.name);
        }

        public int hashCode() {
            return this.name.hashCode();
        }
    }
}

