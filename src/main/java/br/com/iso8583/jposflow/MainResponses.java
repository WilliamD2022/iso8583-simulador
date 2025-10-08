package br.com.iso8583.jposflow;

import org.jpos.iso.*;

import org.jpos.iso.packager.ISO87APackager;

public class MainResponses {
    public static void main(String[] args) throws Exception {
        // 1) Simula a 0200 (você pode reaproveitar a sua existente)
        ISOMsg req0200 = new ISOMsg();
        req0200.setPackager(new ISO87APackager());
        req0200.setMTI("0200");
        req0200.set(2,  "5487230000001234"); // PAN (não logar completo em prod)
        req0200.set(3,  "000000");
        req0200.set(4,  "000000010000");
        req0200.set(7,  br.com.iso8583.jposflow.IsoBuilder.de7Now());
        req0200.set(11, "123456");
        req0200.set(12, br.com.iso8583.jposflow.IsoBuilder.de12Now());
        req0200.set(13, br.com.iso8583.jposflow.IsoBuilder.de13Now());
        req0200.set(14, "2902");
        req0200.set(22, "051");
        req0200.set(25, "00");
        req0200.set(41, "TERM001");
        req0200.set(42, "MERC00000000001");
        req0200.set(49, "986");

        byte[] reqPacked = req0200.pack();

        // 2) Monta 0210 (aprovada)
        ISOMsg resp0210 = ReplyAndReversal.build0210From0200(req0200, "00", "ABC123");
        byte[] r210 = resp0210.pack();
        System.out.println("== 0210 (aprovada) ==");
        System.out.println("ASCII: " + new String(r210));
        System.out.println("HEX  : " + ISOUtil.hexString(r210));

        // (Opcional) Pretty JSON
        String json210 = IsoJsonPrinter.toJson(resp0210);
        System.out.println("\nJSON 0210:\n" + json210);

        // 3) Monta 0400 (reversal da 0200)
        ISOMsg rev0400 = ReplyAndReversal.build0400From0200(req0200, "12345678901", "00000000000");
        byte[] r400 = rev0400.pack();
        System.out.println("\n== 0400 (reversal) ==");
        System.out.println("ASCII: " + new String(r400));
        System.out.println("HEX  : " + ISOUtil.hexString(r400));

        // (Opcional) Pretty JSON
        String json400 = IsoJsonPrinter.toJson(rev0400);
        System.out.println("\nJSON 0400:\n" + json400);
    }
}
