package ycu.ktv.services;


import redis.clients.jedis.Jedis;

public class RedisServices {
    private static Jedis jedis;

    //4小时
    private static final int ExpirationTime = 60 * 60 * 4;


    public static boolean AddKey(String key, String userid) {
        try {
            jedis.set(key, userid);
            jedis.expire(key, ExpirationTime);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String QueryKey(String key) {
        try {
            String userid = jedis.get(key);
            if (userid == null) {
                return "";
            } else {
                jedis.expire(key, ExpirationTime);
                return userid;
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean DelKet(String key) {
        try {
            jedis.del(key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void setJedis() {
        //连接redis服务器
        jedis = new Jedis("118.89.166.89", 6379);
        //权限认证
        jedis.auth("giligili");
    }
}
