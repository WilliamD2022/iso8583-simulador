package br.com.iso8583.iso.dto;

public record Build0200Request(
        String pan,          // ex: 5487230000001234 (não logar completo)
        String amountMinor,  // ex: 000000010000 (= R$ 100,00)
        String stan          // ex: 123456
) {}
