package proxy;

import Interface.AudioManagerInterface;
import object.AudioManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AudioManagerProxy {
    public static AudioManagerInterface createProxy(AudioManager audioManager){
        return (AudioManagerInterface) Proxy.newProxyInstance(
                AudioManagerProxy.class.getClassLoader(),
                new Class[]{AudioManagerInterface.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("start".equals(method.getName())){
                            System.out.println("歌曲播放");
                        } else if ("pause".equals(method.getName())) {
                            System.out.println("歌曲暂停");
                        }
                        return method.invoke(audioManager,args);
                    }
                }
        );
    }
}
