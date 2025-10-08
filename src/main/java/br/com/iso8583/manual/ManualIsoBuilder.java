package br.com.iso8583.manual;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class ManualIsoBuilder {
    private final String mti;
    private final Map<Integer,Object> values = new TreeMap<>();
    public ManualIsoBuilder(String mti){ this.mti=mti; }
    public ManualIsoBuilder set(int de,String v){ values.put(de, v); return this; }
    public ManualIsoBuilder setBinary(int de,byte[] v){ values.put(de, v); return this; }

    public byte[] pack(){
        try (var out = new java.io.ByteArrayOutputStream()){
            // MTI (+ bitmap binário)
            out.write(mti.getBytes(StandardCharsets.US_ASCII));
            out.write(Bitmap.build(values.keySet()));
            for (var e: values.entrySet()){
                int de = e.getKey();
                DE spec = Spec.MAP.get(de);
                Object val = e.getValue();
                switch (spec.type){
                    case N_FIXED -> out.write(IsoFmt.padLeftZeros(val.toString(), spec.length).getBytes(StandardCharsets.US_ASCII));
                    case AN_FIXED, ANS_FIXED -> out.write(IsoFmt.padRightSpaces(val.toString(), spec.length).getBytes(StandardCharsets.US_ASCII));
                    case LLVAR -> out.write(IsoFmt.llvarAscii(val.toString()).getBytes(StandardCharsets.US_ASCII));
                    case LLLVAR -> out.write(IsoFmt.lllvarAscii(val.toString()).getBytes(StandardCharsets.US_ASCII));
                    case BINARY -> out.write((byte[]) val);
                    case LLLBINARY -> {
                        byte[] bin = (byte[]) val;
                        String len = String.format("%03d", bin.length);
                        out.write(len.getBytes(StandardCharsets.US_ASCII));
                        out.write(bin);
                    }
                }
            }
            return out.toByteArray();
        } catch(Exception ex){ throw new RuntimeException(ex); }
    }
}
