package com.libseat.server.framework.config;

import org.springframework.stereotype.Component;

@Component
public class AlipayConfig {
    // 沙箱appid
    public static String APP_ID = "2021000117664541";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCdYooLQr4yiRV/IdPICPXBTNTozC3zQjxc+WwiraDtzpjfbU8SdFo5OCGpbVOSwCVWwFwpWSWT4BtMsvV+yH3bXAVkSPooGEhfsJgK0QQHKwZtr8qrgpc4MyopDvTU7sr0PhM5ymdPP9zIF+z6q1ngInA6iTco68H8uXIYZbAgM5eLEQd8ZLvhq//ufGY9B5xpPN/5drjgmpVlszXti659jsnb64pHKcqCdNxydIaD1jFohgbwjS+kT2EDfZY1Se7X6SbZVujjx198wdBmkauaPUEQcT+Jq1Y0CiD4vOrKlRE7EY1Y3BhWNzw8a6aJEu/5h3x/Rny9UhVx9eRczz8nAgMBAAECggEAbSHbHxvUzy4HXfSS6e5smvEv4Y1Tf466VK4Psaw8HypXDva5Ec7kkTC/BsDsZqIeQh14yqMqT3Pi+jhNyvAzdVxF0NhZvlu4M8I8a6Or61yGRKDVJECVoz63SWk5lD1RNjOlGTmkkWtzKLyOcvNGiwjWUYC8Yv+3sArXdttsWabWGDmq0BLCyOzHExvNy1479acXAs1qaBRbUWW3sOd86mOfIZmNrz8b5fok4iCcbQhuWgCCn2QlPzyJDNWuUo2Jjw+EEPRnHBDKxzsNYK/rJ4BapRRRv9fXjXMKobI2DSYGLsPkblSlMttw3/9ZF43zEF7ym/J46ksnPbpsZLMiQQKBgQDRlzCv1WqxW352Sxs2UxAkno1tPDikhkFmVlnQzsJCfo9ra5Y5GjF1GHkOQEoYiTKDXqyuVimuOuH7Gsf8NsIncPmiUIyhrYmeRQMfL2ax2is4c6Bqi1MizFdKfCiC2Bb1byunNDONYLy6w1WNZE6aHOj84sxnSOlJDBIHan6XXwKBgQDAPAdxZd6Ol9itcbM/gveVmpUO3H5h7SjJLWcWzXJMU4oHokkbrWHoGWSud7enS1k7yANpgOkZ9xsx/BhYzeeLZIqBlG/raKvQRRMCFrZ1CGel12KsdJFCInB6HmiaVcvOkhawHlfPFGP5WlmItjltA0wfbFE56L9/ZaICT8NVOQKBgQC8cR9JOK1RAV5WpXi2A0DEa/nkePzEkCFFDeZ7Iyrt9jSk9HqApNhK29IbyhnUraZJD1pa9QVMrYx45L9iOSmfpKgOFDYdN5ZAQvRnmYrP5cRuYSeeI95/rxHc2nziB7+8tFOfZgPj3VJS6UWGduVlExE4LgJx6tA06TFckZd4KQKBgAUqV8TbSOSp/d+DO57MRRzW/4RbtmWpOrxVJPz+3kBgyke0O6SrvwK+k+8ANoYfRzGNCVJvbVQOSMPKE1lnhv4+4F3lADDiKiO5h2458AMrC6XwKK2boD+Xr1AIuyTSg1x0nnlzB0cANEOCAv/ibZ6ZRRVfErusnmCs+XJ4dDY5AoGAIeyllbS+40E7MZ4VDo+cc8yDSKEKbHyK9H0LdUKGbcOEisnmAHrK+Qz2pkyn3fhwmVp8GPsh1zkhJxYP1HyZ9D5iaisEflgXjj6K6/4/MODrqz97lGHksLpaCFcJzeGOrzRJTwvJvuS5Vz/eBVnJljqtt+Lwjpy0KQCTL/pzsAo=";
    // 请求网关  固定
    public static String URL = "https://openapi.alipaydev.com/gateway.do";
    //异步通知地址
    public static String NOTIFY_URL = "http://jstm79.natappfree.cc/api/pay/fallback";
    //同步地址
	public static String RETURN_URL = "http://localhost:3000";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtNrd7BHf4DyIfezVdKyqbzvzepwvZ37jNOVsWIS+sT8+zHZFOzzeyBIr0zr4th3zyZEc7Lt91oJscRdNtpqTPUijehiREyHe7UNL6bi9QxYCRGaA+Cz/Jj6NdJyDJGahulL8/5nXmE8rXvjmlwtqfBpJkNX2q+xzW0Ppk9K7FTC+pAHeiKrX7y7Z6VoAuXd6epnVsvAlCOcHr+THMpjCLPFrZ8qspmXVbpm7nSR9lFCMniFG09gQxjVLlQdoad450LWpv+T5XNXU8dsGwcPxJeJv6TWOiCsQe1OMeH9Y8j0oJyFKcexdUNSjFk7cUNQDh9xFaUa7wbAt4uuOa6J1fQIDAQAB";
    // 沙箱支付宝公钥
    public static String ZHIFUBAO_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnWKKC0K+MokVfyHTyAj1wUzU6Mwt80I8XPlsIq2g7c6Y321PEnRaOTghqW1TksAlVsBcKVklk+AbTLL1fsh921wFZEj6KBhIX7CYCtEEBysGba/Kq4KXODMqKQ701O7K9D4TOcpnTz/cyBfs+qtZ4CJwOok3KOvB/LlyGGWwIDOXixEHfGS74av/7nxmPQecaTzf+Xa44JqVZbM17YuufY7J2+uKRynKgnTccnSGg9YxaIYG8I0vpE9hA32WNUnu1+km2Vbo48dffMHQZpGrmj1BEHE/iatWNAog+LzqypUROxGNWNwYVjc8PGumiRLv+Yd8f0Z8vVIVcfXkXM8/JwIDAQAB";
    // RSA2
    public static String SIGN_TYPE = "RSA2";
}
