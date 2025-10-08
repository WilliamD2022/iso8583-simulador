package br.com.iso8583.iso.dto;

public record Build0400Request(
        String hex0200,
        String acquirerId,  // 11 dígitos
        String forwarderId  // 11 dígitos
) {}
