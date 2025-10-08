package br.com.iso8583.manual;
import java.util.Set;

public class Bitmap {
    public static byte[] build(Set<Integer> fields){
        long hi=0, lo=0;
        for(int f: fields){
            if (f<1 || f>64 || f==1) continue;
            if (f<=32) hi |= (1L << (32 - f));
            else       lo |= (1L << (64 - f));
        }
        byte[] b=new byte[8];
        for(int i=0;i<4;i++) b[i]   = (byte)((hi >> (24 - i*8)) & 0xFF);
        for(int i=0;i<4;i++) b[4+i] = (byte)((lo >> (24 - i*8)) & 0xFF);
        return b;
    }
}
