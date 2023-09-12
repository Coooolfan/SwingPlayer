package proxy;

import Interface.AudioManagerInterface;
import object.AudioManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Logger;

public class AudioManagerProxy {
    /**
     * 给用户类创建代理
     * 生成用户操作日志
     * 于此扩展AudioManager类的方法
     * @param audioManager AudioManager
     * @return AudioManagerInterface 通过反射添加执行动作
     */
    public static AudioManagerInterface createProxy(AudioManager audioManager){
        return (AudioManagerInterface) Proxy.newProxyInstance(
                AudioManagerProxy.class.getClassLoader(),
                new Class[]{AudioManagerInterface.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Logger logger = Logger.getLogger(this.getClass().getName());
                        if ("start".equals(method.getName())){
                            logger.info("歌曲播放");
                        } else if ("pause".equals(method.getName())) {
                            logger.info("歌曲暂停");
                        }
                        return method.invoke(audioManager,args);
                    }
                }
        );
    }
}
