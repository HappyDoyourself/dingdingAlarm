#### 该项目主要是通过接入dingding告警方式，通过无侵入的方式，把异常信息以及错误日志指定时间(比如1分钟)后进行告警提示，可以把每次的请求信息以及耗时信息写入到es,通过配置grafana可以快速展示接口耗时以及性能跟踪.另外接入druid 进行慢sql的实时打印功能。


展示效果：

接口异常告警:60秒内总告警条数:13条;{"applicationName":"gwm-marketing-community-server","env":"prod","ip":"192.168.33.142","exMessage":"server is DOWNnow, detailed error message: Optional[Distro protocol is not initialized]","requestUri":"com.alibaba.nacos.client.naming"}:11条{"applicationName":"gwm-marketing-community-server","env":"prod","ip":"192.168.33.142","exMessage":"server is DOWNnow, please try again later!","requestUri":"com.alibaba.nacos.client.naming"}:2条

sql超时告警:
sql语句: select
        t1.id id,
        t1.user_id userId,
        t1.total_score totalScore,
        t1.usable_score usableScore,
        t1.growth growth,
        t1.count count,
        t1.continuous_count continuousCount,
        t1.is_enable isEnable,
        t1.is_recommend isRecommend,
        t1.is_car_attest isCarAttest,
        t1.is_plus isPlus,
        t1.is_mute isMute,
        t1.fans_count fansCount,
        t1.follow_count followCount,
        t1.release_count releaseCount,
        t1.essence_count essenceCount,
        t1.comment_count commentCount,
        t1.like_count likeCount,
        t1.favorites_count favoritesCount,
        t1.source_app sourceApp,
        t1.device_id deviceId,
        t1.bean_id beanId,
        t2.sex sex,
        t2.phone,
        t2.avatar,
        t2.register_time registerTime,
        ifnull(t2.nick_name,"") nickName,
        t2.is_invite isInvite,
        t2.birthday birthday,
        t2.email email,
        t2.personal_profile personalProfile
        from user_detail t1
        left join user_info t2 on t2.id=t1.user_id
        where t1.source_app=?
        and t1.is_recommend=1
            and t2.id not in
             (                ?
             , 
                ?
             ) 
            and t2.id != ? LIMIT ?  
参数  :["ORA","U618399202262646784","U775611839773061120","U775611839773061120",80] ;
traceId  :cf147193b9604123b6a33ee5f9229982 ;
maxTime  :1163毫秒 ;
执行次数:1次;
1分钟内慢sql总条数: 1条 
应用名称:gwm-marketing-uc-server
登录环境:prod
登录地址:192.168.33.229


