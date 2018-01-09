package ycu.ktv.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import ycu.ktv.dao.GetDao;
import ycu.ktv.entity.Token;
import ycu.ktv.entity.User;

import java.security.Key;
import java.util.Date;
import java.util.Random;

public class TokenControl {

//    private String key = "U2FsdGVkX19JiOTtaBdBuY+ixhNiVw8qdCKeAWHVrKo=";

    public static String getToken(int user_id){

        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        String key = sb.toString();;;//这里是加密解密的key。
        String compactJws = Jwts.builder()//返回的字符串便是我们的jwt串了
                .setSubject(String.valueOf(user_id))//设置主题
                .signWith(SignatureAlgorithm.HS512, key)//设置算法（必须）
                .compact();//这个是全部设置完成后拼成jwt串的方法
        RedisServices.AddToken(compactJws,String.valueOf(key));
        return compactJws;

    }

    public static String analysisToken(String compactJws){
        try {
            String key = RedisServices.QueryKey(compactJws);
            if (!key.equals("")) {
                Jws<Claims> parseClaimsJws = Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(compactJws);//compactJws为jwt字符串
                Claims body = parseClaimsJws.getBody();//得到body后我们可以从body中获取我们需要的信息
                //比如 获取主题,当然，这是我们在生成jwt字符串的时候就已经存进来的
                return body.getSubject();
                //OK, we can trust this JWT
            } else {
                return "";
            }
        } catch (ExpiredJwtException e) {
            System.out.println("Error!");
            // TODO: handle exception
            // jwt 已经过期，在设置jwt的时候如果设置了过期时间，这里会自动判断jwt是否已经过期，如果过期则会抛出这个异常，我们可以抓住这个异常并作相关处理。
        }
        return null;
    }

}
