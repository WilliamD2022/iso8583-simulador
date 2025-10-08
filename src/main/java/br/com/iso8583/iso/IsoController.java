package br.com.iso8583.iso;

import br.com.iso8583.iso.dto.*;
import org.jpos.iso.ISOUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/iso")
public class IsoController {
    private final IsoService service;
    public IsoController(IsoService service){ this.service = service; }

    // 0200: monta e retorna HEX + JSON
    @PostMapping(value="/0200", produces=MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> build0200(@RequestBody Build0200Request req) throws Exception {
        byte[] msg = service.build0200(req.pan(), req.amountMinor(), req.stan());
        String hex = ISOUtil.hexString(msg);
        String json = service.pretty(msg);
        return Map.of("hex", hex, "json", json);
    }

    // Pretty de qualquer ISO HEX
    @PostMapping(value="/pretty", produces=MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> pretty(@RequestBody FromRawRequest req) throws Exception {
        byte[] raw = ISOUtil.hex2byte(req.hex());
        String json = service.pretty(raw);
        return Map.of("json", json);
    }

    // 0210 a partir da 0200 (HEX)
    @PostMapping(value="/0210-from-0200", produces=MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> build0210(@RequestBody Build0210Request req) throws Exception {
        byte[] r = service.build0210From0200(ISOUtil.hex2byte(req.hex0200()), req.respCode(), req.authId());
        return Map.of("hex", ISOUtil.hexString(r), "json", service.pretty(r));
    }

    // 0400 (reversal) a partir da 0200 (HEX)
    @PostMapping(value="/0400-from-0200", produces=MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> build0400(@RequestBody Build0400Request req) throws Exception {
        byte[] r = service.build0400From0200(
                ISOUtil.hex2byte(req.hex0200()), req.acquirerId(), req.forwarderId());
        return Map.of("hex", ISOUtil.hexString(r), "json", service.pretty(r));
    }
}
