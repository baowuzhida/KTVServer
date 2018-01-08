package ycu.ktv.module;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import ycu.ktv.entity.User;

import javax.sql.DataSource;

public class MainModule {

    public static void main(String[] args) {
//        User u = new User();
//        u.setUser_name("DarkBao");
//        getDao().insert(u);
        LoginModule loginModule=new LoginModule();
//        loginModule.login("18270024144");
    }

    public static Dao getDao() {
        Ioc ioc = new NutIoc(new JsonLoader("ioc/dao.js"));
        DataSource ds = ioc.get(DataSource.class);
        Dao dao = new NutDao(ds); //如果已经定义了dao,那么改成dao = ioc.get(Dao.class);
        return dao;
    }
}
