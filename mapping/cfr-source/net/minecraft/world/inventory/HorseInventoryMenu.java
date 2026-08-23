/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.inventory;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractMountInventoryMenu;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.Slot;

public class HorseInventoryMenu
extends AbstractMountInventoryMenu {
    private static final Identifier SADDLE_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/saddle");
    private static final Identifier LLAMA_ARMOR_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/llama_armor");
    private static final Identifier ARMOR_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/horse_armor");

    public HorseInventoryMenu(int n, Inventory inventory, Container container, final AbstractHorse abstractHorse, int n2) {
        super(n, inventory, container, abstractHorse);
        Container container2 = abstractHorse.createEquipmentSlotContainer(EquipmentSlot.SADDLE);
        this.addSlot(new ArmorSlot(this, container2, abstractHorse, EquipmentSlot.SADDLE, 0, 8, 18, SADDLE_SLOT_SPRITE){

            @Override
            public boolean isActive() {
                return abstractHorse.canUseSlot(EquipmentSlot.SADDLE) && abstractHorse.getType().is(EntityTypeTags.CAN_EQUIP_SADDLE);
            }
        });
        final boolean bl = abstractHorse instanceof Llama;
        Identifier identifier = bl ? LLAMA_ARMOR_SLOT_SPRITE : ARMOR_SLOT_SPRITE;
        Container container3 = abstractHorse.createEquipmentSlotContainer(EquipmentSlot.BODY);
        this.addSlot(new ArmorSlot(this, container3, abstractHorse, EquipmentSlot.BODY, 0, 8, 36, identifier){

            @Override
            public boolean isActive() {
                return abstractHorse.canUseSlot(EquipmentSlot.BODY) && (abstractHorse.getType().is(EntityTypeTags.CAN_WEAR_HORSE_ARMOR) || bl);
            }
        });
        if (n2 > 0) {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < n2; ++j) {
                    this.addSlot(new Slot(container, j + i * n2, 80 + j * 18, 18 + i * 18));
                }
            }
        }
        this.addStandardInventorySlots(inventory, 8, 84);
    }

    @Override
    protected boolean hasInventoryChanged(Container container) {
        return ((AbstractHorse)this.mount).hasInventoryChanged(container);
    }
}

