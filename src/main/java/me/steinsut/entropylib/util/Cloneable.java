package me.steinsut.entropylib.util;

public interface Cloneable<T extends Cloneable<T>> {
    T makeClone();
}
