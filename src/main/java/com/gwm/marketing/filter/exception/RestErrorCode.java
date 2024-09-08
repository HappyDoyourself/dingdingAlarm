package com.gwm.marketing.filter.exception;

import com.gwm.marketing.filter.anno.ResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 定义错误码与错误提示
 *
 * @author fanht
 */
public final class RestErrorCode {
    /**
     * 系统参数
     */
    @ResultMessage("成功")
    public static final int SUCCESS = 0;
    @ResultMessage("获得互斥锁超时")
    public static final int GET_LOCK_TIMEOUT = 1;
    @ResultMessage("缺少授权")
    public static final int HTTP_UNAUTHORIZED = 401;
    @ResultMessage("禁止访问")
    public static final int HTTP_FORBIDDEN = 403;
    @ResultMessage("资源不存在")
    public static final int HTTP_NOT_FOUND = 404;
    @ResultMessage("请求方法被禁止访问")
    public static final int HTTP_METHOD_NOT_ALLOWED = 405;
    @ResultMessage("系统内部发生错误")
    public static final int HTTP_SYSTEM_ERROR = 500;
    @ResultMessage("签名错误")
    public static final int SIGN_ERROR = 501;

    @ResultMessage("获取数据不唯一")
    public static final int UN_ONLYONE = 50888;

    @ResultMessage("请求服务错误")
    public static final int SERVICE_ERROR = 996;

    @ResultMessage("请求参数校验不通过{0}")
    public static final int HTTP_PARAM_INVALID = 998;
    @ResultMessage("未知错误")
    public static final int UNKNOWN_ERROR = 999;

    /**
     * 公共参数
     */
    @ResultMessage("请求参数缺失或不正确")
    public static final int PARAM_ERROR = 3000;
    @ResultMessage("没有查询到相应的结果")
    public static final int QUERY_NOT_EXIST = 3001;
    @ResultMessage("保存数据库失败")
    public static final int SAVE_DB_ERROR = 3002;
    @ResultMessage("查询数量超限")
    public static final int QUERY_OUT_OF_LIMIT = 3003;

    /**
     * 业务参数：司机
     */
    @ResultMessage("司机信息不存在")
    public static final int DRIVER_NOT_EXIST = 1000;
    @ResultMessage("司机状态不是正常启用状态")
    public static final int DRIVER_STATUS_NOT_ENABLED = 1001;
    @ResultMessage("司机手机号码与身份证号码不符合")
    public static final int DRIVER_PHONE_IDCARD_NOT_MATCHED = 1002;
    @ResultMessage("司机手机号码或司机ID两者必须传入一个")
    public static final int DRIVER_PHONE_ID_MUST_HAVE_ONE = 1003;
    @ResultMessage("登录密码不正确")
    public static final int DRIVER_LOGIN_PASSWORD_WRONG = 1004;
    @ResultMessage("超过每天换车最大次数（{0}次）")
    public static final int DRIVER_EXCEED_BINDBUS_LIMIT_PERDAY = 1005;
    @ResultMessage("无法退出并解绑车辆（您目前有待服务、服务中的任务）")
    public static final int DRIVER_CAN_NOT_UNBIND_BUS = 1006;
    @ResultMessage("无法选取车辆（您目前有服务中的任务）")
    public static final int DRIVER_CAN_NOT_BIND_BUS = 1007;
    @ResultMessage("网络繁忙,请休息会再申诉")
    public static final int DRIVER_PUNISH_APPEAL = 1008;
    @ResultMessage("已申诉或不符合申诉条件")
    public static final int DRIVER_PUNISH_NOTEXIST = 1009;
    @ResultMessage("司机质检策略不存在")
    public static final int QUALITY_INSPECT_NOT_EXIST = 1012;
    @ResultMessage("司机质检策略司机信息不完整")
    public static final int DRIVER_INFO_NOT_FULL = 1013;
    @ResultMessage("策略不存在")
    public static final int CONFIG_NOT_EXIST = 10121;
    @ResultMessage("已申诉或不符合申诉条件")
    public static final int DRIVER_PUNISH_REPETI = 1010;
    @ResultMessage("无人车配置不存在")
    public static final int DRIVERLESS_VEHICLE_CONFIG_NOTEXIST = 1014;
    @ResultMessage("司机处罚策略信息不存在")
    public static final int PUNISH_STRATEGY_NOT_EXIST = 1015;
    @ResultMessage("司机投诉处罚策略信息不存在")
    public static final int COMPLAINTPUNISH_STRATEGY_NOT_EXIST = 1016;
    @ResultMessage("网络繁忙,请稍后上传证件")
    public static final int DOUBLE_CARD_UPLOAD_BUSY = 1017;
    @ResultMessage("网络繁忙,请稍后再试")
    public static final int NETWORK_BUSY = 10171;
    @ResultMessage("已经设置过地址信息")
    public static final int DOUBLE_UPDATE_GOHOMEADDR = 1018;
    @ResultMessage("紧急联系人不能为自己手机号")
    public static final int EMERGENCY_CONTACT = 1019;
    @ResultMessage("有正在（待）服务的订单")
    public static final int HAS_SERVICE_ORDER = 1021;
    @ResultMessage("您已经预约过了")
    public static final int HAS_DRIVER_BACKFLOW = 1022;
    @ResultMessage("司机姓名不正确")
    public static final int DRIVER_NAME_ERROR = 1023;

    @ResultMessage("司机已存在")
    public static final int DRIVER_IS_EXIST = 1024;

    /**
     * 业务参数：车辆
     */
    @ResultMessage("车辆信息不存在")
    public static final int BUS_NOT_EXIST = 1100;
    @ResultMessage("车辆状态不是正常启用状态，被停止运营")
    public static final int BUS_STATUS_NOT_ENABLED = 1101;
    @ResultMessage("此车辆已被其他司机选取，请选择其它车辆")
    public static final int BUS_HAVE_BINDED_DRIVER = 1102;
    @ResultMessage("更换车辆与当前接单车辆信息一致，无需更换")
    public static final int EXIST_THE_SAME_LICENSE_PLATE = 1103;

    /**
     * 业务参数：线路
     */
    @ResultMessage("线路信息不存在")
    public static final int BUS_LINE_NOT_EXIST = 1200;
    @ResultMessage("线路当前已无效")
    public static final int BUS_LINE_INVALID = 1201;

    /**
     * 业务参数：任务
     */
    @ResultMessage("任务不存在")
    public static final int BUS_LINE_TASK_NOT_EXIST = 1300;
    @ResultMessage("任务不属于您/司机，禁止操作")
    public static final int BUS_LINE_TASK_NOT_BELONGS_DRIVER = 1301;
    @ResultMessage("任务状态参数不合法")
    public static final int BUS_LINE_TASK_STATUS_WRONG = 1302;
    @ResultMessage("任务不是待服务状态，无法开始服务")
    public static final int TASK_CANNOT_START_IF_TASK_STATUS = 1303;
    @ResultMessage("司机服务状态不是空闲或待服务状态，无法开始服务")
    public static final int TASK_CANNOT_START_IF_DRIVER_STATUS = 1304;
    @ResultMessage("任务不是服务中状态，无法结束服务")
    public static final int TASK_CANNOT_STOP_IF_TASK_STATUS = 1305;
    @ResultMessage("司机服务状态不是服务中状态，无法结束服务")
    public static final int TASK_CANNOT_STOP_IF_DRIVER_STATUS = 1306;
    @ResultMessage("任务不是服务中状态，无法操作中间站点")
    public static final int TASK_CANNOT_CHANGE_SITE = 1307;

    /**
     * 业务参数：公共基础服务
     */
    @ResultMessage("城市不存在")
    public static final int CITY_INFO_NOT_EXIST = 1400;

    /**
     * 业务参数：订单
     */
    @ResultMessage("订单不存在")
    public static final int ORDER_INFO_NOT_EXIST = 2000;
    @ResultMessage("预定人手机号或乘客手机号与此订单不相符合")
    public static final int CUSTOMER_PHONE_NOT_MATCH_ORDER = 2001;
    @ResultMessage("订单已经评价")
    public static final int ORDER_HAD_APPRAISAL = 2002;
    @ResultMessage("订单详情信息不存在")
    public static final int ORDER_DETAILINFO_NOT_EXIST = 2003;
    @ResultMessage("订单无法评价")
    public static final int ORDER_NOT_APPRAISAL = 2004;
    @ResultMessage("司机手机号与此订单不相符合")
    public static final int DRIVER_PHONE_NOT_MATCH_ORDER = 2005;
    @ResultMessage("参数status为null")
    public static final int PARAM_STATUS_ISNULL = 2010;


    /**
     * wechat====================start========================
     */
    @ResultMessage("请求参数过期或者失效")
    public static final int PARAM_FAILED = 10000;

    @ResultMessage("openid没有关联手机号提示去登录")
    public static final int OPENID_NORELEATE_PHONE = 20000;
    @ResultMessage("微信号绑定过其他手机号提示修改授权")
    public static final int DOUBLE_PHONE = 30000;


    @ResultMessage("操作类型错误")
    public static final int TYPE_ERR = 5000;

    /**wechat====================end==========================*/

    /**
     * 三方绑定====================start========================
     */
    @ResultMessage("绑定未传入手机号")
    public static final int BIND_PHONE_NONE = 13212;

    @ResultMessage("商户业务类型不存在")
    public static final int TRIPLE_BIZTYPE_ERROR = 13213;
    @ResultMessage("商户配置不存在")
    public static final int TRIPLE_SOURCE_NONE = 13214;
    @ResultMessage("商户未授权")
    public static final int TRIPLE_SOURCE_NOAUTH = 13215;


    /**三方绑定====================end==========================*/

    /**
     * carQrcode==================start=======================
     */
    @ResultMessage("二维码类型当前已无效")
    public static final int CAR_QRCODE_INVALID = 6000;
    @ResultMessage("当前车辆暂无该类型二维码")
    public static final int CAR_QRCODE_CAR_NOT_EXIST = 6100;
    /**carQrcode==================end=========================*/


    /**
     * 业务参数：司机停运
     */
    @ResultMessage("司机已经被永久停运了")
    public static final int DRIVER_HAS_OUTAGE_ALL = 7001;
    @ResultMessage("司机已经有临时停运计划")
    public static final int DRIVER_HAS_OUTAGE = 7002;
    @ResultMessage("司机没有启用的永久停运")
    public static final int DRIVER_NO_OUTAGE = 7003;
    @ResultMessage("已执行和撤销的停运记录不可以在解除")
    public static final int DRIVER_HAS_NO_REMOVE_OUTAGE = 7004;
    @ResultMessage("没有该条记录")
    public static final int NO_OUTAGE = 7005;
    /**
     * ----------driverChangeInfo--start-----------
     */
    @ResultMessage("上传行驶证与当前车牌不一致")
    public static final int DRIVING_LICENSE_DIFF = 1500;
    @ResultMessage("请上传真实行驶证照片")
    public static final int DRIVER_LICENSE_WRONG = 1501;
    @ResultMessage("上传人车合影与当前车牌不一致")
    public static final int GROUP_PHOTO_DIFF = 1502;
    @ResultMessage("请上传真实人车合影照片")
    public static final int GROUP_PHOTO_WRONG = 1503;
    @ResultMessage("该司机已存在正在审批的申请")
    public static final int ALREADY_HAVE_APPLY = 1504;
    @ResultMessage("图片过大,请重新上传")
    public static final int IMG_TOO_BIG = 1505;
    @ResultMessage("上传失败，请重试")
    public static final int IMG_WRONG_RETRY = 1506;
    @ResultMessage("暂无该品牌对应车型")
    public static final int NO_BRAND_MODEL = 1507;
    /**----------driverChangeInfo--end-----------*/

    /**
     * ===============H5 start==================
     */
    @ResultMessage("获取信息为空")
    public static final int H5_PHONE_NUM_NULL = 31000;
    @ResultMessage("修改信息失败")
    public static final int H5_UPDATE_PHONE_FAIL = 31001;
    @ResultMessage("添加信息失败")
    public static final int H5_INSERT_PHONE_FAIL = 31002;
    /**===============H5 end==================*/

    /**
     * 亲情账号====================start==============
     */

    @ResultMessage("您与该用户已经是亲情账号关系啦")
    public static final int FAMILY_ACCOUNT_BIND = 35001;
    @ResultMessage("该用户已被其他人绑定为亲情账户")
    public static final int FAMILY_ACCOUNT_BIND_OTHERS = 35002;
    @ResultMessage("您已绑定了5个亲情账户，不能再继续绑定哟")
    public static final int FAMILY_ACCOUNT_BIND_MAX = 35003;
    @ResultMessage("亲情账号主账号为空")
    public static final int MASTER_CUSTOMERID_IS_NULL = 35004;
    @ResultMessage("亲情账号子账号注册失败")
    public static final int SUB_CUSTOMER_REGISTER_FAIL = 35005;
    @ResultMessage("亲情账号根据手机号码获取不到用户信息")
    public static final int GET_CUSTOMERID_IS_NULL = 35006;
    @ResultMessage("亲情账号主账号和子账号手机号码相同")
    public static final int MASTER_SUB_PHONE_SAME = 35007;

    /**亲情账号====================end================s*/
    /**
     * 业务参数：数据报表
     */
    @ResultMessage("未查询到相应数据报表任务")
    public static final int SUBSCRIPTION_NOT_EXIST = 7010;
    @ResultMessage("数据报表：{0}")
    public static final int SUBSCRIPTION_INVALID = 7011;
    /**
     * 乘客注册记录
     */
    @ResultMessage("未查询相关乘客信息")
    public static final int CUSTOMER_REGIST_CODE = 80001;
    @ResultMessage("乘客注册记录信息已存在")
    public static final int CUSTOMER_REGIST_EXTIES_CODE = 80002;

    /**顺风车====================start================s*/

    /**
     * 顺风车车主认证流程====================start================
     */
    @ResultMessage("顺风车车主未认证")
    public static final int SFC_DRIVERINFO_NULL = 4000;


    @ResultMessage("您输入的身份证号已被{0}的车主认证，请重新输入")
    public static final int DRIVER_IDCARD_EXISTS = 4003;

    @ResultMessage("顺风车车主认证不符合当前城市条件({0})")
    public static final int SFC_CHECK_RULE_CONFIG_INFO = 4004;

    @ResultMessage("短信验证码发送尾号{0}已经超出5次")
    public static final int SFC_SENDSMS_COUNT = 4005;

    @ResultMessage("短信验证码已经过期")
    public static final int SFC_SENDSMS_OVERDUE = 4006;

    @ResultMessage("短信验证码输入错误")
    public static final int SFC_SENDSMS_WRONG = 4007;

    @ResultMessage("四要素实名认证失败原因:{0}")
    public static final int SFC_REAL_NAMEAUTH_ERROR = 4008;

    @ResultMessage("您的驾驶证号必须与实名认证的身份证号一致，请重新输入")
    public static final int SFC_LICENSE_IDENTITY_DIFF = 4009;

    @ResultMessage("该车辆不符合顺风车认证条件：{0}")
    public static final int SFC_CAR_INCONFORMITY = 4010;

    @ResultMessage("车主认证上一步未成功:{0}")
    public static final int SFC_CARSTEP_ERROR = 4011;

    @ResultMessage("您输入的车牌号已被{0}的车主认证，请重新输入")
    public static final int SFC_CAR_LICENSE_PLATES_EXISTS = 4012;

    @ResultMessage("请不要重复提交,稍后继续操作")
    public static final int SFC_REPEATED = 4013;

    @ResultMessage("您的驾龄不符合当前城市要求的{0}年驾龄以上")
    public static final int MINMAL_DRIVE_AGE_ERROR = 4014;

    @ResultMessage("您的车龄不符合当前城市要求的{0}年车龄以内")
    public static final int CAR_AGE_MAX_ERROR = 4015;

    @ResultMessage("当前操作与实名认证操作城市不符")
    public static final int CITY_NOT_EXISTS = 4016;

    @ResultMessage("不支持该银行卡或银行卡号输入有误")
    public static final int SFC_BANKCARD_VERIFY = 4017;

    @ResultMessage("此顺丰车车辆{0}不存在信息")
    public static final int SFC_BINDIMEI_EXISTS = 4018;

    @ResultMessage("顺丰车车主实名认证信息不存在")
    public static final int SFC_IDENTITY_EXISTS = 4019;

    @ResultMessage("您输入的{0}有误,请输入正确的身份证号")
    public static final int SFC_IDCARD_VERIFY = 4020;

    @ResultMessage("顺风车白名单不存在或标记无效")
    public static final int SFC_WHITE_EXISTS = 4400;

    @ResultMessage("二级白名单不存在")
    public static final int SFC_WHITETWO_EXISTS = 4402;

    @ResultMessage("超出查询最大长度200个电话号")
    public static final int SFC_WHITE_MAXSIZE = 4403;

    @ResultMessage("区域白名单不存在")
    public static final int SFC_WHITE_REGION_NULL = 4404;

    @ResultMessage("顺风车白名单信息已存在")
    public static final int SFC_WHITE_LIST_EXISTS = 4405;
    /**顺风车车主认证流程======================end==================*/


    /**
     * 顺风车评价/调查问券====================start==============
     */

    @ResultMessage("查询评价配置为空")
    public static final int EVALUATE_CONFIG_NULL = 4200;

    @ResultMessage("查询调查问卷配置为空")
    public static final int RESEARCH_CONFIG_NULL = 4201;

    @ResultMessage("查询调查问卷选项配置为空")
    public static final int RESEARCH_CONFIG_DETAIL_NULL = 4202;

    @ResultMessage("该订单已提交调查问卷")
    public static final int RESEARCH_DETAIL_NOT_NULL = 4203;

    @ResultMessage("根据单号未查询到订单信息")
    public static final int ORDER_DETAIL_NULL = 4204;

    @ResultMessage("差评时标签不可为空")
    public static final int BAD_EVAL_LABELIDS_NULL = 4205;

    @ResultMessage("该订单已提交评价")
    public static final int EVALUATE_DETAIL_NOT_NULL = 4206;

    @ResultMessage("调查问卷配置不存在")
    public static final int RESEARCH_CONFIG_ERR = 4207;

    @ResultMessage("调查问卷选项配置不存在")
    public static final int RESEARCH_CONFIG_DETAIL_ERR = 4208;

    @ResultMessage("评价配置不存在")
    public static final int EVALUATE_CONFIG_ERR = 4209;

    @ResultMessage("司机订单号不可为空")
    public static final int EVALUATE_DRIVER_ORDER_NULL = 4210;

    @ResultMessage("乘客订单号不可为空")
    public static final int EVALUATE_RIDER_ORDER_NULL = 4211;

    @ResultMessage("入参司机订单号与订单记录不一致")
    public static final int EVALUATE_MAIN_ORDER_NO_ERR = 4212;

    //顺风车评价/调查问券====================end================s

    @ResultMessage("查询车辆品牌不存在")
    public static final int SFS_NOT_EXIST_CAR_BRAND = 4301;
    @ResultMessage("查询车辆型号不存在")
    public static final int SFS_NOT_EXIST_CAR_MODEL = 4302;


    /**
     * 顺风车常用地址====================end================
     */
    @ResultMessage("超过限值")
    public static final int SFS_OFTEN_ROUTE_LINE_OUT_OR_RANGE = 4401;
    /**顺风车====================end================s*/


    /**
     * 司机打赏====================start==============
     */

    @ResultMessage("司机已收到您的打赏红包~")
    public static final int DRIVER_REWARD_ALREADY = 40000;
    @ResultMessage("后台开关开启")
    public static final int DRIVER_REWARD_SWITCH_ON = 40001;
    @ResultMessage("后台开关关闭")
    public static final int DRIVER_REWARD_SWITCH_OFF = 40002;
    @ResultMessage("已给司机{0}元感谢红包")
    public static final int DRIVER_REWARD_INFORMATION = 40003;
    @ResultMessage("该订单已被打赏")
    public static final int DRIVER_REWARD_SUCCESS = 40004;
    @ResultMessage("签名失败")
    public static final int DRIVER_REWARD_SIGN_FAIL = 40005;
    @ResultMessage("订单不存在")
    public static final int DRIVER_REWARD_ORDER_UNEXIST = 40006;
    @ResultMessage("给司机发个新年红包")
    public static final int DRIVER_REWARD_TOAST = 41000;
    @ResultMessage("您已经给司机感谢红包啦，多谢您的支持~")
    public static final int DRIVER_REWARD_PAY_TOAST = 41001;
    /**司机打赏====================end================s*/

    /**
     * 金融商品====================start=============
     */
    @ResultMessage("短信验证码发送尾号{0}已经超出6次")
    public static final int FINANCIAL_SENDSMS_COUNT = 20001;
    @ResultMessage("您好!请勿重复预约看车.")
    public static final int FINANCIAL_CLUE_EXISTS = 20002;
    /**金融商品====================end=============*/
    /**
     * 有车司机注册
     */
    @ResultMessage("没有查询到注册人信息")
    public static final int NO_REGISTER_INFO = 51001;
    @ResultMessage("OCR审核证件验车失败")
    public static final int ORC_VERIFY_FAIL = 51002;
    @ResultMessage("查询不到策略后台专车升级配置")
    public static final int NO_CONFIG = 8001;
    @ResultMessage("查询不到车辆的注册日期")
    public static final int NO_REGISTRATION_DATE = 8002;
    @ResultMessage("查询不到司机驾龄")
    public static final int NO_DRIVER_DRIVING_YEARS = 8003;
    @ResultMessage("司机身份证号不存在")
    public static final int NO_DRIVER_ID_CARD = 8004;
    @ResultMessage("查询不到司机车型信息")
    public static final int NO_DRIVER_CAR_MODEL = 8005;
    @ResultMessage("专车升级条件校验失败")
    public static final int UPGRAD_FAIR = 8006;

    /**
     * 司机垫付
     */
    @ResultMessage("司机申诉垫付失败")
    public static final int DRIVER_APPLY_ADVANCE_FAILED = 9001;
    @ResultMessage("查询订单尚未进行申诉")
    public static final int ORDER_UN_APPLY = 9002;

    /**
     * 车辆出入库==================Start===================
     */
    @ResultMessage("车辆出入库信息不存在")
    public static final int GMS_PARKINFO_NULL = 60001;
    @ResultMessage("车辆库存状态不能等于当前状态")
    public static final int GMS_PARK_STATE = 60002;
    @ResultMessage("您好,出入库时间要大于等于上次时间")
    public static final int GMS_PARK_ACCESS_TIME = 60003;



    @ResultMessage("您好,上传文件失败,请重新上传")
    public static final int UPLOADFILEFAIL = 60005;

    @ResultMessage("停车场{0},已经被停用禁止停入车辆")
    public static final int NOPARKING = 60007;

    @ResultMessage("停车场所在城市与车辆所在城市不符，请到管理后台操作！")
    public static final int CITYSAME = 60008;
    /**车辆出入库==================end===================*/

    /**
     * 司机取消扣减权益====================start=============
     */
    @ResultMessage("订单{0}已有取消扣减权益")
    public static final int ORDER_HAS_CANCEL_REDUCE_EQUITY = 2101;
    @ResultMessage("司机正在扣减取消订单权益，请稍后重试。")
    public static final int DRIVER_HAS_CANCEL_REDUCE_EQUITY = 2102;
    @ResultMessage("订单{0}无扣减取消订单权益规则。")
    public static final int ORDER_NOT_CANCEL_REDUCE_EQUITY = 2103;
    @ResultMessage("订单{0}没有取消扣减权益")
    public static final int ORDER_NO_CANCEL_REDUCE_EQUITY = 2104;


    @ResultMessage("一次只能按照200个手机号进行查询")
    public static final int DRIVER_WIDE_PHONE_LENGTH = 503;

    @ResultMessage("手机号已被占用")
    public static final int DRIVER_PHONE_IS_USED = 5004;


    @ResultMessage("司机没有查询到运营概况")
    public static final int NO_GUARANTEE_PLAN = 2105;


    /**
     * 司机宽表相关
     */
    @ResultMessage("请求参数都为空,请检查传递参数")
    public static final int DRIVER_WIDE_404 = 9404;

    @ResultMessage("手机号不合法")
    public static final int DRIVER_PHONE_ERROR = 9405;

    @ResultMessage("无效的司机id")
    public static final int DRIVER_WIDE_NOT_DRIVER = 9504;

    @ResultMessage("司机id个数超过30")
    public static final int DRIVER_WIDE_LONG_DRIVERID = 9506;

    @ResultMessage("司机手机号个数超过30")
    public static final int DRIVER_WIDE_LONG_PHONE = 9507;

    @ResultMessage("分页条数不能超过50")
    public static final int DRIVER_WIDE_PAGE_SIZE = 9508;

    /**
     * 三方对接司机保存====================start=============
     */
    @ResultMessage("三方对接司机保存,{0}")
    public static final int MIAOZOU_DRIVER_INFO_ERROR = 2110;

    @ResultMessage("三方对接司机查询接单数,{0}")
    public static final int MIAOZOU_DRIVER_INFO_NUMBER_ERROR = 2111;

    /**换车换牌====================start=============*/
    /**
     * 该错误信息为判空或不满足条件提示
     */
    @ResultMessage("换车换牌-{0}")
    public static final int DRIVER_LICENSE_UPDATE = 2601;

    @ResultMessage("换车换牌-车辆归属本加盟商且支持双班司机，车辆已绑定三个司机，请先解绑车辆和其他司机")
    public static final int DRIVER_LICENSE_UPDATE_THREE = 2602;

    /**
     * 该提示为司机已有待服务订单或者已锁定
     */
    @ResultMessage("换车换牌-{0}")
    public static final int DRIVER_LICENSE_UPDATE_LOCK = 2603;

    /**
     * 该信息提示，后台可以点击确认
     */
    @ResultMessage("换车换牌-{0}")
    public static final int DRIVER_LICENSE_UPDATE_CAR_EXIT = 2604;

    /**
     * 换车换牌====================end=============
     */

    @ResultMessage("订单处罚已存在")
    public static final int ORDERNO_EXIST = 1020;

    @ResultMessage("订单处罚不存在")
    public static final int ORDERNO_NOT_EXIST = 10230;


    @ResultMessage("该城市未设置录音配置")
    public static final int TAXI_CONFIG_NOT_EXIST = 6001;

    /**专车升级====================start=============*/
    /**
     * 该错误信息为判空或不满足条件提示
     */
    @ResultMessage("{0}")
    public static final int DRIVER_UPGRADE_NOT = 2611;
    /**专车升级====================end=============*/

    /**
     * 微信小程序智行乘车
     */
    @ResultMessage("禁止重复绑定司机车辆")
    public static final int BINDINFOEXIST = 80000;

    @ResultMessage("二维码无效")
    public static final int QQRCOED_INVALID = 80006;

    @ResultMessage("小程序二维码未绑定司机")
    public static final int BINDINFONOTEXIST = 80007;


    /**
     * 合作商管理=========
     */
    @ResultMessage("合作商不存在")
    public static final int SUPPLIER_NOT_EXIST = 88888;

    /**
     * 合作商管理=========
     */
    @ResultMessage("合作商当月数据已经存在")
    public static final int SUPPLIER_DATA_EXIST = 88889;

    @ResultMessage("手机号已被占用,请重新输入")
    public static final int DRIVER_LOGIN_PHONE_EXIST = 70020;

    @ResultMessage("您当前有待服务或未完成订单，请完成后再试")
    public static final int DRIVER_LOGIN_NOT_SERVICE_ORDER = 70018;

    @ResultMessage("验证码有误,请重新输入")
    public static final int SMSCAPTCHA_IS_ERROR = 6003;

    @ResultMessage("距您上次修改手机号码不足30天，修改失败")
    public static final int DRIVER_LOGIN_NOT_ENOUGH_TIME = 70019;

    @ResultMessage("身份证信息或密码有误")
    public static final int DRIVER_LOGIN_INFO_ERROR = 70021;

    /*****合作商五周年********/
    @ResultMessage("该合作商已被点过赞了")
    public static final int ASSIST_REPEAT = 85000;

    private static final Logger log = LoggerFactory.getLogger(RestErrorCode.class);
    /**
     * 错误码与错误文字的映射关系
     */
    private static Map<Integer, String> codeMsgMappings = new HashMap<Integer, String>();

    static {
        try {
            Field[] fields = RestErrorCode.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                ResultMessage annotation = field.getAnnotation(ResultMessage.class);
                if (annotation == null) {
                    continue;
                }
                int resultCode = field.getInt(null);
                /*错误码定义发生冲突*/
                if (codeMsgMappings.containsKey(resultCode)) {
                    String text = "[" + RestErrorCode.class.getName() + "]错误码定义发生冲突，应用进程已经退出，请解决冲突并重启服务！";
                    log.error(text);
                    System.exit(-1);
                }
                String resultMsg = annotation.value();
                if (null != resultMsg && !"".equals(resultMsg.trim())) {
                    codeMsgMappings.put(resultCode, resultMsg.trim());
                }
            }
        } catch (Exception e) {
            log.error("初始化错误码异常！", e);
        }
    }

    /**
     * 生成错误信息的字符串
     **/
    public static String renderMsg(int errorCode, Object... args) {
        String rawErrorMsg = codeMsgMappings.get(errorCode);
        if (rawErrorMsg == null) {
            return "未知错误";
        }
        return MessageFormat.format(rawErrorMsg, args);
    }

}