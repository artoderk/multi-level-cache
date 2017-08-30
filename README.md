# multi-level-cache
A multi-level cache framework implemented by Guava(local cache) and Redis(remote cache).

# 主要功能
* 多级缓存： 简单的接口即可实现同时针对一二级缓存的读写操作。
* 一级缓存扩散： 可选择是否扩散，扩散功能通过Redis的pub/sub实现，可将单机本地缓存的写操作扩散到集群（需要消耗正常300MS左右时间才能扩散到集群中其它缓存(扩散前本地缓存还会是老数据)）。
* 二级缓存的CAS操作： 通过Redis的Watch和Transaction功能实现对于Key的Compare and set操作。(如抢购等场景)
* 二级缓存与DB的强一致写操作： 通过CAS与版本号实现写操作与DB写操作一致，不会出现并发写时老数据覆盖缓存中的新数据(使用此方法时必需所有针对改Key的写操作都使用此功能来更新数据)。

# 开发指南
* 只使用本地缓存不扩散
```java
JCache<TestBean<TestNestBean>> cache = JCacheBuilder.newBuilder(
    "test"  // 命名，不同本地缓存必须不一样
    , new TypeReference<TestBean<TestNestBean>>(){}) // 泛型类型
    .expireAfterWrite(20) // 本地缓存过期时间(秒)
    .maximumSize(3) // 本地缓存最大key数量，默认16个
	  .build(); // 生成缓存实例
cache.set("testBean", getBean());
System.out.println(cache.get("testBean"));
```

* 只使用本地缓存并扩散
需要消耗少量时间(正常1S内)才能扩散到集群中其它缓存(扩散前本地缓存还会是老数据)。
```java
JCache<TestBean<TestNestBean>> cache = JCacheBuilder.newBuilder(
    "test"  // 命名，不同本地缓存必须不一样
	  , new TypeReference<TestBean<TestNestBean>>(){}) // 泛型类型
    .expireAfterWrite(20) // 本地缓存过期时间(秒)
    .maximumSize(3) // 本地缓存最大key数量，默认16个
    .spreadToCluster() // 本地缓存写操作扩散到集群中其它本地缓存，可选配置
    .build(); // 生成缓存实例
cache.set("testBean", getBean());
Thread.sleep(100); // 本地缓存扩散需要少量时间
System.out.println(cache.get("testBean"));
```

* 使用多级缓存
所有针对缓存的写操作都会先写完程缓存再更新本地缓存，不保证缓存一致性。
```java
JCache<TestBean<TestNestBean>> cache = JCacheBuilder.newBuilder(
    "test"  // 命名，不同本地缓存必须不一样
    , new TypeReference<TestBean<TestNestBean>>(){}) // 泛型类型
    .expireAfterWrite(20) // 本地缓存过期时间(秒)，默认远程缓存过期*2
    .maximumSize(3) // 本地缓存最大key数量，默认16个
    .spreadToCluster() // 本地缓存写操作扩散到集群中其它本地缓存，可选配置
    .useRemote() // 是否启用远程缓存，可选配置
    // .useRemote(100) //是否启用远程缓存并单独设置过期时间(秒)，可选配置
    .build(); // 生成缓存实例
cache.set("testBean", getBean());
Thread.sleep(100); // 本地缓存扩散需要少量时间
System.out.println(cache.get("testBean"));
```
* 只使用远程缓存
```java
@Autowired
private JRemoteCache jRemoteCache;
```
