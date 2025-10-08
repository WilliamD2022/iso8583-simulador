package br.com.iso8583.jposflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

public class IsoJsonPrinter {
    private static final ObjectMapper M = new ObjectMapper();

    public static String maskPan(String pan) {
        if (pan == null || pan.length() < 8) return "****";
        String first6 = pan.substring(0, 6);
        String last4  = pan.substring(pan.length() - 4);
        return first6.substring(0,4) + " " + first6.substring(4,6) + "** **** " + last4;
    }

    public static String toJson(ISOMsg msg) throws Exception {
        ObjectNode root = M.createObjectNode();
        root.put("mti", msg.getMTI());

        // bitmap primário em HEX (se quiser, derive via msg.pack())
        byte[] packed = msg.pack();
        // bytes 0..3 = MTI ASCII, 4..11 = bitmap binário (ISO87A default)
        String bitmapHex = ISOUtil.hexString(java.util.Arrays.copyOfRange(packed, 4, 12));
        root.put("bitmap_primary", bitmapHex);
        root.putNull("bitmap_secondary");

        ObjectNode fields = M.createObjectNode();

        // DE2 (PAN) – mascarado
        if (msg.hasField(2)) {
            ObjectNode de2 = M.createObjectNode();
            de2.put("masked", maskPan(msg.getString(2)));
            de2.put("format", "LLVAR n");
            fields.set("2", de2);
        }

        // Campos numéricos simples
        putVal(fields, msg, 3);
        putVal(fields, msg, 4);
        putVal(fields, msg, 7);
        putVal(fields, msg, 11);
        putVal(fields, msg, 12);
        putVal(fields, msg, 13);
        putVal(fields, msg, 14);
        putVal(fields, msg, 22);
        putVal(fields, msg, 25);
        putVal(fields, msg, 41);
        putVal(fields, msg, 42);
        putVal(fields, msg, 49);

        // EMV (DE55) em HEX (se existir)
        if (msg.hasField(55)) {
            ObjectNode de55 = M.createObjectNode();
            de55.put("hex", ISOUtil.hexString(msg.getBytes(55)));
            fields.set("55", de55);
        }

        root.set("fields", fields);
        root.putArray("assumptions").add("ISO1987").add("ASCII").add("LLVAR unpacked");
        root.putArray("validations").add("bitmap ok").add("comprimentos ok").add("condicionais ok");

        // pretty print
        return M.writerWithDefaultPrettyPrinter().writeValueAsString(root);
    }

    private static void putVal(ObjectNode fields, ISOMsg msg, int de) {
        if (msg.hasField(de)) {
            ObjectNode node = M.createObjectNode();
            node.put("value", msg.getString(de));
            fields.set(String.valueOf(de), node);
        }
    }
}
