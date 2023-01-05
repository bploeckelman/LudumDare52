package lando.systems.ld52.utils;

@FunctionalInterface
public interface Callback {
    void run(Object... params);
}
