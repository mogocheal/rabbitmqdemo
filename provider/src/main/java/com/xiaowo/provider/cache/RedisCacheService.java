package com.xiaowo.provider.cache;




import com.alibaba.fastjson.TypeReference;

import java.util.Map;
import java.util.Set;

/**
 * 缓存组件-缓存接口
 * @author znw
 *
 */
public interface RedisCacheService {

	/**
	 * 批量删除对应的value
	 * @param keys
	 */
	public void remove(final String... keys);

	/**
	 * 批量删除key
	 * @param pattern
	 */
	public void removePattern(final String pattern);

	/**
	 * 删除对应的value
	 * @param key
	 */
	public void removeObject(final String key);

	/**
	 * 添加对象到缓存
	 * @param key 缓存key
	 * @param value 缓存对象
	 * @param minutes 过期时间
	 * @throws CacheException
	 */
	 void add(String key, String value, int minutes)throws CacheException;

	/**
	 * 添加对象到缓存
	 * @param key 缓存key
	 * @param value 缓存对象
	 * @throws CacheException
	 */
	 void add(String key, String value)throws CacheException;

	/**
	 * 添加过期时间 秒
	 * @param key 缓存Key
	 * @param value 缓存Value
	 * @param seconds 过期时间 秒
	 */
	 void addAndSubsecondExpire(String key, String value, int seconds);

	/**
	 * 获取缓存对象
	 * @param key 缓存key
	 * @return String 缓存的值
	 * @throws CacheException
	 */
	 String get(String key)throws CacheException;

	/**
	 * 获得缓存数量
	 * @param keyPrefix key前缀
	 * @return 缓存数量
	 * @throws CacheException
	 */
	 long getCountLike(String keyPrefix)throws CacheException;

	/**
	 * 删除缓存
	 * @param key 缓存key
	 * @throws CacheException
	 */
	void remove(String key)throws CacheException;

	/**
	 * 模糊删除缓存
	 * @param keyPrefix 缓存前缀
	 * @throws CacheException
	 */
	 void removeLike(String keyPrefix)throws CacheException;

	/**
	 * 获得缓存Key对应的Long值
	 * @param key 缓存key
	 * @return Long 缓存值
	 * @throws CacheException
	 */
	 Long getIncrValue(String key) throws CacheException;

	/**
	 * 缓存值叠加
	 * @param key 缓存的Key
	 * @param value  缓存Value上需要增加的值，
	 * @return  Long 增加之后的值
	 */
	Long increment(final String key, Long value);

	/**
	 * 过期时间设置，分钟
	 * @param key 缓存Key
	 * @param minutes 过期时间 分钟
	 * @throws CacheException
	 */
	void expire(String key, int minutes) throws CacheException;

	/**
	 * 判断缓存Key是否存在
 	 * @param key 缓存Key
	 * @return true 存在  false 不存在
	 */
	boolean exists(final String key);

	/**
	 * 过期时间设置， 秒
	 * @param key 缓存Key
	 * @param second 秒
	 * @throws CacheException
	 */
	void expireSecond(String key, int second) throws CacheException;

	/**
     * 获取set缓存
     * @param key 缓存Key
     * @return
     */
     <T> Set<String> getSet(String key);

     /**
      * 将set写入缓存
      * @param key
      */
     <T> void addSet(String key, Set<String> set);

	/**
	 * 写入缓存
	 * @param key 缓存建
	 * @param value 缓存值
	 * @return
	 */
	public boolean set(final String key, String value);

	/**
	 * 写入缓存
	 * @param key 建
	 * @param value 值
	 * @param expireTime 过期时间
	 * @return
	 */
	public boolean set(final String key, String value, Long expireTime);

	/**
	 * 将map写入缓存
	 * @param key 键
	 * @param map 值
	 */
	<T> void hmset(String key, Map<String, T> map);

	/**
	 * 向key对应的map中添加缓存对象
	 * @param key	cache对象key
	 * @param field map对应的key
	 * @param value 	值
	 */
	void hset(String key, String field, Object value);

	/**
	 * map缓存对象累加数据 HINCR BY
	 * @param key cache对象key
	 * @param field map对应的key
	 * @param value 值
	 * @return
	 */
	Long hinc(String key, String field, long value);

	/**
	 * 指定缓存的失效时间 秒
	 * @param key 缓存KEY
	 * @param expireTime 失效时间(秒)
	 */
	void expire(String key, long expireTime);

	/**
	 * 获取map缓存中的某个对象
	 * @param key cache对象key
	 * @param field map对应的key
	 * @return
	 */
	<T> T hget(String key, String field, TypeReference<T> clazz);

	/**
	 * 获取map缓存
	 * @param key 缓存Key
	 * @return
	 */
	<T> Map<String, T> hgetAll(String key);

	/**
	 * 删除缓存<br>
	 * 根据key精确匹配删除
	 * @param key 缓存Key
	 */
	void del(String... key);

	/**
	 * 删除map中的某个对象
	 * @param key	map对应的key
	 * @param field	map中该对象的key
	 */
	void hdelField(String key, String... field);
}