package com.mf.utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.util.Calendar;
import java.util.Date;

public class MFJWTToken {

	private static final String secretKeyString = MFConfig.LowaSecretKey(); // Ensure this returns a fixed key
	private static final SecretKey secretKey = new SecretKeySpec(secretKeyString.getBytes(),
			SignatureAlgorithm.HS256.getJcaName());

	// public static String generateToken(String subject) {
	// long expirationTimeMillis = MmcConfig.getMobileSessionTime();
	// Date now = new Date();
	// Date expiryDate = new Date(now.getTime() + expirationTimeMillis);
	//
	// return
	// Jwts.builder().setSubject(subject).setIssuedAt(now).setExpiration(expiryDate)
	// .signWith(SignatureAlgorithm.HS256, secretKey).compact();
	// }

	public static String generateToken(String subject) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to next day
		calendar.set(Calendar.HOUR_OF_DAY, 0); // 12 AM
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date expirationTime = calendar.getTime();
System.out.println("here token");
		return Jwts.builder().setSubject(subject).setIssuedAt(new Date()).setExpiration(expirationTime)
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public static String parseToken(String token) {
		try {
			System.out.println("here 5");

			Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
			return claims.getSubject();
		} catch (ExpiredJwtException e) {
			System.out.println("Token has expired");
		} catch (SignatureException e) {
			System.out.println("Token signature does not match");
		} catch (MalformedJwtException e) {
			System.out.println("Token is malformed");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean validateToken(String token) {
		try {
			// System.out.println(token);
			Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
			Date expiration = claims.getExpiration();
			Date now = new Date();

			if (expiration.before(now)) {
				// System.out.println("11 Token has expired");
				return false;
			}

			return true; // Token is valid and not expired
		} catch (ExpiredJwtException e) {
			System.out.println("Token has expired " + e);
			return false;
		} catch (SignatureException e) {
			System.out.println("Token signature does not match");
			return false;
		} catch (MalformedJwtException e) {
			System.out.println("Token is malformed");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
