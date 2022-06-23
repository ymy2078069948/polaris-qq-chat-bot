package polaris.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import polaris.core.pojo.OneMusic;

@Mapper
public interface MusicListMapper extends BaseMapper<OneMusic> {
}
