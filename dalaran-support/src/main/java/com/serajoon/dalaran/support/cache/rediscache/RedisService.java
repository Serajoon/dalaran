package com.serajoon.dalaran.support.cache.rediscache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redisTemplate.opsForValue();//操作字符串
 * redisTemplate.opsForList();//操作list
 * redisTemplate.opsForHash();//操作hash
 * redisTemplate.opsForSet();//操作set
 * redisTemplate.opsForZSet();//操作有序set
 *
 * @param <K> 主键类型
 * @param <V> 值类型
 */
@Service
@SuppressWarnings({"all"})
public class RedisService<K, V> {

    @Resource
    private RedisTemplate<K, V> redisTemplate;

    public RedisTemplate<K, V> getInstance() {
        return redisTemplate;
    }
    /*
     ***********
     * 通用操作
     ***********
     */
    /**
     * 删除指定key的缓存
     *
     * @param key 要删除的主键名
     */
    public void delete(K key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    /**
     * 删除指定key的缓存
     *
     * @param keys 要删除的主键名集合
     */
    public void delete(Collection<K> keys) {
        redisTemplate.opsForValue().getOperations().delete(keys);
    }

    /**
     * 给一个指定的 key 值附加过期时间
     *
     * @param key  主键
     * @param time 过期时长
     * @param type 时间单位
     * @return boolean
     */
    public boolean expire(K key, long time, TimeUnit type) {
        Boolean expire = redisTemplate.boundValueOps(key).expire(time, type);
        return expire == null ? false : expire;
    }

    /**
     * 移除指定key的过期时间
     *
     * @param key 主键
     * @return 是否移除成功
     */
    public boolean persist(K key) {
        Boolean persist = redisTemplate.boundValueOps(key).persist();
        return persist == null ? false : persist;
    }

    /**
     * 获取指定key 的过期时间
     *
     * @param key 主键
     * @return
     */
    public Long getExpire(K key) {
        return redisTemplate.boundValueOps(key).getExpire();
    }

    /**
     * 修改key
     *
     * @param key 主键
     * @return
     */
    public void rename(K key, K newKey) {
        redisTemplate.boundValueOps(key).rename(newKey);
    }


    /*
     ***********
     * String
     ***********
     */
    /**
     * 设置 String 类型 key-value
     */
    public void valueSet(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置 String 类型 key-value,并增加过期时间
     */
    public void valueSet(K key, V value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置 String 类型 key-value 并添加过期时间 (分钟单位)
     *
     * @param time 过期时间,分钟单位
     */
    public void valueSetForTimeMinutes(K key, V value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }

    /**
     * 获取 String 类型 key-value
     *
     * @param key 主键
     * @return 值
     */
    public V valueGet(K key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 如果 key 存在则覆盖,并返回旧值.
     * 如果不存在,返回null 并将新值添加
     * 不能设置过期时间
     *
     * @param key   主键
     * @param value 值
     * @return 旧值
     */
    public V valueGetAndSet(K key, V value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 批量添加 key-value (重复的键会覆盖)
     *
     * @param keyAndValue 缓存的map集合
     */
    public void valueMultiSet(Map<K, V> keyAndValue) {
        redisTemplate.opsForValue().multiSet(keyAndValue);
    }

    /**
     * 批量添加 key-value 只有在键不存在时,才添加
     * map中只要有一个key存在,则全部不添加
     *
     * @param keyAndValue
     */
    public void valueMultiSetIfAbsent(Map<K, V> keyAndValue) {
        redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
    }

    /**
     * 对一个 key-value的值进行加减操作,
     * 如果该 key不存在将创建一个key并赋值该number
     * 如果key存在,则在原值的基础上增加number
     * 如果key存在,但value不是长整型,将报错
     *
     * @param key
     * @param number
     */
    public Long valueIncrement(K key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是 纯数字 ,将报错
     *
     * @param key
     * @param number
     */
    public Double valueIncrement(K key, double number) {
        return redisTemplate.opsForValue().increment(key, number);
    }


    /*
     ***********
     * List
     * head---->tail
     ***********
     */

    /**
     * 指定list从左入栈
     *
     * @param key 主键
     * @return 当前队列的长度
     */
    public Long listLeftPush(K key, V value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 指定list从左出栈,删除并获取列表中的第一个元素
     *
     * @param key 主键
     * @return 出栈的值
     */
    public V listLeftPop(K key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从左(头)边依次入栈
     * 导入顺序按照Collection顺序
     * 如: [a,b,c] => [c,b,a]
     *
     * @param key    主键
     * @param values 值的集合
     * @return 当前队列的长度
     */
    public Long listLeftPushAll(K key, Collection<V> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 指定list,从右(尾)入栈
     *
     * @param key
     * @return 当前队列的长度
     */
    public Long listRightPush(K key, V value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 指定list从右出栈,并删除
     *
     * @param key
     * @return 出栈的值
     */
    public V listRightPop(K key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 从右边依次入栈
     * 导入顺序按照 Collection 顺序
     * 如: a b c => a b c
     *
     * @param key
     * @param values
     * @return
     */
    public Long listRightPushAll(K key, Collection<V> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }


    /**
     * 根据下标获取值,下表值从0开始,[0,1,2....]
     *
     * @param key   主键
     * @param index 下表
     * @return 缓存值
     */
    public V listGetIndex(K key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }


    /**
     * 获取列表指定长度
     *
     * @param key 主键
     * @return list的长度
     */
    public Long listSize(K key) {
        return redisTemplate.opsForList().size(key);
    }


    /**
     * 获取列表指定范围内的所有值
     * [start,end]
     *
     * @param key   主键
     * @param start 开始
     * @param end   结束
     * @return List
     */
    public List<V> listRange(K key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }


    /**
     * 删除key中值为value的count个数.
     *
     * @param key   主键
     * @param count 数量
     * @param value 值V
     * @return 成功删除的个数
     */
    public Long listRemove(K key, long count, V value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }


    /**
     * 删除列表[start,end]以外的所有元素,即只保留区间之内的元素
     *
     * @param key   主键
     * @param start 开始
     * @param end   结束
     */
    public void listTrim(K key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);

    }

    /**
     * 将key右出栈,并左入栈到key2
     *
     * @param key  右出栈的列表
     * @param key2 左入栈的列表
     * @return 操作的值
     */
    public V listRightPopAndLeftPush(K key, K key2) {
        return redisTemplate.opsForList().rightPopAndLeftPush(key, key2);
    }


    /*
     ***********
     * Hash
     ***********
     */
    /**
     * 删除一个或多个哈希字段
     */
    public Long hashDelete(K key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 验证指定key下有没有指定的hashkey
     *
     * @param
     * @return
     */
    public boolean hashHasKey(K key, Object hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取存储在指定键的哈希字段的值
     *
     * @param
     * @return
     * @author hanmeng1
     * @since 2019/4/12 11:10
     */
    public Object hashGet(K key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 返回map集合
     *
     * @param
     * @return
     * @author hanmeng1
     * @since 2019/4/12 11:05
     */
    public Map<Object, Object> hashEntries(K key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 将哈希字段的整数值按给定数字增加,并返回修改后的值
     *
     * @param
     * @return
     * @author hanmeng1
     * @since 2019/4/12 10:58
     */
    public Double hashIncrementDouble(K key, Object hashKey, double delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 将哈希字段的整数值按给定数字增加,并返回修改后的值
     *
     * @param
     * @return
     * @author hanmeng1
     * @since 2019/4/12 10:58
     */
    public Long hashIncrementLong(K key, Object hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 返回hash中map的key的Set集合
     *
     * @param key 主键
     * @return hashkey的set集合
     * @author hanmeng1
     * @since 2019/4/12 11:14
     */
    public Set<Object> hashKeys(K key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取hash中的字段数量
     *
     * @param key 主键
     * @return 获取hash中的字段数量
     * @author hanmeng1
     * @since 2019/4/12 10:55
     */
    public Long hashSize(K key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 获取所有给定哈希字段的值
     *
     * @param key      主键
     * @param hashKeys hashKey
     * @return List<Object>
     * @author hanmeng1
     * @since 2019/4/12 10:47
     */
    public List<Object> hashMultiGet(K key, Collection<Object> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    /**
     * 设置hash字段的字符串值
     *
     * @param
     * @return
     * @author hanmeng1
     * @since 2019/4/12 10:17
     */
    public void hashPutAll(K key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 设置hash字段的字符串值
     *
     * @param key     主键
     * @param hashKey hashkey
     * @param value   值
     * @author hanmeng1
     * @since 2019/4/12 10:17
     */
    public void hashPut(K key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 仅当字段不存在时，才设置hash字段的值
     *
     * @param key     主键
     * @param hashKey hashkey
     * @param value   值
     * @author hanmeng1
     * @since 2019/4/12 10:32
     */
    public void hashPutIfAbsent(K key, Object hashKey, Object value) {
        redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * 返回map中的value集合List
     *
     * @param key 主键
     * @return valuelist
     * @author hanmeng1
     * @since 2019/4/12 10:35
     */
    public List<Object> hashValues(K key) {
        return redisTemplate.opsForHash().values(key);
    }


    /*
     * 无序不重复集合
     ***********
     * Set
     ***********
     */
    /**
     * 添加 set 元素
     * @param key 主键
     * @param values 值
     * @return
     */
    public Long setsAdd(K key ,V ...values){
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取两个集合的差集
     * @param key 集合A
     * @param otherkey 集合B
     * @returnV
     */
    public Set<V> setsDifference(K key ,K otherkey){
        return redisTemplate.opsForSet().difference(key, otherkey);
    }


    /**
     * 获取key和集合collections中的key集合的差集
     * @param key
     * @param collections
     * @return
     */
    public Set<V> setsDifference(K key ,Collection<K> otherKeys){
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * 将  key 与 otherkey 的差集 ,添加到新的 newKey 集合中
     * @param key
     * @param otherkey
     * @param newKey
     * @return 返回差集的数量
     */
    public Long setsDifferenceAndStore(K key ,K otherkey,K newKey){
        return redisTemplate.opsForSet().differenceAndStore(key, otherkey, newKey);
    }

    /**
     * 将 key 和 集合  collections 中的 key 集合的差集 添加到  newkey 集合中
     * @param key
     * @param otherKeys
     * @param newKey
     * @return 返回差集的数量
     */
    public Long setsDifferenceAndStore(K key,Collection<K> otherKeys,K newKey){
        return redisTemplate.opsForSet().differenceAndStore(newKey, otherKeys, newKey);
    }

    /**
     * 删除一个或多个集合中的指定值
     * @param key
     * @param values
     * @return 成功删除数量
     */
    public Long setsRemove(K key,V ...values){
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 随机移除一个元素,并返回出来
     * @param key
     * @return
     */
    public V setsRandomSetPop(K key){
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 随机获取一个元素
     * @param key
     * @return
     */
    public V setsRandomSet(K key){
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 随机获取指定数量的元素,同一个元素可能会选中两次
     * @param key
     * @param count
     * @return
     */
    public List<V> setsRandomSet(K key,long count){
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取指定数量的元素,去重(同一个元素只能选择两一次)
     * @param key
     * @param count
     * @return
     */
    public Set<V> setsRandomSetDistinct(K key,long count){
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * 将 key 中的 value 转入到 destKey 中
     * @param key
     * @param value
     * @param destKey
     * @return 返回成功与否
     */
    public boolean setsMoveSet(K key,V value,K destKey){
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 无序集合的大小
     * @param key
     * @return
     */
    public Long setsSize(K key){
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 判断 set 集合中 是否有 value
     * @param key
     * @param value
     * @return
     */
    public boolean setsIsMember(K key,V value){
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 返回 key 和 othere 的并集
     * @param key
     * @param otherKey
     * @return
     */
    public Set<V> setsUnionSet(K key,K otherKey){
        return redisTemplate.opsForSet().union(key, otherKey);
    }

    /**
     * 返回 key 和 otherKeys 的并集
     * @param key
     * @param otherKey key 的集合
     * @return
     */
    public Set<V> setsUnionSet(K key,Collection<K> otherKeys){
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * 将 key 与 otherKey 的并集,保存到 destKey 中
     * @param key
     * @param otherKey
     * @param destKey
     * @return destKey 数量
     */
    public Long setsUnionAndStoreSet(K key, K otherKey,K destKey){
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * 将 key 与 otherKey 的并集,保存到 destKey 中
     * @param key
     * @param otherKeys
     * @param destKey
     * @return destKey 数量
     */
    public Long setsUnionAndStoreSet(K key, Collection<K> otherKeys,K destKey){
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 返回集合中所有元素
     * @param key
     * @return
     */
    public Set<V> setsMembers(K key){
        return redisTemplate.opsForSet().members(key);
    }

    /*
     * 根据socre排序不重复每个元素附加一个socre,double类型的属性(double可以重复)
     ***********
     * ZSet
     ***********
     */

    /**
     * 添加 ZSet元素,根据score从小到大排序
     * @param key
     * @param value
     * @param score
     */
    public boolean zsetAdd(K key,V value,double score){
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 批量添加
     * @param key
     * @param tuples
     * @return
     */
    public Long zsetBatchAdd(K key,Set<TypedTuple<V>> tuples){
        return redisTemplate.opsForZSet().add(key, tuples);
    }

    /**
     * Zset 删除一个或多个元素
     * @param key
     * @param values
     * @return
     */
    public Long zsetRemove(K key,V ...values){
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 对指定的 zset 的 value 值 , socre 属性做增减操作
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Double zsetIncrementScore(K key,V value,double score){
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 获取 key 中指定 value 的排名(从0开始,从小到大排序)
     * @param key
     * @param value
     * @return
     */
    public Long zsetRank(K key,V value){
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 获取 key 中指定 value 的排名(从0开始,从大到小排序)
     * @param key
     * @param value
     * @return
     */
    public Long zsetReverseRank(K key,V value){
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从小到大,带上分数)
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<TypedTuple<V>> zsetRangeWithScores(K key, long start, long end){
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从小到大,只有列名)
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<V> zsetRange(K key, long start, long end){
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,只有列名)
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<V> zsetRangeByScore(K key, double min, double max){
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从小到大,集合带分数)
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<TypedTuple<V>> zsetRangeByScoreWithScores(K key, double min, double max){
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,不带分数的集合)
     * @param key
     * @param min
     * @param max
     * @param offset 从指定下标开始
     * @param count 输出指定元素数量
     * @return
     */
    public Set<V> zsetRangeByScore(K key, double min, double max,long offset,long count){
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    /**
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从小到大,带分数的集合)
     * @param key
     * @param min
     * @param max
     * @param offset 从指定下标开始
     * @param count 输出指定元素数量
     * @return
     */
    public Set<TypedTuple<V>> zsetRangeByScoreWithScores(K key, double min, double max,long offset,long count){
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从大到小,只有列名)
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<V> zsetReverseRange(K key,long start,long end){
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取索引区间内的排序结果集合(从0开始,从大到小,带上分数)
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<TypedTuple<V>> zsetReverseRangeWithScores(K key,long start,long end){
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合不带分数)
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<V> zsetReverseRangeByScore(K key,double min,double max){
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 获取分数范围内的 [min,max] 的排序结果集合 (从大到小,集合带分数)
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<TypedTuple<V>> zsetReverseRangeByScoreWithScores(K key,double min,double max){
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,不带分数的集合)
     * @param key
     * @param min
     * @param max
     * @param offset 从指定下标开始
     * @param count 输出指定元素数量
     * @return
     */
    public Set<V> zsetReverseRangeByScore(K key,double min,double max,long offset,long count){
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * 返回 分数范围内 指定 count 数量的元素集合, 并且从 offset 下标开始(从大到小,带分数的集合)
     * @param key
     * @param min
     * @param max
     * @param offset 从指定下标开始
     * @param count 输出指定元素数量
     * @return
     */
    public Set<TypedTuple<V>> zsetReverseRangeByScoreWithScores(K key,double min,double max,long offset,long count){
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * 返回指定分数区间 [min,max] 的元素个数
     * @param key
     * @param min
     * @param max
     * @return
     */
    public long zsetCount(K key,double min,double max){
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 返回 zset 集合数量
     * @param key
     * @return
     */
    public long zsetSize(K key){
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取指定成员的 score 值
     * @param key
     * @param value
     * @return
     */
    public Double zsetScore(K key,Object value){
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 删除指定索引位置的成员,其中成员分数按( 从小到大 )
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zsetRemoveRange(K key,long start ,long end){
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 删除指定 分数范围 内的成员 [main,max],其中成员分数按( 从小到大 )
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zsetRemoveRangeByScore(K key,double min ,double max){
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     *  key 和 other 两个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long zsetUnionAndStoreZset(K key,K otherKey,K destKey){
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     *  key 和 otherKeys 多个集合的并集,保存在 destKey 集合中, 列名相同的 score 相加
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long zsetUnionAndStoreZset(K key,Collection<K> otherKeys,K destKey){
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     *  key 和 otherKey 两个集合的交集,保存在 destKey 集合中
     * @param key
     * @param otherKey
     * @param destKey
     * @return
     */
    public Long zsetIntersectAndStore(K key,K otherKey,K destKey){
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     *  key和 otherKeys 多个集合的交集,保存在 destKey 集合中
     * @param key
     * @param otherKeys
     * @param destKey
     * @return
     */
    public Long zsetIntersectAndStore(K key,Collection<K> otherKeys,K destKey){
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }


}
