package iv.root.modeling.hospital;

public interface Unit<T> {
    T step(int dt);
}
