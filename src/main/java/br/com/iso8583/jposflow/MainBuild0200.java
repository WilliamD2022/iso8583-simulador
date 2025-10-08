package br.com.iso8583.jposflow;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.ISO87APackager;

public class MainBuild0200 {
    public static void main(String[] args) throws Exception {
        // 1) Monta a 0200
        IsoBuilder b = new IsoBuilder()
                .mti("0200")
                .set(2,  "5487230000001234") // PAN real em memória; log sempre mascarado
                .set(3,  "000000")
                .set(4,  "000000010000")
                .set(7,  IsoBuilder.de7Now())
                .set(11, "123456")
                .set(12, IsoBuilder.de12Now())
                .set(13, IsoBuilder.de13Now())
                .set(14, "2902")
                .set(22, "051")
                .set(25, "00")
                .set(41, "TERM001")
                .set(42, "MERC00000000001")
                .set(49, "986");
        // .setBinary(55, ISOUtil.hex2byte("9F0206...")); // se quiser EMV

        byte[] packed = b.pack();

        // 2) Mostra ASCII/HEX (debug)
        System.out.println("ASCII: " + new String(packed));
        System.out.println("HEX  : " + ISOUtil.hexString(packed));

        // 3) Desempacota e imprime JSON bonitão
        ISOMsg parsed = new ISOMsg();
        parsed.setPackager(new ISO87APackager());
        parsed.unpack(packed);

        String json = IsoJsonPrinter.toJson(parsed);
        System.out.println("\nJSON:");
        System.out.println(json);
    }
}
