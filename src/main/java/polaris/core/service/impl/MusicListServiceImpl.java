package polaris.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import polaris.core.mapper.MusicListMapper;
import polaris.core.pojo.OneMusic;
import polaris.core.service.MusicListService;

@Service
public class MusicListServiceImpl extends ServiceImpl<MusicListMapper, OneMusic> implements MusicListService {
}
