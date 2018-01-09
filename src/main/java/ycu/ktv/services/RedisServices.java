package ycu.ktv.services;


import redis.clients.jedis.Jedis;

public class RedisServices {
    private static Jedis jedis;

    //4小时
    private static final int ExpirationTime = 60 * 60 * 4;


    public static boolean AddToken(String token, String key) {
        setJedis();
        try {
            jedis.set(token, key);
            jedis.expire(token, ExpirationTime);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String QueryKey(String token) {
        setJedis();
        try {
            String key = jedis.get(token);
            if (key == null) {
                return "";
            } else {
                jedis.expire(token, ExpirationTime);
                return key;
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean DelToken(String token) {
        setJedis();
        try {
            if (jedis.del(token) == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static void setJedis() {
        //连接redis服务器
        jedis = new Jedis("118.89.166.89", 6379);
        //权限认证
        jedis.auth("giligili");
    }
}
