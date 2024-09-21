package hmoa.hmoaserver.admin.service;

import hmoa.hmoaserver.admin.dto.AdminTokenRequestDto;
import hmoa.hmoaserver.member.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String getTestToken() {
        Claims claims = Jwts.claims().setSubject("anonymous");
        claims.put("roles", Role.ADMIN);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 86_400_000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
