package br.com.iso8583.jposflow;

import org.jpos.iso.*;

import org.jpos.iso.packager.ISO87APackager;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class ReplyAndReversal {
    // Gera um RRN (DE37) simples p/ demo (12 chars)
    public static String newRRN() {
        String mmddhh = ISODate.getDate(new Date()).substring(0,4) + ISODate.getTime(new Date()).substring(0,2);
        int rand = ThreadLocalRandom.current().nextInt(100000, 999999);
        return mmddhh + rand; // 4+2+6 = 12
    }

    // Monta uma 0210 a partir da 0200 original
    public static ISOMsg build0210From0200(ISOMsg req0200, String respCode, String authId) throws ISOException {
        ISOMsg r = new ISOMsg();
        r.setPackager(new ISO87APackager());
        r.setMTI("0210");

        // Eco de campos comuns
        copyIfExists(req0200, r, 3,4,7,11,12,13,41,42,49);

        // RRN (opcional mas comum)
        if (!r.hasField(37)) r.set(37, newRRN());

        // Resposta
        r.set(39, respCode);     // 00 = aprovado
        if (authId != null) r.set(38, authId); // Auth ID (quando aprovado)

        // Se quiser devolver EMV (DE55) de scripts/TVR/Tsi, set(55, bytes)
        return r;
    }

    // Monta uma 0400 (reversal request) referenciando uma 0200 aprovada
    public static ISOMsg build0400From0200(ISOMsg req0200, String acquirerId, String forwarderId) throws ISOException {
        ISOMsg r = new ISOMsg();
        r.setPackager(new ISO87APackager());
        r.setMTI("0400");

        // Campos do movimento a reverter (normalmente os mesmos da 0200)
        copyIfExists(req0200, r, 2,3,4,41,42,49);

        // Data/hora e STAN do reversal (novos)
        r.set(7,  ISODate.getDateTime(new Date())); // MMDDhhmmss
        r.set(11, String.format("%06d", ThreadLocalRandom.current().nextInt(0, 999999)));

        // DE90 (Original Data Elements) – 42 chars
        //  MTI original (0200) + STAN original(6) + DE7 original(10) + AcquirerID(11) + ForwarderID(11)
        String origMti = "0200";
        String origStan = req0200.getString(11);
        String origDe7  = req0200.getString(7);
        String acq = rightPadDigits(acquirerId, 11);   // se não tiver, preenche com zeros
        String fwd = rightPadDigits(forwarderId, 11);  // idem
        String de90 = origMti + origStan + origDe7 + acq + fwd; // total 42
        r.set(90, de90);

        // (Opcional) DE37 do reversal
        r.set(37, newRRN());

        // (Opcional) incluir DE22/25/14/12/13 conforme tua regra
        return r;
    }

    // Helpers
    private static void copyIfExists(ISOMsg from, ISOMsg to, int... des) {
        for (int de : des) if (from.hasField(de)) to.set(de, from.getString(de));
    }
    private static String rightPadDigits(String s, int len) {
        if (s == null) s = "";
        s = s.replaceAll("\\D", "");
        if (s.length() > len) return s.substring(0, len);
        return String.format("%-" + len + "s", s).replace(' ', '0');
    }
}
