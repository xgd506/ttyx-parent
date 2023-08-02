package hue.xgd.ttyx.product.service.impl;

import hue.xgd.ttyx.model.product.Comment;
import hue.xgd.ttyx.product.mapper.CommentMapper;
import hue.xgd.ttyx.product.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品评价 服务实现类
 * </p>
 *
 * @author xgd
 * @since 2023-08-02
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
