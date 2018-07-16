package com.xiaowo.provider.cache.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xiaowo.provider.cache.CacheException;
import com.xiaowo.provider.cache.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存服务
 * @author gunj
 * create by 2018-04-13
 * */
@Service
public class RedisCacheServiceImpl implements RedisCacheService {

	private final static Logger logger = LoggerFactory.getLogger(RedisCacheServiceImpl.class);

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void remove(final String... keys) {
		for (String key : keys) {
			removeObject(key);
		}
	}

	@Override
	public void removePattern(final String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0){
			redisTemplate.delete(keys);
		}
	}

	@Override
	public void removeObject(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public boolean exists(final String key) {

		return redisTemplate.hasKey(key);
	}

	@Override
	public boolean set(final String key, String value) {
		boolean result = false;
		try {
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	@Override
	public boolean set(final String key, String value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	@Override
	public Long increment(final String key, Long value) throws CacheException {
		Long result = 0L;
		try {
			Long operations = redisTemplate.opsForValue().increment(key, value);
			result = operations;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
		return result;
	}

	@Override
	public Long getIncrValue(String key) throws CacheException {
		Long result = 0L;
		try {
			Long operations = redisTemplate.opsForValue().increment(key, 0L);
			result = operations;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
		return result;
	}
	

	@Override
	public long getCountLike(String keyPrefix) throws CacheException {
		try {
			if (StringUtils.isEmpty(keyPrefix)) {
				return 0;
			} else {
				Set<String> matchedCacheKeys = redisTemplate.keys(keyPrefix + "*");
				return matchedCacheKeys.size();
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
		}
	}

	@Override
	public void remove(String key) throws CacheException {
		try {
			if (exists(key)) {
				redisTemplate.delete(key);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
		}

	}

	@Override
	public void removeLike(String keyPrefix) throws CacheException {
		try {
			if (!StringUtils.isEmpty(keyPrefix)) {
				Set<String> matchedCacheKeys = redisTemplate.keys(keyPrefix + "*");
				for (String cacheKey : matchedCacheKeys) {
					this.remove(cacheKey);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", ex.getMessage());
		}
	}

	@Override
	public void expire(String key, int minutes) throws CacheException {
		try {
			redisTemplate.expire(key, minutes, TimeUnit.MINUTES);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public void add(String key, String value, int minutes) throws CacheException {
		try {
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value, minutes, TimeUnit.MINUTES);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
		
	}

	@Override
	public void addAndSubsecondExpire(String key, String value, int seconds) throws CacheException {
		try {
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value, seconds*1L);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public void add(String key, String value) throws CacheException {
		try {
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public String get(String key) throws CacheException {
		try {
			Object object = redisTemplate.opsForValue().get(key);
			if(null!=object){
				return object.toString();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public void expireSecond(String key, int second) throws CacheException {
		try {
			redisTemplate.expire(key, second, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public <T> Set<String> getSet(String key) {
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
            throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public <T> void addSet(String key, Set<String> set) {
		try {
			String[] strs = new String[set.size()];
			redisTemplate.opsForSet().add(key, set.toArray(strs));
		} catch (Exception e) {
			logger.error(e.getMessage());
            throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}		
	}

	@Override
	public <T> void hmset(String key, Map<String, T> map) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public void hset(String key, String field, Object value) {
		String cacheValue;
		if (value instanceof String){
			cacheValue = (String)value;
		} else {
			cacheValue = JSONObject.toJSONString(value);
		}
		try {
			redisTemplate.opsForHash().put(key, field, cacheValue);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public Long hinc(String key, String field, long value) {
		try {
			return redisTemplate.opsForHash().increment(key,field,value);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public void hdelField(String key, String... field){
		try {
			BoundHashOperations<String, String, ?> boundHashOperations = redisTemplate.boundHashOps(key);
			boundHashOperations.delete(field);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public void expire(String key, long expireTime) {
		try {
			if(expireTime > 0){
				redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public <T> T hget(String key, String field, TypeReference<T> clazz) {
		try {
			BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
			String value = boundHashOperations.get(field);
			return JSONObject.parseObject(value,clazz);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public <T> Map<String, T> hgetAll(String key) {
		try{
			BoundHashOperations<String, String, T> boundHashOperations = redisTemplate.boundHashOps(key);
			return boundHashOperations.entries();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}

	@Override
	public void del(String... key) {
		try{
			if(key!=null && key.length > 0){
				if(key.length == 1){
					redisTemplate.delete(key[0]);
				}else{
					redisTemplate.delete(CollectionUtils.arrayToList(key));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new CacheException("EXC_CACHE_ERROR", e.getMessage());
		}
	}
}
