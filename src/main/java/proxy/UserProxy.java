package proxy;

import Interface.UserInterface;
import object.User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Logger;

public class UserProxy {
    /**
     * 给用户类创建代理
     * 生成用户操作日志
     * 于此扩展user类的方法
     * @param user User 用户信息
     * @return UserInterface 通过反射添加执行动作
     */
    public static UserInterface createProxy(User user){
        return (UserInterface) Proxy.newProxyInstance(
                UserProxy.class.getClassLoader(),
                new Class[]{UserInterface.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Logger logger = Logger.getLogger(this.getClass().getName());
                        if ("login".equals(method.getName())){
                            logger.info("用户执行登录操作");
                        }else if ("getSongLists".equals(method.getName())){
                            logger.info("获取用户所有歌单");
                        }else if ("save".equals(method.getName())) {
                            logger.info("执行保存用户信息操作");
                        }
                        return method.invoke(user,args);
                    }
                }
        );
    }
}
