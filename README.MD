该项目主要是通过接入dingding告警方式，通过无侵入的方式，把异常信息以及错误日志实时打印，同时设置开关把每次的请求写入到es,通过配置grafana可以快速展示接口耗时以及性能跟踪
.另外接入druid 进行慢sql的实时打印功能。
社区-钉钉告警功能-接入方式：
1.在接入项目的bootstrap.properties加入配置:
spring.cloud.nacos.config.extension-configs[0].data-id=dingding-alarm-common.yaml
spring.cloud.nacos.config.extension-configs[0].group=COMMUNITY_GROUP
spring.cloud.nacos.config.extension-configs[0].refresh=true
这个是一个公共的配置，考虑到如果所有的项目都打印到一个告警群，会特别频繁，且会触发钉钉每分钟20次的上限。所以如果项目里面告警很频繁的话，可以在项目里面再去配置，这样读取配置的时候会把告警信息打印在项目添加的告警群里面。
这样做会影响原来的加载顺序，如果项目里面需要优先加载项目的配置再去加载其他的，建议修改nacos配置的加载顺序(从0开始，后面如果配置的key在相同，会替换key的value值)
2.在需要接入的项目的启动类(SpringBoot项目启动类)里面引入pom依赖，通过注解@ServletComponentScan引入，@ComponentScan指定加载包(不同服务的包名不同，如果包名相同，则默认就会加载)，修改路径即可。
pom.xml添加依赖
       <dependency>
            <groupId>com.beantechs.dingding.alarm</groupId>
            <artifactId>community-dingding-alarm</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>

SpringBoot启动类上面添加引入的class

@ServletComponentScan(basePackageClasses = {DingdingConfigurer.class})
@ComponentScan(basePackages = {"com.beantechs.filter"})
3.引入后如果有启动异常，比如Okhttp的包冲突，需要在项目里面解决包依赖冲突问题。需要对有冲突包进行设置
<!--去掉有冲突的依赖包-->
         <dependency>
            <groupId>com.beantechs.common.content.security.service</groupId>
            <artifactId>common-content-security-service</artifactId>
            <version>1.2.0-SNAPSHOT</version>
            <exclusions>
                <exclusion>
                    <groupId>com.squareup.okhttp3</groupId>
                    <artifactId>okhttp</artifactId>
                </exclusion>
            </exclusions>
         </dependency>


         <dependency>
            <groupId>com.beantechs.dingding.alarm</groupId>
            <artifactId>community-dingding-alarm</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>compile</scope>
            <exclusions>
                <exclusion>
                    <groupId>com.squareup.okhttp3</groupId>
                    <artifactId>okhttp</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
		
	    <!--重新引入-->
		<dependency>
            <groupId>com.squareup.okhttp3</groupId>
            <artifactId>okhttp</artifactId>
            <version>4.2.2</version>
        </dependency>

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


