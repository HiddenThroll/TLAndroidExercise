package com.tanlong.exercise.ui.activity;

import android.os.Bundle;
import android.util.Log;

import com.tanlong.exercise.ui.activity.base.BaseActivity;
import com.tanlong.exercise.util.DateUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 龙
 */
public class ProxyTestActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        doStaticProxy();
        doDynamicProxy();
    }

    private void doStaticProxy() {
        ICoder iCoder = new JavaCoder("zhang");
        CoderProxy coderProxy = new CoderProxy(iCoder);
        coderProxy.implementDemand("Add Reports");
    }

    private void doDynamicProxy() {
        ICoder iCoder = new JavaCoder("zhang");
        //获取中介类
        InvocationHandler invocationHandler = new CoderDynamicProxy(iCoder);
        //获取类加载器
        ClassLoader classLoader = iCoder.getClass().getClassLoader();
        //动态生成代理类
        ICoder proxy = (ICoder) Proxy.newProxyInstance(classLoader,
                iCoder.getClass().getInterfaces(), invocationHandler);
        //通过代理类执行方法
        proxy.implementDemand("Add Reports");
    }
    /**
     * ICoder接口,表征码农
     */
    private interface ICoder {
        /**
         * 实现需求
         * @param demandName -- 需求名称
         */
        void implementDemand(String demandName);
    }

    /**
     * Java码农
     */
    private class JavaCoder implements ICoder{
        private String name;

        public JavaCoder(String name) {
            this.name = name;
        }

        @Override
        public void implementDemand(String demandName) {
            Log.e("test", name + " has implement demand " + demandName);
        }
    }

    /**
     * 码农代理类,也实现ICoder接口
     * 通过内置的真实码农对象实现需求
     */
    private class CoderProxy implements ICoder {
        private ICoder iCoder;

        public CoderProxy(ICoder iCoder) {
            this.iCoder = iCoder;
        }

        @Override
        public void implementDemand(String demandName) {
            iCoder.implementDemand(demandName);
        }
    }

    /**
     * 中介类
     */
    private class CoderDynamicProxy implements InvocationHandler {
        /**
         * 被代理的示例
         */
        private ICoder iCoder;

        public CoderDynamicProxy(ICoder iCoder) {
            Log.e("test", "create CoderDynamicProxy");
            this.iCoder = iCoder;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Log.e("test", "before " + DateUtil.getCurrentDateTime());
            Object result = method.invoke(iCoder, args);
            Log.e("test", "after " + DateUtil.getCurrentDateTime());

            return result;
        }
    }
}
