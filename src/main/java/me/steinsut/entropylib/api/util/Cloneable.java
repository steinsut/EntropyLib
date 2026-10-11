package me.steinsut.entropylib.api.util;

public interface Cloneable<T extends Cloneable<T>> {
    T makeClone();
}
