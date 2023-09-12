package proxy;

import Interface.SongListInterface;
import object.SongList;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Logger;

public class SongListProxy {
    /**
     * 给用户类创建代理
     * 生成用户操作日志
     * 于此扩展songList类的方法
     * @param songList SongList 歌单信息
     * @return UserInterface 通过反射添加执行动作
     */
    public static SongListInterface createProxy(SongList songList){
        return (SongListInterface) Proxy.newProxyInstance(
                SongListProxy.class.getClassLoader(),
                new Class[]{SongListInterface.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Logger logger = Logger.getLogger(this.getClass().getName());
                        if ("getName".equals(method.getName())) {
                            logger.info("获取歌单名");
                        }
                        else if ("remove".equals(method.getName())){
                            logger.info("用户删除了歌单");
                        } else if ("getSongs".equals(method.getName())) {
                            logger.info("加载歌单内容");
                        }
                        return method.invoke(songList,args);
                    }
                }
        );
    }
}
