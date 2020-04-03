package com.realmax.smarthomeversion2.tcp;

import io.netty.channel.ChannelHandlerContext;

public class CustomerHandler extends NettyHandler {

    private ChannelHandlerContext handlerContext;
    private CustomerCallback customerCallback;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.handlerContext = ctx;
    }

    @Override
    public void callbackFunction(String jsonStr) {
        if (customerCallback != null) {
            customerCallback.getResultData(jsonStr);
        }
    }

    public ChannelHandlerContext getHandlerContext() {
        return handlerContext;
    }

    public void setHandlerContext(ChannelHandlerContext handlerContext) {
        this.handlerContext = handlerContext;
    }

    public CustomerCallback getCustomerCallback() {
        return customerCallback;
    }

    public void setCustomerCallback(CustomerCallback customerCallback) {
        this.customerCallback = customerCallback;
    }
}
