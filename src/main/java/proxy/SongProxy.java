package proxy;

import Interface.SongInterface;
import object.Song;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SongProxy {
    /**
     * 给用户类创建代理
     * 生成用户操作日志
     * 于此扩展song类的方法
     * @param song Song 歌曲
     * @return SqliterInterface 通过反射添加执行动作
     */
    public static SongInterface createProxy(Song song){
        return (SongInterface) Proxy.newProxyInstance(SongProxy.class.getClassLoader(),
                new Class[]{SongInterface.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        return method.invoke(song,args);
                    }
                }
        );

    }
}
