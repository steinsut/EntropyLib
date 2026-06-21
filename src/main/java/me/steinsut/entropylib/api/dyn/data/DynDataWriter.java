package me.steinsut.entropylib.api.dyn.data;

import com.mojang.logging.LogUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.storage.ValueOutput;
import org.slf4j.Logger;

public final class DynDataWriter<D> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final DynDataType.Holder<D> holder;

    public DynDataWriter(DynDataType.Holder<D> holder) {
        this.holder = holder;
    }

    @SuppressWarnings("unchecked")
    public void writeToHolder(DynDataType.Holder<?> holder) {
        if (this.holder.getDataType() == holder.getDataType()) {
            ((DynDataType.Holder<D>) holder).setData(this.holder.getData());
        } else {
            LOGGER.warn("Cannot copy value to holder, holders belong to different data types");
        }
    }

    public void storeData(ValueOutput out, String childName) {
        out.store(childName, this.holder.getCodec(), this.holder.getData());
    }

    public void encodeData(RegistryFriendlyByteBuf buf) {
        this.holder.getStreamCodec().encode(buf, this.holder.getData());
    }
}
