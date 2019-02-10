package iv.root.modeling.hospital;

import java.util.Random;

public class RandomEnum {
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = new Random(System.currentTimeMillis()).nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}
