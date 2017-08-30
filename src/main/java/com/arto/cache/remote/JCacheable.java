package com.arto.cache.remote;

/**
 * Created by xiong.jie on 2017-07-11.
 */
public interface JCacheable extends JCacheLiveSecs {

    /**
     * 缓存更新时间
     *
     * @return 保存时间
     */
    long getCacheUpdateTime();

    /**
     * 设置缓存更新时间
     */
    void setCacheUpdateTime();

}
