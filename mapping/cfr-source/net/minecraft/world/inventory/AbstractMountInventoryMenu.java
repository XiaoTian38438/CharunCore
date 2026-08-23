/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractMountInventoryMenu
extends AbstractContainerMenu {
    protected final Container mountContainer;
    protected final LivingEntity mount;
    protected final int SLOT_SADDLE = 0;
    protected final int SLOT_BODY_ARMOR = 1;
    protected final int SLOT_INVENTORY_START = 2;
    protected static final int INVENTORY_ROWS = 3;

    protected AbstractMountInventoryMenu(int n, Inventory inventory, Container container, LivingEntity livingEntity) {
        super(null, n);
        this.mountContainer = container;
        this.mount = livingEntity;
        container.startOpen(inventory.player);
    }

    protected abstract boolean hasInventoryChanged(Container var1);

    @Override
    public boolean stillValid(Player player) {
        return !this.hasInventoryChanged(this.mountContainer) && this.mountContainer.stillValid(player) && this.mount.isAlive() && player.isWithinEntityInteractionRange(this.mount, 4.0);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.mountContainer.stopOpen(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int n) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(n);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            int n2 = 2 + this.mountContainer.getContainerSize();
            if (n < n2) {
                if (!this.moveItemStackTo(itemStack2, n2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemStack2) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemStack2, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemStack2) && !this.getSlot(0).hasItem()) {
                if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.mountContainer.getContainerSize() == 0 || !this.moveItemStackTo(itemStack2, 2, n2, false)) {
                int n3;
                int n4 = n3 = n2 + 27;
                int n5 = n4 + 9;
                if (n >= n4 && n < n5 ? !this.moveItemStackTo(itemStack2, n2, n3, false) : (n >= n2 && n < n3 ? !this.moveItemStackTo(itemStack2, n4, n5, false) : !this.moveItemStackTo(itemStack2, n4, n3, false))) {
                    return ItemStack.EMPTY;
                }
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemStack;
    }

    public static int getInventorySize(int n) {
        return n * 3;
    }
}

