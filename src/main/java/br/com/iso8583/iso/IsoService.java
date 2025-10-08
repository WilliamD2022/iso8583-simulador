package br.com.iso8583.iso;

import br.com.iso8583.jposflow.IsoBuilder;
import br.com.iso8583.jposflow.IsoJsonPrinter;
import br.com.iso8583.jposflow.ReplyAndReversal;
import org.jpos.iso.*;
import org.jpos.iso.packager.ISO87APackager;
import org.springframework.stereotype.Service;

@Service
public class IsoService {

    public byte[] build0200(String pan, String amountMinor, String stan) {
        IsoBuilder b = new IsoBuilder()
                .mti("0200")
                .set(2,  pan)                    // cuidado: não logar PAN completo
                .set(3,  "000000")
                .set(4,  amountMinor)            // ex: "000000010000" = 100,00
                .set(7,  IsoBuilder.de7Now())
                .set(11, stan)
                .set(12, IsoBuilder.de12Now())
                .set(13, IsoBuilder.de13Now())
                .set(14, "2902")
                .set(22, "051")
                .set(25, "00")
                .set(41, "TERM001")
                .set(42, "MERC00000000001")
                .set(49, "986");
        return b.pack();
    }

    public String pretty(byte[] iso) throws Exception {
        ISOMsg m = new ISOMsg();
        m.setPackager(new ISO87APackager());
        m.unpack(iso);
        return IsoJsonPrinter.toJson(m);
    }

    public byte[] build0210From0200(byte[] req, String respCode, String authId) throws Exception {
        ISOMsg m = new ISOMsg();
        m.setPackager(new ISO87APackager());
        m.unpack(req);
        ISOMsg r = ReplyAndReversal.build0210From0200(m, respCode, authId);
        return r.pack();
    }

    public byte[] build0400From0200(byte[] req, String acquirer, String forwarder) throws Exception {
        ISOMsg m = new ISOMsg();
        m.setPackager(new ISO87APackager());
        m.unpack(req);
        ISOMsg r = ReplyAndReversal.build0400From0200(m, acquirer, forwarder);
        return r.pack();
    }
}
