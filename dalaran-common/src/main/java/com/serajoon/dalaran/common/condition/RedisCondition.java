package com.serajoon.dalaran.common.condition;


import com.serajoon.dalaran.common.util.MyNetUtils;
import com.serajoon.dalaran.common.util.MyLogUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;


public class RedisCondition implements Condition {
    private Logger log = MyLogUtils.log(getClass());
    @Override
    public boolean matches(@NotNull ConditionContext context, @NotNull AnnotatedTypeMetadata metadata) {
        String ip = context.getEnvironment().getProperty("spring.redis.host","localhost");
        String port = context.getEnvironment().getProperty("spring.redis.port","6379");
        boolean connectable = MyNetUtils.isIpAndPortReachable(ip, Integer.valueOf(port));
        if(!connectable){
            log.error("redis is not unable to connect");
        }
        return connectable;
    }
}