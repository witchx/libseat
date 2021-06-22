package com.libseat.utils.utils;

import com.libseat.api.constant.Constant;
import com.libseat.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author witch
 */
public class CodeGenerateUtils {

	@Autowired
	private RedisUtil redisUtil;


	/**
	 * 商品编号
	 * @param name
	 * @return
	 */
	public String generateProductCode(String name) {
		String timePrefix = getTimePrefix(DateUtils.YYYYMMDD);
		return name + timePrefix + String.format("%1$06d", generate(Constant.PRODUCT_REDIS_KEY+timePrefix,1,(int)(DateUtils.DAY/DateUtils.SECONDS)));
	}

	/**
	 * 订单编号
	 */
	public String generateOrderCode() {
		String timePrefix = getTimePrefix(DateUtils.YYYYMMDDHHMMSS);
		return timePrefix+String.format("%1$06d", generate(Constant.ORDER_REDIS_KEY+timePrefix,1,1));
	}

	/**
	 * 支付单号
	 * @return
	 */
	public String generatePayCode(){
		String timePrefix = getTimePrefix(DateUtils.YYYYMMDDHHMMSS);
		return timePrefix+String.format("%1$06d", generate(Constant.PAY_REDIS_KEY+timePrefix,1,2));
	}

	/**
	 * 获取有过期时间的自增长ID
	 * @param redisKey key
	 * @param increment 增量
	 * @param expireSeconds 过期时间
	 * @return
	 */
	public Long generate(String redisKey, long increment, int expireSeconds) {
		Jedis jedis = null;
		String id = "0";
		try {
			jedis = redisUtil.getJedis();
			jedis.incrBy(redisKey,increment);
			jedis.expire(redisKey, expireSeconds);
			id = jedis.get(redisKey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return Long.valueOf(id);
	}

	public String getTimePrefix(String format) {
		LocalDateTime now = LocalDateTime.now();
		return now.format(DateTimeFormatter.ofPattern(format));
	}
}
