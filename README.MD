该项目主要是通过接入dingding告警方式，把异常信息以及错误日志实时打印，通过无侵入的方式，同时设置开关把每次的请求写入到es,通过配置grafana可以快速展示接口耗时以及性能跟踪
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
3.引入后如果有启动异常，比如Okhttp的包冲突，需要在项目里面解决包依赖冲突问题。以bt-community-business-service为例(敏感词过滤有依赖OkHttp)
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