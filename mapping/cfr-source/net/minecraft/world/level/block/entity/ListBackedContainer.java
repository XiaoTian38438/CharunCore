/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.block.entity;

import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

public interface ListBackedContainer
extends Container {
    public NonNullList<ItemStack> getItems();

    default public int count() {
        return (int)this.getItems().stream().filter(Predicate.not(ItemStack::isEmpty)).count();
    }

    @Override
    default public int getContainerSize() {
        return this.getItems().size();
    }

    @Override
    default public void clearContent() {
        this.getItems().clear();
    }

    @Override
    default public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    default public ItemStack getItem(int n) {
        return this.getItems().get(n);
    }

    @Override
    default public ItemStack removeItem(int n, int n2) {
        ItemStack itemStack = ContainerHelper.removeItem(this.getItems(), n, n2);
        if (!itemStack.isEmpty()) {
            this.setChanged();
        }
        return itemStack;
    }

    @Override
    default public ItemStack removeItemNoUpdate(int n) {
        return ContainerHelper.removeItem(this.getItems(), n, this.getMaxStackSize());
    }

    @Override
    default public boolean canPlaceItem(int n, ItemStack itemStack) {
        return this.acceptsItemType(itemStack) && (this.getItem(n).isEmpty() || this.getItem(n).getCount() < this.getMaxStackSize(itemStack));
    }

    default public boolean acceptsItemType(ItemStack itemStack) {
        return true;
    }

    @Override
    default public void setItem(int n, ItemStack itemStack) {
        this.setItemNoUpdate(n, itemStack);
        this.setChanged();
    }

    default public void setItemNoUpdate(int n, ItemStack itemStack) {
        this.getItems().set(n, itemStack);
        itemStack.limitSize(this.getMaxStackSize(itemStack));
    }
}

