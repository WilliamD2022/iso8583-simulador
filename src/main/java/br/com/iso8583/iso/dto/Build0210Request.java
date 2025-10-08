package br.com.iso8583.iso.dto;

public record Build0210Request(
        String hex0200,  // 0200 em HEX
        String respCode, // ex: "00"
        String authId    // ex: "ABC123"
) {}
