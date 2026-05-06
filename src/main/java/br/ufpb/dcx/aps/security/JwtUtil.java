package br.ufpb.dcx.aps.security;

import br.ufpb.dcx.aps.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtil {

    private static final String SECRET = "secret";

    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    //gera token:
    public static String generateToken(Usuario usuario){
        return JWT.create()
                .withClaim("id", usuario.getId())
                .withClaim("email", usuario.getEmail())
                .withClaim("categoria", usuario.getCategoria().name())
                .sign(algorithm);
    }

    //valida token:
    public static DecodedJWT validateToken(String token){
        return JWT.require(algorithm)
                .build()
                .verify(token);
    }
}
