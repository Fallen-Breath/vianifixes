/*
 * This file is part of the vianifixes project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
 *
 * vianifixes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * vianifixes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with vianifixes.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.fallenbreath.vianifixes.mixins.polymercompat;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = "polymer-networking", versionPredicates = "*"))
@Pseudo
@Mixin(targets = "eu.pb4.polymer.networking.impl.PolymerHandshakeHandlerImplLogin", remap = false)
public abstract class PolymerHandshakeHandlerMixin
{
	@Inject(method = "<init>", at = @At("RETURN"), remap = false)
	private void scheduleTimeoutForOldClients(CallbackInfo ci)
	{
		try {
			Object server = this.getClass().getMethod("getServer").invoke(this);
			java.lang.reflect.Method execute = server.getClass().getMethod("execute", Runnable.class);
			
			execute.invoke(server, (Runnable) () -> {
				try {
					Thread.sleep(2000);
					execute.invoke(server, (Runnable) () -> {
						try {
							java.lang.reflect.Field canContinueField = this.getClass().getSuperclass().getDeclaredField("canContinue");
							canContinueField.setAccessible(true);
							if (!canContinueField.getBoolean(this)) {
								return;
							}
							
							java.lang.reflect.Field contextField = this.getClass().getSuperclass().getDeclaredField("context");
							contextField.setAccessible(true);
							Object context = contextField.get(this);
							Object consumer = context.getClass().getMethod("continueRunning").invoke(context);
							consumer.getClass().getMethod("accept", Object.class).invoke(consumer, context);
						} catch (Exception e) {
						}
					});
				} catch (Exception e) {
				}
			});
		} catch (Exception e) {
		}
	}
}
