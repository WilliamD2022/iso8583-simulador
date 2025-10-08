package br.com.iso8583.jposflow;

import org.jpos.iso.ISODate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;

import java.util.Date;

public class IsoBuilder {
    private final ISOMsg msg = new ISOMsg();

    public IsoBuilder() {
        // ISO 8583:1987 ASCII packager padrão do jPOS
        msg.setPackager(new ISO87APackager()); // não precisa try/catch
    }

    public IsoBuilder mti(String mti) {
        try {
            msg.setMTI(mti); // este sim pode lançar ISOException
        } catch (ISOException e) {
            throw new RuntimeException("MTI inválido: " + mti, e);
        }
        return this;
    }

    public IsoBuilder set(int de, String v) {
        msg.set(de, v);
        return this;
    }

    public IsoBuilder setBinary(int de, byte[] b) {
        msg.set(de, b);
        return this;
    }

    public byte[] pack() {
        try {
            return msg.pack();
        } catch (ISOException e) {
            throw new RuntimeException("Erro ao empacotar ISO 8583", e);
        }
    }

    public static String de7Now()  { return ISODate.getDateTime(new Date()); }          // MMDDhhmmss
    public static String de12Now() { return ISODate.getTime(new Date()); }              // hhmmss
    public static String de13Now() { return ISODate.getDate(new Date()).substring(0,4);} // MMDD
}
