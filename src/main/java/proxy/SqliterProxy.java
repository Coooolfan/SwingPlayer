package proxy;

import Interface.SqliterInterface;
import object.Sqliter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Logger;

public class SqliterProxy {
    /**
     * 给用户类创建代理
     * 生成用户操作日志
     * 于此扩展sqliter类的方法
     * @param sqliter Sqliter
     * @return SqliterInterface 通过反射添加执行动作
     */
    public static SqliterInterface createProxy(Sqliter sqliter){
        return (SqliterInterface) Proxy.newProxyInstance(
                Sqliter.class.getClassLoader(),
                new Class[]{SqliterInterface.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Logger logger = Logger.getLogger(this.getClass().getName());
                        if ("createSong".equals(method.getName())){
                            logger.info("用户上传歌曲");
                        } else if ("removeSong".equals(method.getName())) {
                            logger.info("用户删除歌曲");
                        } else if ("createSongList".equals(method.getName())) {
                            logger.info("用户创建歌单");
                        } else if ("removeSongList".equals(method.getName())) {
                            logger.info("用户删除了歌单");
                        } else if ("removeSongInSongList".equals(method.getName())) {
                           logger.info("用户从歌单中移除歌曲");
                        }
                        return method.invoke(sqliter,args);
                    }
                }
        );
    }
}
