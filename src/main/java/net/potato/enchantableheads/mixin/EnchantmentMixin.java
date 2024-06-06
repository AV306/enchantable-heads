package net.potato.enchantableheads.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

	@Inject(at = @At("HEAD"), method = "isAcceptableItem", cancellable = true)
	private void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if(stack.getItem() == Items.PLAYER_HEAD) cir.setReturnValue(true);
	}
	@Inject(at = @At("TAIL"), method = "getEquipment", cancellable = true)
	private void getEquipmentII(LivingEntity entity, CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir){
		Map<EquipmentSlot, ItemStack> returnValue = cir.getReturnValue();
		if(entity.getEquippedStack(EquipmentSlot.HEAD).getItem() == Items.PLAYER_HEAD && !returnValue.containsKey(EquipmentSlot.HEAD)){
			returnValue.put(EquipmentSlot.HEAD, entity.getEquippedStack(EquipmentSlot.HEAD));
			cir.setReturnValue(returnValue);
		}
	}
}