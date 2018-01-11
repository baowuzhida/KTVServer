package ycu.ktv.module;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.mvc.annotation.IocBy;
import ycu.ktv.entity.User;

import javax.sql.DataSource;

@IocBy(args = {"*js", "ioc/",
        "*anno", "ycu.ktv.module",
        "*async",
        "*tx"
})
public class MainModule {

}
