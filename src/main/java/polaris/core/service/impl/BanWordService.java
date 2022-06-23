package polaris.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import polaris.core.mapper.BanWordMapper;

@Service
public class BanWordService extends ServiceImpl<BanWordMapper,String> implements polaris.core.service.BanWordService {
}
