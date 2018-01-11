package ycu.ktv.dao;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.mvc.Mvcs;

import javax.sql.DataSource;

public class GetDao {
    public static Dao getDao() {
        return Mvcs.getIoc().get(Dao.class);
    }
}
