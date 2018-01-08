package ycu.ktv.services;

import io.jsonwebtoken.*;
import ycu.ktv.dao.GetDao;
import ycu.ktv.entity.Token;
import ycu.ktv.entity.User;

import java.util.Date;

public class TokenControl {

    private String key = "U2FsdGVkX19JiOTtaBdBuY+ixhNiVw8qdCKeAWHVrKo=";

    public String getToken(){

        //Key key = MacProvider.generateKey();//这里是加密解密的key。
        User user = new User();
        user.setUser_name("asd");
        String test = user.toString();
        String compactJws = Jwts.builder()//返回的字符串便是我们的jwt串了
                .setSubject(test)//设置主题
                .signWith(SignatureAlgorithm.HS512, key)//设置算法（必须）
                .compact();//这个是全部设置完成后拼成jwt串的方法
        System.out.println(compactJws);
        return compactJws;

    }

    public void analysisToken(String compactJws){
        try {

            Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws);//compactJws为jwt字符串
            Claims body = parseClaimsJws.getBody();//得到body后我们可以从body中获取我们需要的信息
            //比如 获取主题,当然，这是我们在生成jwt字符串的时候就已经存进来的
            String subject = body.getSubject();

            System.out.println(subject);


            //OK, we can trust this JWT

        } catch (ExpiredJwtException e) {
            System.out.println("Error!");
            // TODO: handle exception
            // jwt 已经过期，在设置jwt的时候如果设置了过期时间，这里会自动判断jwt是否已经过期，如果过期则会抛出这个异常，我们可以抓住这个异常并作相关处理。
        }
    }
    public boolean saveToken(int user_id,String compactJws){
        Token token = new Token();
        String time = new Date().getTime()+2*60*1000+"";
        token.setToken_info(compactJws);
        token.setUser_id(user_id);
//        String time1 = time+2*60*1000+"";
        token.setToken_life(time);
        GetDao.getDao().insert(TokenControl.class);
        return true;

    }

//    public static void main(String[] args) {
//        String key = "U2FsdGVkX19JiOTtaBdBuY+ixhNiVw8qdCKeAWHVrKo=";//这里是加密解密的key AES加密。
//        User user = new User();
//        user.setUser_name("asd");
//        String test = user.toString();
//        String compactJws = Jwts.builder()//返回的字符串便是我们的jwt串了
//                .setSubject("asd")//设置主题
//                .signWith(SignatureAlgorithm.HS512, key)//设置算法（必须）
//                .compact();//这个是全部设置完成后拼成jwt串的方法
//
//        try {
//
//            Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws);//compactJws为jwt字符串
//            Claims body = parseClaimsJws.getBody();//得到body后我们可以从body中获取我们需要的信息
//            //比如 获取主题,当然，这是我们在生成jwt字符串的时候就已经存进来的
//            String subject = body.getSubject();
//
//            System.out.println(subject);
//
//            //OK, we can trust this JWT
//
//        } catch (ExpiredJwtException e) {
//            System.out.println("Error!");
//            // TODO: handle exception
//            // jwt 已经过期，在设置jwt的时候如果设置了过期时间，这里会自动判断jwt是否已经过期，如果过期则会抛出这个异常，我们可以抓住这个异常并作相关处理。
//        }
//    }
}
