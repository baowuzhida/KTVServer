package ycu.ktv;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import ycu.ktv.dao.GetDao;
import ycu.ktv.entity.Code;
import ycu.ktv.entity.User;
import ycu.ktv.services.RedisServices;

import java.security.Key;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
//        User p = GetDao.getDao().fetch(User.class,23);
        GetDao.getDao().update(User.class, Chain.make("kt_user_name", "asd")
                .addSpecial("kt_user_password", "+2*60*1000"), Cnd.where("kt_user_id","=", 23));
//        GetDao.getDao().update(Code.class, Chain.makeSpecial("kt_code_info", 5).add("kt_code_life", 11),
//                Cnd.where("kt_user_phone","=", "18174052899"));

//        System.out.println(GetDao.getDao().query(User.class, Cnd.where("kt_user_phone", "=", "18174052899").
//                and("kt_user_password","=","123")).get(0).getId());
//        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
//        Random random = new Random();
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < 8; i++) {
//            int number = random.nextInt(base.length());
//            sb.append(base.charAt(number));
//        }
//        String key = sb.toString();;;//这里是加密解密的key。
//        String compactJws = Jwts.builder()//返回的字符串便是我们的jwt串了
//                .setSubject(String.valueOf("123"))//设置主题
//                .signWith(SignatureAlgorithm.HS512, key)//设置算法（必须）
//                .compact();//这个是全部设置完成后拼成jwt串的方法
//        RedisServices.AddToken(compactJws,key);
//        String key1 = RedisServices.QueryKey(compactJws);
//        Jws<Claims> parseClaimsJws = Jwts.parser()
//                .setSigningKey(key1)
//                .parseClaimsJws(compactJws);//compactJws为jwt字符串
//        Claims body = parseClaimsJws.getBody();//得到body后我们可以从body中获取我们需要的信息
//        System.out.println(body.getSubject());
        //比如 获取主题,当然，这是我们在生成jwt字符串的时候就已经存进来的
        //OK, we can trust this JWT
    }
}


