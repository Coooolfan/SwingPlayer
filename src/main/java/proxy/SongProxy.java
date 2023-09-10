package proxy;

import Interface.SongInterface;
import object.Song;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SongProxy {
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
