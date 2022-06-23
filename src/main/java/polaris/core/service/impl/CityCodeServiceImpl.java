package polaris.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import polaris.core.mapper.CityCodeMapper;
import polaris.core.pojo.CityCode;
import polaris.core.service.CityCodeService;

@Service
public class CityCodeServiceImpl extends ServiceImpl<CityCodeMapper, CityCode> implements CityCodeService {
}
