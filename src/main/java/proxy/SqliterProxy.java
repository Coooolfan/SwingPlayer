package proxy;

import Interface.SqliterInterface;
import object.Sqliter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SqliterProxy {
    public static SqliterInterface createProxy(Sqliter sqliter){
        return (SqliterInterface) Proxy.newProxyInstance(
                Sqliter.class.getClassLoader(),
                new Class[]{SqliterInterface.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("createSong".equals(method.getName())){
                            System.out.println("用户上传歌曲");
                        } else if ("removeSong".equals(method.getName())) {
                            System.out.println("用户删除歌曲");
                        } else if ("createSongList".equals(method.getName())) {
                            System.out.println("用户创建歌单");
                        } else if ("removeSongList".equals(method.getName())) {
                            System.out.println("用户删除了歌单");
                        } else if ("removeSongInSongList".equals(method.getName())) {
                            System.out.println("用户从歌单中移除歌曲");
                        }
                        return method.invoke(sqliter,args);
                    }
                }
        );
    }
}
