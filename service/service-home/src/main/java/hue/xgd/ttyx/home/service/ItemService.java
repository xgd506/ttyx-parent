package hue.xgd.ttyx.home.service;

import java.util.Map;

/**
 * @Author:xgd
 * @Date:2023/8/7 10:30
 * @Description:
 */
public interface ItemService {
    Map<String,Object> item(Long id, Long userId);
}
