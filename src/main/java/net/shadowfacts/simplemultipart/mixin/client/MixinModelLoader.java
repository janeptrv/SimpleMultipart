package net.shadowfacts.simplemultipart.mixin.client;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import net.shadowfacts.simplemultipart.client.MultipartContainerBakedModel;
import net.shadowfacts.simplemultipart.client.SimpleMultipartClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * @author shadowfacts
 */
@Mixin(ModelLoader.class)
public abstract class MixinModelLoader {

	@Shadow
	private Map<Identifier, BakedModel> bakedModels;

	@Inject(method = "<init>", at = @At("RETURN"))
	public void addMultipartModel(ResourceManager resourceManager_1, SpriteAtlasTexture spriteAtlasTexture_1, BlockColors colors_1, Profiler profiler_1, CallbackInfo info) {
		bakedModels.put(new ModelIdentifier("simplemultipart:container#"), new MultipartContainerBakedModel());
		bakedModels.put(new ModelIdentifier("simplemultipart:tickable_container#"), new MultipartContainerBakedModel());
	}

}
