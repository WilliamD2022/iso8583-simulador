package br.com.iso8583.manual;
import java.util.*;

public class Spec {
    public static final Map<Integer, DE> MAP = new HashMap<>();
    static {
        MAP.put(2,  new DE(2,19, Type.LLVAR));
        MAP.put(3,  new DE(3,6,  Type.N_FIXED));
        MAP.put(4,  new DE(4,12, Type.N_FIXED));
        MAP.put(7,  new DE(7,10, Type.N_FIXED));
        MAP.put(11, new DE(11,6, Type.N_FIXED));
        MAP.put(12, new DE(12,6, Type.N_FIXED));
        MAP.put(13, new DE(13,4, Type.N_FIXED));
        MAP.put(14, new DE(14,4, Type.N_FIXED));
        MAP.put(22, new DE(22,3, Type.N_FIXED));
        MAP.put(25, new DE(25,2, Type.N_FIXED));
        MAP.put(41, new DE(41,8, Type.AN_FIXED));
        MAP.put(42, new DE(42,15,Type.AN_FIXED));
        MAP.put(49, new DE(49,3, Type.N_FIXED));
        MAP.put(55, new DE(55,255,Type.LLLBINARY));
    }
}
