/*
 * This file is part of the TemplateMod project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
 *
 * TemplateMod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TemplateMod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TemplateMod.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.fallenbreath.vianifixes.mixins.fabricnetworkingapi;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.fabricmc.fabric.impl.networking.server.ServerConfigurationNetworkAddon;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Clients in older mc versions connected with viaversion never responds with the pong packet,
 * resulting in an endless waiting inside the ServerConfigurationNetworkAddon
 * <p>
 * Here's a bruteforce fix for it
 */
@Restriction(require = @Condition("fabric-networking-api-v1"))
@SuppressWarnings("UnstableApiUsage")
@Mixin(ServerConfigurationNetworkAddon.class)
public abstract class ServerConfigurationNetworkAddonMixin
{
	@ModifyReturnValue(
			method = "startConfiguration",
			at = @At(value = "RETURN", ordinal = 0),
			remap = false
	)
	private boolean doNotPerformTheUselessWait(boolean shouldContinueProcess)
	{
		return false;
	}

	@WrapOperation(
			method = {
					"receiveRegistration",
					"onPong"
			},
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/network/ServerConfigurationNetworkHandler;sendConfigurations()V",
					remap = true
			),
			remap = false
	)
	private void skipTheCallCuzItHasAlreadyBeenDone(ServerConfigurationNetworkHandler instance, Operation<Void> original)
	{
	}
}
