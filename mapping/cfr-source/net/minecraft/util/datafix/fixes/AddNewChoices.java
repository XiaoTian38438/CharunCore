/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TaggedChoice;
import java.util.Locale;

public class AddNewChoices
extends DataFix {
    private final String name;
    private final DSL.TypeReference type;

    public AddNewChoices(Schema schema, String string, DSL.TypeReference typeReference) {
        super(schema, true);
        this.name = string;
        this.type = typeReference;
    }

    @Override
    public TypeRewriteRule makeRule() {
        TaggedChoice.TaggedChoiceType<?> taggedChoiceType = this.getInputSchema().findChoiceType(this.type);
        TaggedChoice.TaggedChoiceType<?> taggedChoiceType2 = this.getOutputSchema().findChoiceType(this.type);
        return this.cap(taggedChoiceType, taggedChoiceType2);
    }

    private <K> TypeRewriteRule cap(TaggedChoice.TaggedChoiceType<K> taggedChoiceType, TaggedChoice.TaggedChoiceType<?> taggedChoiceType2) {
        if (taggedChoiceType.getKeyType() != taggedChoiceType2.getKeyType()) {
            throw new IllegalStateException("Could not inject: key type is not the same");
        }
        TaggedChoice.TaggedChoiceType<?> taggedChoiceType3 = taggedChoiceType2;
        return this.fixTypeEverywhere(this.name, taggedChoiceType, taggedChoiceType3, dynamicOps -> pair -> {
            if (!taggedChoiceType3.hasType(pair.getFirst())) {
                throw new IllegalArgumentException(String.format(Locale.ROOT, "%s: Unknown type %s in '%s'", this.name, pair.getFirst(), this.type.typeName()));
            }
            return pair;
        });
    }
}

