package com.dimbisoapatrick.springjwt;
import com.dimbisoapatrick.springjwt.util.JwtTokenUtil;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
class JwtUnitTests {
    private final JwtTokenUtil util=new JwtTokenUtil();
    @Test void signedVerificationTokenHasExpectedEmail() { var token=JwtTokenUtil.generateToken("ada@example.com");assertThat(util.validateToken(token)).isTrue();assertThat(util.extractEmail(token)).isEqualTo("ada@example.com"); }
    @Test void malformedTokenIsRejected() { assertThatThrownBy(() -> util.extractEmail("garbage")).isInstanceOf(io.jsonwebtoken.JwtException.class); }
    @Test void wrongSignatureIsRejected() { var token=Jwts.builder().subject("ada@example.com").signWith(Jwts.SIG.HS256.key().build()).compact();assertThatThrownBy(() -> util.extractEmail(token)).isInstanceOf(io.jsonwebtoken.JwtException.class); }
}
