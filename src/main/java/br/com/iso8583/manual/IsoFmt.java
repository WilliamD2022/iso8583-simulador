package br.com.iso8583.manual;

public class IsoFmt {
    // n (fixed): completa à esquerda com '0'
    public static String padLeftZeros(String s, int len) {
        if (s == null) s = "";
        // formata com espaços à esquerda e depois troca espaços por '0'
        return String.format("%" + len + "s", s).replace(' ', '0');
    }

    // an/ans (fixed): completa à direita com espaço
    public static String padRightSpaces(String s, int len) {
        if (s == null) s = "";
        return String.format("%-" + len + "s", s);
    }

    // LLVAR (indicador ASCII de 2 dígitos)
    public static String llvarAscii(String data) {
        if (data == null) data = "";
        return String.format("%02d", data.length()) + data;
    }

    // LLLVAR (indicador ASCII de 3 dígitos)
    public static String lllvarAscii(String data) {
        if (data == null) data = "";
        return String.format("%03d", data.length()) + data;
    }
}
