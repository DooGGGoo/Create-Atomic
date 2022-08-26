package mod.dooggoo.createatomic.register;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.Create;

import mod.dooggoo.createatomic.CreateAtomic;
import net.minecraft.resources.ResourceLocation;

public class ModPartials {
    public static PartialModel get(String name) {
        return new PartialModel(new ResourceLocation(CreateAtomic.MODID, "block/" + name));
    }

    public static PartialModel getCreate(String name) {
        return new PartialModel(Create.asResource("block/" + name));
    }

    public static PartialModel getEntity(String name) {
        return new PartialModel(new ResourceLocation(CreateAtomic.MODID, "entity/" + name));
    }

    public static PartialModel getEntityCreate(String name) {
        return new PartialModel(Create.asResource("entity/" + name));
    }

    public static void load() {

    }
}
