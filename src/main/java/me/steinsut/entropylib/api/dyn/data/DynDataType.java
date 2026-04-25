/*
Copyright © 2026 steinsut

This file is part of EntropyLib.

EntropyLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

EntropyLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with EntropyLib. If not, see <https://www.gnu.org/licenses/>.
*/

package me.steinsut.entropylib.api.dyn.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import me.steinsut.entropylib.api.registries.CommonRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.Supplier;

import static me.steinsut.entropylib.EntropyLib.LOGGER;

public final class DynDataType<D> {
    public static final Codec<DynDataType<?>> CODEC = CommonRegistries.DYN_RENDERER_DATA_TYPE_REGISTRY.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, DynDataType<?>> STREAM_CODEC = ByteBufCodecs.registry(CommonRegistries.DYN_RENDERER_DATA_TYPE_REGISTRY_KEY);

    private final Supplier<D> defaultSupplier;
    private final Map<Identifier, Supplier<D>> presets;
    private final MapCodec<D> dataCodec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, D> dataStreamCodec;

    public DynDataType(MapCodec<D> dataCodec, StreamCodec<? super RegistryFriendlyByteBuf, D> dataStreamCodec, Supplier<D> defaultSupplier) {
        this(dataCodec, dataStreamCodec, defaultSupplier, new HashMap<>());
    }

    public DynDataType(MapCodec<D> dataCodec, StreamCodec<? super RegistryFriendlyByteBuf, D> dataStreamCodec, Supplier<D> defaultSupplier, Map<Identifier, Supplier<D>> presets) {
        this.defaultSupplier = defaultSupplier;
        this.presets = presets;
        this.dataCodec = dataCodec;
        this.dataStreamCodec = dataStreamCodec;
    }

    public Holder<D> createHolder() {
        return new Holder<>(this, defaultSupplier.get());
    }

    public Holder<D> createHolder(Identifier preset) {
        if (preset != null && this.presets.containsKey(preset)) {
            return new Holder<>(this, this.presets.get(preset).get());
        } else {
            LOGGER.warn("Preset \"{}\" is null or does not exist, falling back to default preset", preset);
            return this.createHolder();
        }
    }

    public boolean hasPreset(Identifier id) {
        return this.presets.containsKey(id);
    }

    public Collection<Identifier> getAllPresetsIds() {
        return Collections.unmodifiableSet(this.presets.keySet());
    }

    public static final class Builder<_D> {
        private final Supplier<_D> defaultSupplier;
        private final Map<Identifier, Supplier<_D>> presets;
        private final MapCodec<_D> codec;
        private final StreamCodec<? super RegistryFriendlyByteBuf, _D> streamCodec;

        public static <_D> Builder<_D> of(MapCodec<_D> codec, StreamCodec<? super RegistryFriendlyByteBuf, _D> streamCodec, Supplier<_D> defaultSupplier) {
            return new Builder<>(codec, streamCodec, defaultSupplier);
        }

        private Builder(MapCodec<_D> codec, StreamCodec<? super RegistryFriendlyByteBuf, _D> streamCodec, Supplier<_D> defaultSupplier) {
            this.defaultSupplier = defaultSupplier;
            this.presets = new HashMap<>();
            this.codec = codec;
            this.streamCodec = streamCodec;
        }

        public Builder<_D> withPreset(Identifier id, Supplier<_D> supplier) {
            if (this.presets.containsKey(id)) {
                throw new RuntimeException("Preset already exists: " + id);
            }

            this.presets.put(id, supplier);
            return this;
        }

        public DynDataType<_D> build() {
            return new DynDataType<>(this.codec, this.streamCodec, this.defaultSupplier, this.presets);
        }
    }

    public static final class Holder<_D> {
        private final DynDataType<_D> dataType;
        private _D data;

        private Holder(DynDataType<_D> dataType, _D data) {
            this.dataType = dataType;
            this.data = data;
        }

        public static StreamCodec<RegistryFriendlyByteBuf, Holder<?>> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public void encode(@NonNull RegistryFriendlyByteBuf buf, Holder<?> holder) {
                DynDataType.STREAM_CODEC.encode(buf, holder.dataType);
                holder.encodeData(buf);
            }

            public @NonNull Holder<?> decode(@NonNull RegistryFriendlyByteBuf buf) {
                DynDataType<?> dataType = DynDataType.STREAM_CODEC.decode(buf);
                Holder<?> holder = dataType.createHolder();
                holder.decodeData(buf);

                return holder;
            }
        };


        public _D getData() {
            return this.data;
        }

        public DynDataType<_D> getDataType() {
            return this.dataType;
        }

        @SuppressWarnings("unchecked")
        public void copyTo(Holder<?> holder) {
            if (this.dataType != holder.dataType) {
                ((Holder<_D>) holder).data = this.data;
            } else {
                LOGGER.warn("Cannot copy value to holder, holders belong to different data types");
            }
        }

        @SuppressWarnings("unchecked")
        public void copyFrom(Holder<?> holder) {
            if (this.dataType == holder.dataType) {
                this.data = ((Holder<_D>) holder).data;
            } else {
                LOGGER.warn("Cannot copy from holder, holders belong to different data types");
            }
        }

        public void readData(ValueInput in) {
            //noinspection deprecation
            in.read(this.dataType.dataCodec).ifPresent((d) -> {
                this.data = d;
            });
        }

        public void storeData(ValueOutput out) {
            //noinspection deprecation
            out.store(this.dataType.dataCodec, this.data);
        }

        public void decodeData(RegistryFriendlyByteBuf buf) {
            this.data = this.dataType.dataStreamCodec.decode(buf);
        }

        public void encodeData(RegistryFriendlyByteBuf buf) {
            this.dataType.dataStreamCodec.encode(buf, this.data);
        }

        public void setData(_D data) {
            this.data = data;
        }

        public void setToPreset(Identifier id) {
            if (this.dataType.hasPreset(id)) {
                this.data = this.dataType.presets.get(id).get();
            }
            else {
                LOGGER.warn("Data dynType does not have preset {}", id);
            }
        }
    }
}
