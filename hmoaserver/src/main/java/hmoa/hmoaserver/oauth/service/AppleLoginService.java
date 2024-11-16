package hmoa.hmoaserver.oauth.service;

import hmoa.hmoaserver.oauth.apple.TokenDecoder;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.oauth.apple.AppleAuthClient;
import hmoa.hmoaserver.oauth.apple.AppleProperties;
import hmoa.hmoaserver.oauth.userinfo.AppleOAuth2UserInfo;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

import static hmoa.hmoaserver.exception.Code.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleLoginService {
    private final AppleAuthClient appleAuthClient;
    private final AppleProperties appleProperties;

    public AppleOAuth2UserInfo get(String token) {
        String idToken = appleAuthClient.getIdToken(
                appleProperties.getClientId(),
                generateClientSecret(),
                appleProperties.getGrantType(),
                token
        ).getIdToken();

        return TokenDecoder.decodePayload(idToken, AppleOAuth2UserInfo.class);
    }

    private String generateClientSecret() {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, appleProperties.getKeyId())
                .setIssuer(appleProperties.getTeamId())
                .setAudience(appleProperties.getAudience())
                .setSubject(appleProperties.getClientId())
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleProperties.getPrivateKey());

            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }
}
